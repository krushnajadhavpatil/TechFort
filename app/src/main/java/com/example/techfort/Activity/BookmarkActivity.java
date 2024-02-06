package com.example.techfort.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Adapter.BookmarkAdapter;
import com.example.techfort.Model.BookmarkModel;
import com.example.techfort.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<BookmarkModel> bookmarkList;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        recyclerView = findViewById(R.id.bookmark_recview);
        bookmarkList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        BookmarkAdapter adapter = new BookmarkAdapter(bookmarkList, this);

        //Toolbar
        Toolbar setupToolbar = findViewById(R.id.bookmarkToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Bookmarks");


        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(BookmarkActivity.this,R.color.color_white));

        if (firebaseAuth.getCurrentUser() != null) {
//            firebaseFirestore.collection("Bookmark")
//                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                            bookmarkList.clear();
//                            for (DocumentSnapshot snapshot : value.getDocuments()) {
//                                BookmarkModel model = snapshot.toObject((BookmarkModel.class));
//                                model.setTopicId(snapshot.getId());
//                                bookmarkList.add(model);
//                            }
//                            adapter.notifyDataSetChanged();
//                        }
//                    });
            firebaseFirestore.collection("Bookmark").addSnapshotListener(BookmarkActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String bookmarkId = doc.getDocument().getId();
                            BookmarkModel model = doc.getDocument().toObject(BookmarkModel.class).withId(bookmarkId);
                            String bookmarkUserId = doc.getDocument().getString("user_id");
                            if (current_user_id.equals(bookmarkUserId)) {
                                firebaseFirestore.collection("Bookmark").document(bookmarkUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
//                                        BookmarkModel model = task.getResult().toObject(BookmarkModel.class);
                                            bookmarkList.add(model);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }

                                });
                            }
                        }
                    }
                }
            });
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);
    }
}