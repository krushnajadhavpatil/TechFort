package com.example.techfort.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Adapter.CategoryAdapter;
import com.example.techfort.Adapter.ContentAdapter;
import com.example.techfort.Api.AdminUid;
import com.example.techfort.Model.BookmarkModel;
import com.example.techfort.Model.ContentModel;
import com.example.techfort.Model.TopicModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityContentBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Content extends AppCompatActivity {

    public final String TAG = Content.class.getSimpleName();
    String url;
    String topicId;
    String topicName;
    String category;
    String catId;
    ActivityContentBinding binding;
    FirebaseFirestore database;

    DatabaseReference db ;
    FirebaseAuth firebaseAuth;
    String current_user_id;

    ArrayList<ContentModel> content;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra("topicUrl");

        setContentView(R.layout.activity_content);
        binding = ActivityContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        db= FirebaseDatabase.getInstance().getReference();
        content = new ArrayList<>();

        topicId = getIntent().getStringExtra("topicId");
        topicName = getIntent().getStringExtra("topicName");
//        topicUrl = getIntent().getStringExtra("topicUrl");
        category = getIntent().getStringExtra("category");

        Log.d("mytag123", "onCreate: "+topicName+category);

        current_user_id = firebaseAuth.getCurrentUser().getUid();
        catId = CategoryAdapter.getCatId();


        ContentAdapter adapter = new ContentAdapter(this,content);
        binding.contentRecycler.setAdapter(adapter);
        binding.contentRecycler.setLayoutManager(new LinearLayoutManager(this));


        AdminUid adminUid=new AdminUid();

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(adminUid.adminUid.equals(id)){
            binding.addButton.setVisibility(View.VISIBLE);
        }

        db.child("topics").child(category).child(topicName).child("content").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                content.clear();
                for (DataSnapshot data:snapshot.getChildren()) {
                    Log.d("mytag", "onDataChange: "+data.getKey());
                    if(data!=null) {
                        ContentModel content1 = data.getValue(ContentModel.class);
                        content.add(content1);
                    }
                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Content.this,NewContentActivity.class);
                intent.putExtra("category",category);
                intent.putExtra("topicName",topicName);
                startActivity(intent);
            }
        });



        //Adding Bookmark
        binding.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.child("bookmark").child(current_user_id).push().setValue(new BookmarkModel(topicId,topicName,current_user_id,category)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Content.this, "Bookmark Added", Toast.LENGTH_SHORT).show();
                        } else {
                            String e = task.getException().getMessage();
                            Toast.makeText(Content.this, "Error" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//                //Change Color of Bookmark Button

                db.child("bookmark").child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("mytag", "bookmark: "+snapshot.getValue());

                        if (!snapshot.exists()) {

                            binding.bookmark.setBackgroundResource(R.drawable.turned_in_not);
                            binding.bookmark.setImageDrawable(getResources().getDrawable(R.drawable.turned_in_not));

                        } else {

                            binding.bookmark.setBackgroundResource(R.drawable.turned);
                            binding.bookmark.setImageDrawable(getResources().getDrawable(R.drawable.turned));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });



//
//

        //        Calling the quiz
//        binding.quizBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Content.this, WeeklyQuizQuestionActivity.class);
//                intent.putExtra("catId", catId);
//                startActivity(intent);
////                startActivity(new Intent(getApplicationContext(), WeeklyQuizQuestionActivity.class));
//
//            }
//        });
        binding.topicName.setText(getIntent().getStringExtra("topicName"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String,Object> map= new HashMap<>();
        String topicn;
        Map<String, Object> user;
        db.child("bookmark").orderByChild(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (!snapshot.exists()) {
                    binding.bookmark.setBackgroundResource(R.drawable.turned_in_not);
                    binding.bookmark.setImageDrawable(getResources().getDrawable(R.drawable.turned_in_not));

                } else {


                    Map<String, Object> usermap = new HashMap<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        for (DataSnapshot d : data.getChildren()) {
//
//                            Map <String,Object>user = (Map<String,Object>)d.getValue();
////                            String key = d.getKey();
////                            Object val = d.getValue();
////
////                            usermap.put(key, val);
//
////                            Log.d("mytag", "onDataChange: " + usermap);

//                        Map<String, Object> user= (Map<String, Object>) data.getValue();
                            usermap = (Map<String, Object>) d.getValue();
                        }

                        Object name = usermap.get("topicName");

                        Log.d("mytag123", "onDataChange: " + usermap);
                        Log.d("mytag123", "onDataChange: " + name + topicName);
                        if (name.equals(topicName)) {
                            binding.bookmark.setBackgroundResource(R.drawable.turned);
                            binding.bookmark.setImageDrawable(getResources().getDrawable(R.drawable.turned));

                        } else {

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}