package com.example.techfort.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.techfort.Adapter.WebDevTopicAdapter;
import com.example.techfort.Model.WebDevTopicModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityWebDevTopicBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WebDevTopicActivity extends AppCompatActivity {

    ActivityWebDevTopicBinding binding;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_dev_topic);

        binding = ActivityWebDevTopicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(WebDevTopicActivity.this,R.color.color_white));

        database = FirebaseFirestore.getInstance();
        ArrayList<WebDevTopicModel> topics = new ArrayList<>();

        WebDevTopicAdapter adapter = new WebDevTopicAdapter(this, topics);
        final String catId = getIntent().getStringExtra("webId");

        database.collection("web_dev")
                .document(catId)
                .collection("topics")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        topics.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            WebDevTopicModel model = snapshot.toObject((WebDevTopicModel.class));
//                    model.setWeeklyQuizCategoryId(snapshot.getId());
                            topics.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        Glide.with(this).load(getIntent().getStringExtra("webImage")).into(binding.categoryImage);
        binding.categoryName.setText(getIntent().getStringExtra("webName"));
        binding.topicRecview.setLayoutManager(new GridLayoutManager(this, 1));
        binding.topicRecview.setAdapter(adapter);
    }
}