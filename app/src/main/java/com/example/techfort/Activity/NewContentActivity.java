package com.example.techfort.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.techfort.Model.ContentModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityNewContentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewContentActivity extends AppCompatActivity {

    ActivityNewContentBinding binding;
    DatabaseReference database;

    Toolbar newContentBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        newContentBar = (Toolbar) findViewById(R.id.new_content_toolbar);
        setSupportActionBar(newContentBar);
        getSupportActionBar().setTitle("Add New Content");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance().getReference();
        String category = getIntent().getStringExtra("category");
        String topic = getIntent().getStringExtra("topicName");




        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String heading = binding.Heading.getText().toString();
                String body = binding.body.getText().toString();

                if(heading.length()>0 && body.length()>0){

                    database.child("topics").child(category).child(topic).child("content").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = String.valueOf(snapshot.getChildrenCount()+1);
                            database.child("topics").child(category).child(topic)
                                    .child("content").push()
                                    .setValue(new ContentModel(id,heading,body));
                            Toast.makeText(NewContentActivity.this,"Information Added",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("mytag", "onCancelled: "+error);
                        }
                    });


                }
            }
        });

    }
}