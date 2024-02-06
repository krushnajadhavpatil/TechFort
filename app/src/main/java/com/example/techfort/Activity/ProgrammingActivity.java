package com.example.techfort.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.techfort.Adapter.ProgrammingAdapter;
import com.example.techfort.Model.ProgrammingModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityProgrammingBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProgrammingActivity extends AppCompatActivity {

    ActivityProgrammingBinding bindings;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programming);
        bindings = ActivityProgrammingBinding.inflate(getLayoutInflater());
        setContentView(bindings.getRoot());


        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(ProgrammingActivity.this,R.color.color_white));

        database = FirebaseFirestore.getInstance();

        ArrayList<ProgrammingModel> prog_categories = new ArrayList<>();
        ProgrammingAdapter adapter = new ProgrammingAdapter(this,prog_categories);

        database.collection("programming").orderBy("progName").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                prog_categories.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    ProgrammingModel model = snapshot.toObject((ProgrammingModel.class));
                    model.setProgId((snapshot.getId()));
                   prog_categories.add(model);
                }
                adapter.notifyDataSetChanged();
            }
        });

        bindings.progCatList.setLayoutManager(new GridLayoutManager(this, 2));
        bindings.progCatList.setAdapter(adapter);
    }
}