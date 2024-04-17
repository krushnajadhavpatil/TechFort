package com.example.techfort.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techfort.Adapter.WeeklyQuizAdapter;
import com.example.techfort.Api.AdminUid;
import com.example.techfort.Model.Question;
import com.example.techfort.Model.WeeklyQuizModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityWeeklyQuizBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WeeklyQuizActivity extends AppCompatActivity {

    //    public static int selected_cat_index = 0;
    ActivityWeeklyQuizBinding binding;
    DatabaseReference database;

    ArrayList<Question> questions;
//    private RecyclerView recyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_quiz);
        questions = new ArrayList<>();
        binding = ActivityWeeklyQuizBinding.inflate(getLayoutInflater());

        AdminUid Uid = new AdminUid();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(Uid.adminUid.equals(id)){
            binding.addQ.setVisibility(View.VISIBLE);
        }


        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(WeeklyQuizActivity.this,R.color.color_white));

        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance().getReference();
        ArrayList<WeeklyQuizModel> quizCategories = new ArrayList<>();
        WeeklyQuizAdapter adapter = new WeeklyQuizAdapter(this, quizCategories);

        binding.weeklyQuizRecview.setLayoutManager(new LinearLayoutManager(this));
        binding.weeklyQuizRecview.setAdapter(adapter);

        database.child("weeklyQuestions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizCategories.clear();
                for(DataSnapshot data:snapshot.getChildren()){
                        WeeklyQuizModel q = new WeeklyQuizModel( data.getKey());
                        quizCategories.add(q);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        database.child("")
        binding.addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeeklyQuizActivity.this, AddQuestionActivity.class);
                startActivity(intent);

            }
        });


//        binding.weeklyQuizRecview.setLayoutManager(new LinearLayoutManager(this));
        binding.weeklyQuizRecview.setLayoutManager(new GridLayoutManager(this, 1));
        binding.weeklyQuizRecview.setAdapter(adapter);
    }
}