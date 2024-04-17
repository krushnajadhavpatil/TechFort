package com.example.techfort.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.techfort.Model.VideoModel;
import com.example.techfort.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VideoName extends AppCompatActivity {

    Toolbar bar;
    EditText text;
    private static final int REQUEST_PICK_VIDEO = 1;

    StorageReference storage;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_name);
        bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setTitle("Video");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storage = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        text = findViewById(R.id.video_name);

        Button btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(VideoName.this)
                if(text.getText().toString().length()>0){

                    pickVideo();
                }

            }
        });
    }

    private void pickVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_VIDEO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            Uri selectedVideoUri = data.getData();
            Log.d("mytagg", "onActivityResult: "+selectedVideoUri);
            // Handle the selected video URI here, e.g., display it in a VideoView or upload it

            storage = FirebaseStorage.getInstance().getReference();
            StorageReference videoRef = storage.child("videos/ videofile"+Math.random());

            videoRef.putFile(selectedVideoUri)

                    .addOnSuccessListener(taskSnapshot -> {
                        // Video uploaded successfully
                        // Get the download URL
                        videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String videoUrl = uri.toString();
                            // Now you can save the video URL to Firestore

                            databaseReference.child("videos").push().setValue(new VideoModel(videoUrl,String.valueOf(System.currentTimeMillis()),text.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Intent postIntent = new Intent(VideoName.this, DiscussActivity.class);
                                    startActivity(postIntent);
                                    finish();
                                }
                            });
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle unsuccessful uploads
                        Log.e("mytag", "Error uploading video: " + e.getMessage());
                    });


        }
    }

}