package com.fishmethis.aman.chatter2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
    private SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("com.fishmethis.aman.chatter2", MODE_PRIVATE);
        Button busr = (Button)findViewById(R.id.button);
        busr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SetUsr.class);
                startActivity(i);
            }
        });
        Button bsel = (Button)findViewById(R.id.button2);
        bsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SelectPartner.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            Intent i = new Intent(getApplicationContext(),SetUsrF.class);
            startActivity(i);
            finish();
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

}
