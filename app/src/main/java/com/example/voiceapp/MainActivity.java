package com.example.voiceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private TextView txv_result;
    List<String> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txv_result = (TextView) findViewById(R.id.txv_result);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.toolbar));

        requestAudioPermissions();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            mySpeechRecognizer.startListening(intent);
            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //do nothing, just let it tick
                }

                public void onFinish() {
                    mySpeechRecognizer.stopListening();
                }   }.start();
        });

        initializeTextToSpeech();
        initializeSpeechRecognizer();
    }

    private void initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {

                    results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    assert results != null;
                    txv_result.setText(results.get(0));
                    processResult(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String command) {
        command = command.toLowerCase();
        if (command.contains("what")) {
            if (command.contains("your name")) {
                speak("My name is Jean.");
            }
            else if (command.contains("time")) {
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("The time now is " + time);
            }
            else if (command.contains("date")|| command.contains("day")) {
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                speak("The date today is " + currentDate);
            }
            else {
                String string = results.toString()
                        .substring(1, 3 * results.size() - 1)
                        .replaceAll(", ", "");
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.setClassName("com.google.android.googlequicksearchbox",
                        "com.google.android.googlequicksearchbox.SearchActivity");
                intent.putExtra("query", string);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    speak("App not found");
                }
            }
        }
        else if(command.contains("button")){
            startActivity(new Intent(this, Buttons.class));
        }
        else if(command.contains("open")){
            if(command.contains("browser") || command.contains("internet") || command.contains("google")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    speak("App not found");
                }
            }
            else if(command.contains("youtube")){
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                assert intent != null;
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    speak("App not found");
                }
            }
            else if(command.contains("asl")){
                Intent intent = getPackageManager().getLaunchIntentForPackage("edmt.dev.androidcustomkeyboard");
                assert intent != null;
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    speak("App not found");
                }
            }
            else if(command.contains("whatsapp") || command.contains("whats app")){
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                assert intent != null;
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    speak("App not found");
                }
            }
            else if(command.contains("camera")){
                PackageManager packman = getPackageManager();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String pack = intent.resolveActivity(packman).getPackageName();
                Intent intent_2 = getPackageManager().getLaunchIntentForPackage( pack);
                assert intent_2 != null;
                if (intent_2.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent_2);
                }
                else {
                    speak("App not found");
                }
            }

            else if (command.contains("search") || command.contains("search for")){
                String string = results.toString()
                        .substring(1, 3 * results.size() - 1)
                        .replaceAll(", ", "");
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.setClassName("com.google.android.googlequicksearchbox",
                        "com.google.android.googlequicksearchbox.SearchActivity");
                intent.putExtra("query", string);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    speak("App not found");
                }
            }
            else if(command.contains("button")){
                startActivity(new Intent(this, Buttons.class));
            }

            else {
                String string = results.toString()
                        .substring(1, 3 * results.size() - 1)
                        .replaceAll(", ", "");
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.setClassName("com.google.android.googlequicksearchbox",
                        "com.google.android.googlequicksearchbox.SearchActivity");
                intent.putExtra("query", string);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else {
                    speak("App not found");
                }
            }
        }
        else if(command.contains("speech to text") || command.contains("stt")){
            Intent intent = new Intent(MainActivity.this, Page.class);
            startActivity( intent);
        }
        else if(command.contains("text to speech") || command.contains("tts")) {
            Intent intent = new Intent(MainActivity.this, Page2.class);
            startActivity(intent);
        }
        else if(command.contains("analysis") || command.contains("analyse")|| command.contains("analyze")) {
            Intent intent = new Intent(MainActivity.this, ImageAnalysis.class);
            startActivity(intent);
        }
        else if(command.contains("map") || (command.contains("map") && command.contains("open")) || command.contains("direction")){
            startActivity(new Intent(this, Map2.class));
        }

        else {
           String string = results.toString()
                    .substring(1, 3 * results.size() - 1)
                    .replaceAll(", ", "");
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.setClassName("com.google.android.googlequicksearchbox",
                    "com.google.android.googlequicksearchbox.SearchActivity");
            intent.putExtra("query", string);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            else {
                speak("App not found");
            }
        }

    }

    private void initializeTextToSpeech(){
        myTTS = new TextToSpeech(this , i -> {
            if (myTTS.getEngines().size() == 0) {
                Toast.makeText(MainActivity.this, "There is no TTS engine on this device", Toast.LENGTH_LONG).show();
                finish();
            } else{
                myTTS.setLanguage(Locale.US);
                speak("Hello! Welcome to One For All. How may I help you?");
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
        if (id == R.id.action_settings) {
                startActivity(new Intent(this, Buttons.class));
        }
        else if (id == R.id.action_home) {
            startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
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
