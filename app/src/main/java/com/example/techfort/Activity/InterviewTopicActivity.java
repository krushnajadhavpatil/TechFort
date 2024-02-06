package com.example.techfort.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.techfort.Adapter.InterviewTopicAdapter;
import com.example.techfort.Model.InterviewTopicModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityInterviewTopicBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InterviewTopicActivity extends AppCompatActivity {

    ActivityInterviewTopicBinding binding;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_topic);

        binding = ActivityInterviewTopicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();
        ArrayList<InterviewTopicModel> topics = new ArrayList<>();

        InterviewTopicAdapter adapter = new InterviewTopicAdapter(this, topics);
        final String catId = getIntent().getStringExtra("catId");


        database.collection("Categories")
                .document(catId)
                .collection("topics")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        topics.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            InterviewTopicModel model = snapshot.toObject((InterviewTopicModel.class));
                            model.setTopicId(snapshot.getId());
                            topics.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        Glide.with(this).load(getIntent().getStringExtra("categoryImage")).into(binding.categoryImage);
        binding.categoryName.setText(getIntent().getStringExtra("categoryName"));
        binding.topicRecview.setLayoutManager(new GridLayoutManager(this, 1));
        binding.topicRecview.setAdapter(adapter);
    }
}