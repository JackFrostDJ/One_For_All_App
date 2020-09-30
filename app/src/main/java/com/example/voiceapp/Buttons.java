package com.example.voiceapp;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Buttons extends AppCompatActivity {

    Button btn_tts;
    Button btn_stt;
    Button btn_maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        btn_tts = (Button) findViewById(R.id.btn_tts);
        btn_stt = (Button) findViewById(R.id.btn_stt);
        btn_maps = (Button) findViewById(R.id.btn_maps);

        btn_tts.setOnClickListener(v -> {
            Intent intent = new Intent(Buttons.this, Page2.class);
            startActivity(intent);
        });

        btn_stt.setOnClickListener(v -> {
            Intent intent = new Intent(Buttons.this, Page.class);
            startActivity(intent);
        });

        btn_maps.setOnClickListener(v -> {
            startActivity(new Intent(this, Map2.class));
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            startActivity(new Intent(this, MainActivity.class));
        }
        else if (id == R.id.action_settings) {
            startActivity(new Intent(this, Buttons.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
