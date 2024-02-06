package com.example.techfort.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.techfort.Adapter.WeeklyQuizAdapter;
import com.example.techfort.Model.WeeklyQuizModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityWeeklyQuizBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WeeklyQuizActivity extends AppCompatActivity {

    //    public static int selected_cat_index = 0;
    ActivityWeeklyQuizBinding binding;
    FirebaseFirestore database;
//    private RecyclerView recyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_quiz);


        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(WeeklyQuizActivity.this,R.color.color_white));

        binding = ActivityWeeklyQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        ArrayList<WeeklyQuizModel> quizCategories = new ArrayList<>();
        WeeklyQuizAdapter adapter = new WeeklyQuizAdapter(this, quizCategories);
//        recyclerView = (RecyclerView) findViewById(R.id.weekly_quiz_recview);
//        // use a linear layout manager
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

        database.collection("weeklyquiz")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        quizCategories.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            WeeklyQuizModel model = snapshot.toObject((WeeklyQuizModel.class));
                            model.setWeeklyQuizCategoryId(snapshot.getId());
                            quizCategories.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


//        binding.weeklyQuizRecview.setLayoutManager(new LinearLayoutManager(this));
        binding.weeklyQuizRecview.setLayoutManager(new GridLayoutManager(this, 1));
        binding.weeklyQuizRecview.setAdapter(adapter);
    }
}