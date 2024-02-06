package com.example.techfort.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techfort.Adapter.CategoryAdapter;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityContentBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Content extends AppCompatActivity {

    public final String TAG = Content.class.getSimpleName();
    String url;
    String topicId;
    String topicName;
    String topicUrl;
    String catId;
    ActivityContentBinding binding;
    FirebaseFirestore database;
    FirebaseAuth firebaseAuth;
    String current_user_id;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra("topicUrl");
        new PdfDownload().execute(url);

        setContentView(R.layout.activity_content);
        binding = ActivityContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        topicId = getIntent().getStringExtra("topicId");
        topicName = getIntent().getStringExtra("topicName");
        topicUrl = getIntent().getStringExtra("topicUrl");

        current_user_id = firebaseAuth.getCurrentUser().getUid();
        catId = CategoryAdapter.getCatId();


        //Adding Bookmark
        binding.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, " CatId  " + catId);
                String topicName = getIntent().getStringExtra("topicName");
                Map<String, Object> map = new HashMap<>();

                map.put("topicName", topicName);
                map.put("topicId", topicId);
                map.put("topicUrl", topicUrl);
                map.put("user_id", current_user_id);

                database.collection("Bookmark").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Content.this, "Bookmark Added", Toast.LENGTH_SHORT).show();
                        } else {
                            String e = task.getException().getMessage();
                            Toast.makeText(Content.this, "Error" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //Change Color of Bookmark Button
                database.collection("Bookmark").document(current_user_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot.exists()) {
//                            binding.bookmark.setBackgroundResource(R.drawable.turned_in_not);
                            binding.bookmark.setImageDrawable(getResources().getDrawable(R.drawable.turned_in_not));
                        } else {
//                            binding.bookmark.setBackgroundResource(R.drawable.turned);
                            binding.bookmark.setImageDrawable(getResources().getDrawable(R.drawable.turned));
                        }
                    }
                });
            }
        });

        //        Calling the quiz
        binding.quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Content.this, WeeklyQuizQuestionActivity.class);
                intent.putExtra("catId", catId);
                startActivity(intent);
//                startActivity(new Intent(getApplicationContext(), WeeklyQuizQuestionActivity.class));

            }
        });
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