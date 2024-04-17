package com.example.techfort.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.techfort.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewTopicActivity extends AppCompatActivity {


    DatabaseReference database ;
    Toolbar newTopicToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic);
        newTopicToolbar = (Toolbar) findViewById(R.id.new_topic_toolbar);
        setSupportActionBar(newTopicToolbar);
        getSupportActionBar().setTitle("Add New Topics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        EditText editText= findViewById(R.id.add_topic);

        Button btn = findViewById(R.id.btn);
        database = FirebaseDatabase.getInstance().getReference();
        String path =getIntent().getStringExtra("path");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = editText.getText().toString();
                if (text.length() > 0) {
                    DatabaseReference d = database.child("topics").child(path);
                    d.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long count = snapshot.getChildrenCount();

                            // Database interaction moved inside onDataChange
                            Map<String, String> map = new HashMap<>();
                            map.put("No", String.valueOf(count+1));
                            database.child("topics").child(path).child(text).setValue(map);
                            Toast.makeText(NewTopicActivity.this, "Chapter Added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled if needed
                        }
                    });
                }
            }
        });
    }
}