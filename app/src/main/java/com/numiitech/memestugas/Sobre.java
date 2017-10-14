package com.numiitech.memestugas;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.numiitech.memestugas.R;

public class Sobre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        TextView version = (TextView) findViewById(R.id.sobre_version);
        try {
            PackageInfo pi = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String verStr = pi.versionName;
            version.setText(getString(R.string.version) + ": " + verStr);
        } catch (PackageManager.NameNotFoundException e) {
            version.setText(getString(R.string.version) + ": " + R.string.exception_error);
        }
    }
}
