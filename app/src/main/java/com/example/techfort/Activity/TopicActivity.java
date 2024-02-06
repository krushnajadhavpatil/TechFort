package com.example.techfort.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.techfort.Adapter.TopicAdapter;
import com.example.techfort.Model.TopicModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityTopicBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {

    ActivityTopicBinding binding;
    FirebaseFirestore database;
//    public final String TAG = TopicActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        binding = ActivityTopicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();
        ArrayList<TopicModel> topics = new ArrayList<>();

        TopicAdapter adapter = new TopicAdapter(this, topics);
        final String catId = getIntent().getStringExtra("catId");
//        String catId = CategoryAdapter.getCatId();
//        Log.i(TAG, "Cat " + catId);


        database.collection("Categories" )
                .document(catId)
                .collection("topics")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                topics.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    TopicModel model = snapshot.toObject((TopicModel.class));
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