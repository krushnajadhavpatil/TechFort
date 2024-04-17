package com.example.techfort.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Activity.BookmarkActivity;
import com.example.techfort.Activity.Content;
import com.example.techfort.Model.BookmarkModel;
import com.example.techfort.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    public List<BookmarkModel> bookmarkList;
    Context context;
    String uid = FirebaseAuth.getInstance().getUid();
    DatabaseReference database= FirebaseDatabase.getInstance().getReference();

    public BookmarkAdapter(List<BookmarkModel> bookmarkList, Context context) {
        this.bookmarkList = bookmarkList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
        context = parent.getContext();

        return new BookmarkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {

        BookmarkModel model = bookmarkList.get(position);
        holder.textView.setText(model.getTopicName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Content.class);
                intent.putExtra("topicId",model.getTopicId());
                intent.putExtra("topicName",model.getTopicName());
                intent.putExtra("user_id",model.getUser_id());
                intent.putExtra("category",model.getCategory());
                Log.d("mytag", "onClick: "+model.getCategory());
                context.startActivity(intent);
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("mytagxyz", "onClick: bookmark ");
                database.child("bookmark").child(uid).orderByChild("topicName").equalTo(model.getTopicName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data: snapshot.getChildren()) {
                            Log.d("mytagxyz", "onDataChange: "+data.getKey());
                            database.child("bookmark").child(uid).child(data.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error != null) {
                                        Log.e("mytagxyz", "Error removing bookmark: " + error.getMessage());
                                        Toast.makeText(context, "Error removing bookmark: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d("mytagxyz", "Bookmark removed successfully");
                                        Toast.makeText(context, "Bookmark removed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton imageButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bookmark_topic);
            imageButton = itemView.findViewById(R.id.removeBookmark);

        }
    }
}
