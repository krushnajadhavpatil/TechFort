package com.example.techfort.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.techfort.Adapter.ProgTopicAdapter;
import com.example.techfort.Model.ProgTopicModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityProgTopicBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProgTopicActivity extends AppCompatActivity {

    ActivityProgTopicBinding binding;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prog_topic);

        binding = ActivityProgTopicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();
        ArrayList<ProgTopicModel> topics = new ArrayList<>();


        ProgTopicAdapter adapter = new ProgTopicAdapter(this, topics);
        final String progId = getIntent().getStringExtra("progId");

        database.collection("programming")
                .document(progId)
                .collection("topics")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        topics.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            ProgTopicModel model = snapshot.toObject((ProgTopicModel.class));
                            topics.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        Glide.with(this).load(getIntent().getStringExtra("progImage")).into(binding.categoryImage);
        binding.categoryName.setText(getIntent().getStringExtra("progName"));
        binding.topicRecview.setLayoutManager(new GridLayoutManager(this, 1));
        binding.topicRecview.setAdapter(adapter);
    }
}