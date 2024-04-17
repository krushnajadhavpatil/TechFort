package com.example.techfort.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.techfort.Model.Question;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityAddQuestionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestionActivity extends AppCompatActivity {

    Toolbar bar;

    ActivityAddQuestionBinding binding;

    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance().getReference();

        bar = (Toolbar) findViewById(R.id.questionToolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setTitle("Add New Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Question q = new Question(
                        binding.question.getText().toString(),
                        binding.op1.getText().toString(),
                        binding.op2.getText().toString(),
                        binding.op3.getText().toString(),
                        binding.op4.getText().toString(),
                        binding.ans.getText().toString()

                );

            database.child("weeklyQuestions").child(binding.questionCategory.getText().toString().toUpperCase()).push().setValue(q)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                    binding.questionCategory.setText("");
                                    binding.question.setText("");
                                    binding.op1.setText("");
                                    binding.op2.setText("");
                                    binding.op3.setText("");
                                    binding.op4.setText("");
                                    binding.ans.setText("");
                        }
                    });


            }
        });

    }
}