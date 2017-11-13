package com.numiitech.memestugas;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAnalytics mFirebaseAnalytics;

    // MediaPlayer
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Components
        final ListView memes = (ListView) findViewById(R.id.memesList);

        // List Related Stuff
        final List<Meme> list = allMemes();
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

                // Media Player Instance and Settings
                mp = MediaPlayer.create(getApplicationContext(), list.get(i).getPath());

                try {
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.exception_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<Meme> allMemes() {
        return new ArrayList<>(Arrays.asList(
                new Meme("Aprendizagem", R.raw.aprendizagem),
                new Meme("Bocado Rijo", R.raw.bocado_rijo),
                new Meme("É um cabrão?", R.raw.cabrao),
                new Meme("Cheira mal", R.raw.cheira_mal),
                new Meme("Cocó Enrolado", R.raw.coco_enrolado),
                new Meme("Droga!", R.raw.droga),
                new Meme("Epah, cala-te!", R.raw.epah_calate),
                new Meme("Já tou a falar espanhol...", R.raw.espanhol),
                new Meme("Espetáculo!", R.raw.espetaculo),
                new Meme("Fazer uma ganza", R.raw.fazer_ganza),
                new Meme("Na feira da Ladra", R.raw.feira_ladra),
                new Meme("Ganza na areia", R.raw.ganza_areia),
                new Meme("Que informação dramática...", R.raw.info_dramatica),
                new Meme("Leave me Alone!", R.raw.leave_alone),
                new Meme("Maria Leal", R.raw.maria_leal),
                new Meme("Nem quero comentar", R.raw.nem_comentar),
                new Meme("Passaste!", R.raw.passaste),
                new Meme("Perdemos? Que se f#&?!", R.raw.perdemos),
                new Meme("Tem planos para jantar?", R.raw.planos_jantar),
                new Meme("Drogados a caçar Pokémons", R.raw.pokemons),
                new Meme("Porra pá!", R.raw.porra_pa),
                new Meme("Tu és solteiro ca#!%?!", R.raw.solteiro),
                new Meme("Acabou o sossego!", R.raw.sossego),
                new Meme("Vou-te comer (e comi!)", R.raw.vou_comer),
                new Meme("Windoohhh!", R.raw.windoh),
                new Meme("Windoohhh! (fã)", R.raw.windoh_fa)
        ));
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
}
