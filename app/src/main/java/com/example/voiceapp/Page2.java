package com.example.voiceapp;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Page2 extends AppCompatActivity {
    private TextToSpeech myTTS;
    EditText txt;
    Button tts_to_home;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        tts_to_home = findViewById(R.id.buttontts);
        tts_to_home.setOnClickListener(v -> {
            Intent intent = new Intent(Page2.this, Buttons.class);
            startActivity(intent);
        });
        myTTS = new TextToSpeech(this, i -> {
            if (myTTS.getEngines().size() == 0) {
                Toast.makeText(Page2.this, "There is no TTS engine on this device", Toast.LENGTH_LONG).show();
                finish();
            } else {
                myTTS.setLanguage(Locale.US);
            }
        });
        requestAudioPermissions();
        txt = findViewById(R.id.txt);
        FloatingActionButton fab = findViewById(R.id.tts);
        fab.setOnClickListener(view -> {
            String result = txt.getText().toString();
            speak(result);
        });
        initializeTextToSpeech();
    }
    private void initializeTextToSpeech(){
        myTTS = new TextToSpeech(this , i -> {
            if (myTTS.getEngines().size() == 0) {
                Toast.makeText(this, "There is no TTS engine on this device", Toast.LENGTH_LONG).show();
                finish();
            } else{
                myTTS.setLanguage(Locale.US);
                speak("Welcome to the Text to Speech Engine. Type the text and press the button to listen! ");
            }
        });
    }

    public void speak(String message) {
        if (Build.VERSION.SDK_INT >= 23){
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else{
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    private void requestAudioPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)){
                Toast.makeText(this, "Please grant permission to record audio", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_RECORD_AUDIO:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initializeTextToSpeech();
                } else {
                    Toast.makeText(this, "Permission Denied to record audio", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
