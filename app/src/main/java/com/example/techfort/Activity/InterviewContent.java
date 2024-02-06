package com.example.techfort.Activity;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfort.R;
import com.example.techfort.databinding.ActivityInterviewContentBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InterviewContent extends AppCompatActivity {


    ActivityInterviewContentBinding binding;
    FirebaseFirestore database;
    private PDFView pdfView;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_content);

        String   url = getIntent().getStringExtra("topicUrl");
        new PdfDownload().execute(url);

        setContentView(R.layout.activity_content);
        binding = ActivityInterviewContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        binding.topicName.setText(getIntent().getStringExtra("topicName"));

    }
    private class PdfDownload extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            binding.pdfView.fromStream(inputStream).load();
        }
    }
}