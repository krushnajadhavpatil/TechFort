package com.example.techfort.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techfort.databinding.ActivityTopicQuizResultBinding;

public class TopicQuizResult extends AppCompatActivity {

    ActivityTopicQuizResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityTopicQuizResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total", 0);

        binding.score.setText(String.format("%d/%d", correctAnswers, totalQuestions));

        binding.restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopicQuizResult.this, MainActivity.class));
                finish();
            }
        });
    }
}