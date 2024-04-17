package com.example.techfort.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.techfort.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {
    DatabaseReference db;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        db= FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        EditText edt = findViewById(R.id.feedback);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = edt.getText().toString();
                if(feedback.length()>0) {
                    Toast.makeText(getApplicationContext(), "Feedback submitted",Toast.LENGTH_SHORT).show();
                    Map<Object,Object> map = new HashMap<>();
                    map.put("User",user.getEmail());
                    map.put("message",feedback);
                    db.child("feedback").push().setValue(map);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Enter Valid Feedback ",Toast.LENGTH_SHORT).show();

            }
        });
    }
}