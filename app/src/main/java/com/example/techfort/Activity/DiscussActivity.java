package com.example.techfort.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.techfort.Adapter.PostAdapter;
import com.example.techfort.Adapter.PostAdapter;
import com.example.techfort.Api.AdminUid;
import com.example.techfort.Model.Post;
import com.example.techfort.Model.User;
import com.example.techfort.Model.VideoModel;
import com.example.techfort.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscussActivity extends AppCompatActivity {

    private RecyclerView list_view;
    private List<VideoModel> list;
//    private List<User> user_list;
    private FloatingActionButton addPostBtn;

    private FirebaseFirestore firebaseFirestore;

    StorageReference storage;
    PostAdapter postAdapter;


    DatabaseReference databaseReference ;
//    private PostAdapter postAdapter;
    private FirebaseAuth firebaseAuth;
    private Boolean isFirstPageFirstLoad = true;
    private DocumentSnapshot lastVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);

        list = new ArrayList<>();
//        user_list = new ArrayList<>();
        list_view = findViewById(R.id.list_view);
        addPostBtn = findViewById(R.id.add_video_btn);

        postAdapter = new PostAdapter(this, list);

        list_view.setLayoutManager(new LinearLayoutManager(this));
        list_view.setAdapter(postAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Toolbar
        Toolbar setupToolbar = findViewById(R.id.discussToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Videos");
        AdminUid adminUid=new AdminUid();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(adminUid.adminUid.equals(id)){
            addPostBtn.setVisibility(View.VISIBLE);
        }



        if (firebaseAuth.getCurrentUser() != null) {

            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent postIntent = new Intent(DiscussActivity.this, VideoName.class);
                    startActivity(postIntent);
                    finish();

                }
            });




            databaseReference.child("videos").orderByChild("time").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot data:snapshot.getChildren()) {
                        VideoModel model = data.getValue(VideoModel.class);
                        list.add(model);
                    }

                    postAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

}