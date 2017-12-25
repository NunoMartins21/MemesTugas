package com.numiitech.memestugas;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.numiitech.memestugas.R;
import com.numiitech.memestugas.classes.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // App Settings Class
    App app = new App();

    // Firebase
    private FirebaseAnalytics mFirebaseAnalytics;

    // MediaPlayer
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Components
        final ListView memes = (ListView) findViewById(R.id.memesList);

        // List Related Stuff
        final List<Meme> list = app.memesList();
        ArrayAdapter<Meme> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        memes.setAdapter(adapter);

        memes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Firebase - Bundle
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Meme");
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(list.indexOf(list.get(i))));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, list.get(i).getName());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                try {
                    stopPlaying();
                    // Media Player Instance and Settings
                    mp = MediaPlayer.create(getApplicationContext(), list.get(i).getPath());
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            stopPlaying();
                        }
                    });
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.exception_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        memes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                try {
                    // Instantiate the AlertDialog class
                    final AlertDialog.Builder shareDialog = new AlertDialog.Builder(MainActivity.this);

                    shareDialog.setTitle(getString(R.string.shareDialog_title) + ": " + list.get(i).getName())
                            .setItems(new String[]{getString(R.string.shareDialog_share), getString(R.string.shareDialog_exit)}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int d) {
                                    switch (d) {
                                        case 0:
                                            // Create new folder + Move file to external Storage
                                            File folder = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
                                            File f;
                                            if (!folder.exists()) {
                                                folder.mkdir();
                                                f = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name), getResources().getResourceEntryName(list.get(i).getPath()) + ".mp3");
                                            }
                                            else {
                                                f = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name), getResources().getResourceEntryName(list.get(i).getPath()) + ".mp3");
                                            }

                                            try {
                                                InputStream iS = getResources().openRawResource(list.get(i).getPath());
                                                FileOutputStream fOS = new FileOutputStream(f);

                                                byte[] buffer = new byte[1024];
                                                int lenght;
                                                while ((lenght = iS.read(buffer)) > 0) {
                                                    fOS.write(buffer, 0, lenght);
                                                }

                                                iS.close();
                                                fOS.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            // Create share intent
                                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                            sharingIntent.setType("audio/mp3");
                                            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + getResources().getResourceEntryName(list.get(i).getPath()) + ".mp3"));
                                            startActivityForResult(Intent.createChooser(sharingIntent, getString(R.string.shareDialog_title)), app.SHARE_MEME);
                                            break;
                                        case 1:
                                            dialog.cancel();
                                            break;
                                    }
                                }
                            })
                            .create()
                            .show();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.exception_error), Toast.LENGTH_LONG).show();
                }

                return false;
            }
        });
    }

    // To stop playing the meme
    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sobre) {
            Intent sobre = new Intent(this, Sobre.class);
            startActivity(sobre);
            Toast.makeText(getApplicationContext(), getString(R.string.action_sobre), Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_share) {
            // Firebase - Bundle
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "App");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);

            try {
                // Get app version
                PackageInfo pi = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                String verStr = pi.versionName;

                // Create share intent
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String body = getString(R.string.app_name) + " (" + getString(R.string.version) + " " + verStr + ") - " + getString(R.string.available_at) + " https://goo.gl/pGjZ5n";
                sharingIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.sharing)));
            }
            catch (PackageManager.NameNotFoundException e) {
                // Create share intent (without version)
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String body = getString(R.string.app_name) + " - " + getString(R.string.available_at) + " https://goo.gl/pGjZ5n";
                sharingIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.sharing)));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == app.SHARE_MEME) {
            // Confirmation Message
            Toast.makeText(getApplicationContext(), getString(R.string.shareDialog_success), Toast.LENGTH_SHORT).show();

            File dir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));

            // Delete the memes
            if (dir.exists()) {
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.directory_notExist), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            // Error Message
            Toast.makeText(getApplicationContext(), getString(R.string.exception_error), Toast.LENGTH_SHORT).show();
        }
    }
}
