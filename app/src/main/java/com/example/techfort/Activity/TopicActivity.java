package com.example.techfort.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.techfort.Adapter.TopicAdapter;
import com.example.techfort.Api.AdminUid;
import com.example.techfort.Model.ContentModel;
import com.example.techfort.Model.TopicModel;
import com.example.techfort.R;
import com.example.techfort.databinding.ActivityTopicBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TopicActivity extends AppCompatActivity {

    ActivityTopicBinding binding;
    FirebaseFirestore database;
    FirebaseAuth mAuth;
    DatabaseReference db;
//    public final String TAG = TopicActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        binding = ActivityTopicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        db = FirebaseDatabase.getInstance().getReference();

        String name =getIntent().getStringExtra("categoryName");

        ArrayList<TopicModel> topics = new ArrayList<>();

        TopicAdapter adapter = new TopicAdapter(this, topics);
        adapter.category =name;
        AdminUid adminUid=new AdminUid();

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(adminUid.adminUid.equals(id)){
            binding.addButton.setVisibility(View.VISIBLE);
        }


        final String catId = getIntent().getStringExtra("catId");
//        String catId = CategoryAdapter.getCatId();
//        Log.i(TAG, "Cat " + catId);




        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TopicActivity.this,NewTopicActivity.class);
                intent.putExtra("path",name);
                startActivity(intent);
//                db.child("topics").child(name).

            }
        });
//        database.collection("Categories" )
//                .document(catId)
//                .collection("topics")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                topics.clear();
//                for (DocumentSnapshot snapshot : value.getDocuments()) {
//                    TopicModel model = snapshot.toObject((TopicModel.class));
//                    model.setTopicId(snapshot.getId());
//                    topics.add(model);
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });

//        Map<String,String> info = new HashMap<>();

//            ContentModel c = new ContentModel("2","Defination 2","This is a defination ");
//            db.child("topics").child("Ethical Hacking").child("Introduction").child("content").push().setValue(c);

            //
//
////
//        String[] arr ={"Introduction","Basic Of Networking","Chapter Thirtd ","Chapter Fourth","Chapter Fifth"};
//
//        int i=0;
//        for (String a :arr){
//            i+=1;
//            info.put("No",Integer.toString(1));
//        }
//        db.child("topics").child("Kali Linux").setValue("");
//        db.child("topics").child("Python").setValue("");
//        db.child("topics").child("Cyber Security").setValue("");
//        db.child("topics").child("Information").setValue("");
//        db.child("topics").child("Networking").setValue("");



        db.child("topics").child(name).orderByChild("No").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topics.clear();
                int i =0;
                for (DataSnapshot data:snapshot.getChildren()) {
                    i+=1;
                    TopicModel model = new TopicModel(data.getKey(),Integer.toString(i));
                    topics.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Glide.with(this).load(getIntent().getStringExtra("categoryImage")).into(binding.categoryImage);
        binding.categoryName.setText(name);
        binding.topicRecview.setLayoutManager(new GridLayoutManager(this, 1));
        binding.topicRecview.setAdapter(adapter);
    }
}