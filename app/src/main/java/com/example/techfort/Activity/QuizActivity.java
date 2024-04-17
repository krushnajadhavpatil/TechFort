package com.example.techfort.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.techfort.Api.AdminUid;
import com.example.techfort.Model.Question;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;


public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;

    ArrayList<Question> questions;
    int index = 0;
    Question question;
    CountDownTimer timer;
//    FirebaseFirestore database;
    DatabaseReference database;
    int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(QuizActivity.this,R.color.color_white));

        questions = new ArrayList<>();
//        database = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance().getReference();


        String category = getIntent().getStringExtra("category");

        database.child("weeklyQuestions").child(category).orderByChild("weeklyQuizCategoryId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data:snapshot.getChildren()){
                    Question q = data.getValue(Question.class);
                    questions.add(q);
                }
                setNextQuestion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        resetTimer();
    }

    void resetTimer() {
        timer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished < 10000)
                    binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }
            @Override
            public void onFinish(){
                setNextQuestion();
            }
        };
        timer.start();
    }

    void showAnswer() {
        if(question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    void setNextQuestion() {
        if(timer != null)
            timer.cancel();

        timer.start();
        if(index < questions.size()) {
            binding.questionCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        }

    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())) {
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNextQuestion();
            }
        }, 2000);
    }

    void reset() {
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if(timer!=null)
                    timer.cancel();
                TextView selected = (TextView) view;
                binding.option1.setEnabled(false);
                binding.option2.setEnabled(false);
                binding.option3.setEnabled(false);
                binding.option4.setEnabled(false);


                checkAnswer(selected);

                break;
            case R.id.nextBtn:
                reset();
                index++;
                binding.option1.setEnabled(true);
                binding.option2.setEnabled(true);
                binding.option3.setEnabled(true);
                binding.option4.setEnabled(true);
                if(index < questions.size()) {
                    setNextQuestion();
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    //Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}