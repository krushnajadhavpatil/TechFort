package com.example.techfort.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Adapter.PostAdapter;
import com.example.techfort.Model.Post;
import com.example.techfort.Model.User;
import com.example.techfort.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiscussActivity extends AppCompatActivity {

    private RecyclerView list_view;
    private List<Post> list;
    private List<User> user_list;
    private FloatingActionButton addPostBtn;

    private FirebaseFirestore firebaseFirestore;
    private PostAdapter postAdapter;
    private FirebaseAuth firebaseAuth;
    private Boolean isFirstPageFirstLoad = true;
    private DocumentSnapshot lastVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);

        list = new ArrayList<>();
        user_list = new ArrayList<>();
        list_view = findViewById(R.id.list_view);
        addPostBtn = findViewById(R.id.add_post_btn);
        postAdapter = new PostAdapter(this, list, user_list);

        list_view.setLayoutManager(new LinearLayoutManager(this));
        list_view.setAdapter(postAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Toolbar
        Toolbar setupToolbar = findViewById(R.id.discussToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Discuss");


        if (firebaseAuth.getCurrentUser() != null) {

            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent postIntent = new Intent(DiscussActivity.this, NewPostActivity.class);
                    startActivity(postIntent);
                    finish();

                }
            });


            list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {
                        loadMorePost();
                    }
                }
            });

            Query firstQuery = firebaseFirestore.collection("Posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING).limit(4);

            firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            if (!documentSnapshots.isEmpty()) {

                                if (isFirstPageFirstLoad) {

                                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                                    list.clear();
                                    user_list.clear();
                                }

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {

                                        String postId = doc.getDocument().getId();

                                        Post post = doc.getDocument().toObject(Post.class).withId(postId);

                                        String postUserId = doc.getDocument().getString("user_id");
                                        firebaseFirestore.collection("Users")
                                                .document(postUserId)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            User user = task.getResult().toObject(User.class);

                                                            if (isFirstPageFirstLoad) {
                                                                list.add(post);
                                                                user_list.add(user);
                                                                postAdapter.notifyItemInserted(list.size());
                                                            } else {
                                                                list.add(0, post);
                                                                user_list.add(0, user);
                                                                postAdapter.notifyItemInserted(0);
                                                            }
                                                            postAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                });

                                    }
                                }
                                isFirstPageFirstLoad = false;
                            }
                        }
                    });
        }
    }

    public void loadMorePost() {

        Query nextQuery = firebaseFirestore.collection("Posts")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(5);

        nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String postId = doc.getDocument().getId();

                            Post post = doc.getDocument().toObject(Post.class).withId(postId);
                            String postUserId = doc.getDocument().getString("user_id");
                            firebaseFirestore.collection("Users")
                                    .document(postUserId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                User user = task.getResult().toObject(User.class);
                                                list.add(post);
                                                user_list.add(user);
                                                postAdapter.notifyItemInserted(list.size());
                                                postAdapter.notifyDataSetChanged();

                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });
    }
}