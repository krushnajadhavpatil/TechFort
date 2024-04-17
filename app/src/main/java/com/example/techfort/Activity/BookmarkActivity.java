package com.example.techfort.Activity;

import android.os.Bundle;
import android.util.Log;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<BookmarkModel> bookmarkList;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    DatabaseReference database ;

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
        database = FirebaseDatabase.getInstance().getReference();

        BookmarkAdapter adapter = new BookmarkAdapter(bookmarkList, this);

        //Toolbar
        Toolbar setupToolbar = findViewById(R.id.bookmarkToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Bookmarks");


        //Status Bar Color
        getWindow().setStatusBarColor(ContextCompat.getColor(BookmarkActivity.this,R.color.color_white));

        if (firebaseAuth.getCurrentUser() != null) {

            database.child("bookmark").child(current_user_id).orderByChild("topicName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    bookmarkList.clear();
                    for(DataSnapshot data:snapshot.getChildren()){
                        Log.d("mytag", "Bookark: "+data);
                        BookmarkModel model = data.getValue(BookmarkModel.class);
                        bookmarkList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);
    }
}