package com.example.techfort.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.techfort.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {

    private Toolbar newPostToolbar;
    private ProgressBar newPostProgress;

    private EditText newPostDesc;
    private EditText newPostQuestion;
    private EditText newPostTag;
    private Button newPostBtn;
    private long countPost = 0;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        newPostToolbar = (Toolbar) findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add New Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newPostQuestion = findViewById(R.id.add_question);
        newPostDesc = findViewById(R.id.add_description);
        newPostTag = findViewById(R.id.add_tag);
        newPostBtn = findViewById(R.id.post_btn);
        newPostProgress = findViewById(R.id.new_post_progress);

        //Get count of documents in database
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshots.isEmpty()) {
                    countPost = documentSnapshots.size();
                } else {
                    countPost = 0;
                }
            }
        });


        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String question = newPostQuestion.getText().toString();
                String desc = newPostDesc.getText().toString();
                String tag = newPostTag.getText().toString();

                if (!TextUtils.isEmpty(question) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(tag)) {

                    newPostProgress.setVisibility(View.VISIBLE);

                    Map<String, Object> postMap = new HashMap<>();
                    postMap.put("question", question);
                    postMap.put("description", desc);
                    postMap.put("tag", tag);
                    postMap.put("user_id", current_user_id);
                    postMap.put("timestamp", System.currentTimeMillis());
                    postMap.put("count",countPost);

                    firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(NewPostActivity.this, "Post was Added", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(NewPostActivity.this, DiscussActivity.class);
                                startActivity(mainIntent);
                                finish();

                            } else {
                                String e = task.getException().getMessage();
                                Toast.makeText(NewPostActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();
                            }
                            newPostProgress.setVisibility(View.INVISIBLE);
                        }
                    });

                }

            }
        });

    }
}