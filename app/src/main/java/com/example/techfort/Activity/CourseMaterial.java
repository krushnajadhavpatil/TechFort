package com.example.techfort.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Adapter.CourseMaterialAdapter;
import com.example.techfort.Model.CourseMaterialModel;
import com.example.techfort.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseMaterial extends AppCompatActivity {

    private RecyclerView ebookRecyclerView;
    private DatabaseReference reference;
    private List<CourseMaterialModel> list;
    private CourseMaterialAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_material);

        ebookRecyclerView = findViewById(R.id.ebookRecycler);
        reference = FirebaseDatabase.getInstance().getReference().child("pdf");

        getData();

        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(CourseMaterial.this,R.color.color_white));
        //Toolbar
        Toolbar setupToolbar = findViewById(R.id.coursematerialToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Cheetsheats");

    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CourseMaterialModel data = dataSnapshot.getValue(CourseMaterialModel.class);
                    list.add(data);
                }
                adapter = new CourseMaterialAdapter(CourseMaterial.this, list);
                ebookRecyclerView.setLayoutManager(new LinearLayoutManager(CourseMaterial.this));
                ebookRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CourseMaterial.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}