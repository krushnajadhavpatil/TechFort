package com.example.techfort.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.techfort.Adapter.WebDevAdapter;
import com.example.techfort.Model.WebDevModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityWebDevBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WebDevActivity extends AppCompatActivity {

    ActivityWebDevBinding bindings;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_dev);
        bindings = ActivityWebDevBinding.inflate(getLayoutInflater());
        setContentView(bindings.getRoot());


        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(WebDevActivity.this,R.color.color_white));

        database = FirebaseFirestore.getInstance();

        ArrayList<WebDevModel> webdev_categories = new ArrayList<>();
        WebDevAdapter adapter = new  WebDevAdapter(this,webdev_categories);

        database.collection("web_dev").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                webdev_categories.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    WebDevModel model = snapshot.toObject((WebDevModel.class));
                    model.setWebId((snapshot.getId()));
                    webdev_categories.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        });
        bindings.febCatList.setLayoutManager(new GridLayoutManager(this, 2));
        bindings.febCatList.setAdapter(adapter);
    }
}