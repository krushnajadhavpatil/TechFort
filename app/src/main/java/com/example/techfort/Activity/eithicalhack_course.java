package com.example.techfort.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.techfort.R;

public class eithicalhack_course extends AppCompatActivity {

    CardView ch1,ch2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eithicalhack_course);
        ch1=findViewById(R.id.ethichap1);
        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(eithicalhack_course.this, ethichapter_first.class);
                startActivity(intent);

            }
        });


    }
}