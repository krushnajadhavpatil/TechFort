package com.example.techfort.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Adapter.CommentsAdapter;
import com.example.techfort.Model.Comments;
import com.example.techfort.Model.User;
import com.example.techfort.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private Toolbar commentToolbar;
    private Boolean isFirstPageFirstLoad = true;
    private DocumentSnapshot lastVisible;
    private EditText comment_field;
    private ImageView comment_post_btn;

    private String post_id;
    private String current_user_id;
    private long countComments = 0;

    private RecyclerView comment_list;
    private CommentsAdapter commentsAdapter;
    private List<Comments> commentsList;
    private List<User> user_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentToolbar = findViewById(R.id.comment_toolbar);
        setSupportActionBar(commentToolbar);
        getSupportActionBar().setTitle("Q&A Disscussions");

        comment_field = findViewById(R.id.comment_field);
        comment_post_btn = findViewById(R.id.comment_post_btn);
        comment_list = findViewById(R.id.comment_list);
        user_list = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        post_id = getIntent().getStringExtra("post_id");


        //RecyclerView

        commentsList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentsList, this, user_list);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(commentsAdapter);


        //Get count of documents in database
        firebaseFirestore.collection("Posts").document(post_id).collection("Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshots.isEmpty()) {
                    countComments = documentSnapshots.size();
                } else {
                    countComments = 0;
                }
            }
        });


        if (firebaseAuth.getCurrentUser() != null) {


            comment_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedTop = !comment_list.canScrollVertically(1);

                    if (reachedTop) {
                        loadMorePost();
                    }
                }
            });

            //Getting Comments From Firebase

            Query loadMessageQuery = firebaseFirestore.collection("Posts")
                    .document(post_id)
                    .collection("Comments")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(10);


                   loadMessageQuery.addSnapshotListener(CommentActivity.this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                            if (!documentSnapshots.isEmpty()) {
                                if (isFirstPageFirstLoad) {

                                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                                    commentsList.clear();
                                    user_list.clear();
                                }
                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                    if (doc.getType() == DocumentChange.Type.ADDED) {

                                        String commentId = doc.getDocument().getId();
                                        Comments comments = doc.getDocument().toObject(Comments.class).withId(commentId);

                                        String commentUserId = doc.getDocument().getString("user_id");

                                        firebaseFirestore.collection("Users").document(commentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    User user = task.getResult().toObject(User.class);

                                                    if (isFirstPageFirstLoad) {
                                                        commentsList.add(comments);
                                                        user_list.add(user);
                                                    } else {
                                                        commentsList.add(0, comments);
                                                        user_list.add(0, user);
                                                    }
                                                    commentsAdapter.notifyDataSetChanged();
                                                    comment_list.scrollToPosition(0);
                                                }
                                            }
                                        });
                                    }
                                }
                                isFirstPageFirstLoad = false;
                            }
                        }
                    });

            //Setting Comments
            comment_post_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String comment_message = comment_field.getText().toString();

                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("message", comment_message);
                    commentsMap.put("user_id", current_user_id);
                    commentsMap.put("timestamp", System.currentTimeMillis());
                    commentsMap.put("count", countComments);

                    if (!comment_message.isEmpty()) {
                        firebaseFirestore.collection("Posts").document(post_id).collection("Comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(CommentActivity.this, "Error Posting Answer: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(CommentActivity.this, "Answered", Toast.LENGTH_LONG).show();
                                    comment_field.setText("");
                                }
                            }
                        });
                        comment_list.scrollToPosition(0);
                    }
                }
            });
        }
    }

    public void loadMorePost() {
//        Query nextQuery = firebaseFirestore.collection("Posts")
//                .document(post_id)
//                .collection("Comments")
//                .orderBy("count", Query.Direction.DESCENDING)
//                .startAfter(lastVisible);
        Query nextQuery = firebaseFirestore.collection("Posts")
                .document(post_id)
                .collection("Comments")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastVisible).limit(15);


        nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String commentId = doc.getDocument().getId();

//                            Post post = doc.getDocument().toObject(Post.class).withId(postId);
                            Comments comments = doc.getDocument().toObject(Comments.class).withId(commentId);
                            String commentUserId = doc.getDocument().getString("user_id");
                            firebaseFirestore.collection("Users").document(commentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        User user = task.getResult().toObject(User.class);

                                        commentsList.add(comments);
                                        user_list.add(user);
                                        commentsAdapter.notifyDataSetChanged();

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