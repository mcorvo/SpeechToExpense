package com.example.roomwordssample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private WordViewModel mWordViewModel;
    private ExpensesViewModel mExpensesViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final Integer RecordAudioRequestCode = 1;

    private SpeechRecognizer speechRecognizer;
    private ArrayList<String> data;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff());
        final ExpensesListAdapter expAdapter = new ExpensesListAdapter(new ExpensesListAdapter.WordDiff());

        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(expAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
         * Check permission to record audio
         */
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        RecognitionListener recoListener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Toast.makeText(getApplicationContext(), "Listening", Toast.LENGTH_LONG).show();
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
            public void onResults(Bundle results) {
                data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                SpeechParser p = new SpeechParser(data);
                String res = p.parse(mExpensesViewModel);
                mWordViewModel.insert(new Word(res));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        };

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT");

        speechRecognizer.setRecognitionListener(recoListener);

        ViewModelProvider provider = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mWordViewModel = provider.get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(this, words -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(words);
        });

        mExpensesViewModel = provider.get(ExpensesViewModel.class);
        mExpensesViewModel.getAllExpenses().observe(this, expenses -> {
            // Update the cached copy of the words in the adapter.
            expAdapter.submitList(expenses);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });

        FloatingActionButton reg = findViewById(R.id.reg);
        reg.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                speechRecognizer.stopListening();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                speechRecognizer.startListening(speechRecognizerIntent);
            }
            return false;

        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermission() {
        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.RECORD_AUDIO },
                RecordAudioRequestCode);
//            requestPermissionLauncher.launch(
//                    Manifest.permission.RECORD_AUDIO);
    }

}