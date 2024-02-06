package com.example.techfort.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.techfort.Activity.CommentActivity;
import com.example.techfort.Model.Post;
import com.example.techfort.Model.User;
import com.example.techfort.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<com.example.techfort.Adapter.PostAdapter.ViewHolder> {

    public List<Post> list;
    public List<User> user_list;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public PostAdapter(Context context, List<Post> list, List<User> user_list) {
        this.list = list;
        this.user_list = user_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        Post model = list.get(position);
        holder.descView.setText(model.getDescription());
        holder.questionView.setText(model.getQuestion());
        holder.tagView.setText(model.getTag());

        String postId = list.get(position).PostId;
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        String post_user_id = list.get(position).getUser_id();

        //Delete Post
        if (post_user_id.equals(currentUserId)) {

            holder.postDeleteBtn.setEnabled(true);
            holder.postDeleteBtn.setVisibility(View.VISIBLE);

        }

        //User Data will be update here

        String userName = user_list.get(position).getName();
        String userImage = user_list.get(position).getImage();

        holder.setUserData(userName, userImage);


        //Getting timestamp
        SimpleDateFormat spf = new SimpleDateFormat("MMM dd, yyyy");
        String date = spf.format(list.get(position).getTimestamp());
        holder.setTime(date);

        //Get Likes Count
        firebaseFirestore.collection("Posts").document(postId).collection("Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    int count = documentSnapshots.size();

                    holder.updateLikesCount(count);
                } else {
                    holder.updateLikesCount(0);
                }

            }
        });

        //Get Comment Count
        firebaseFirestore.collection("Posts").document(postId).collection("Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    int count = documentSnapshots.size();

                    holder.updateCommentCount(count);
                } else {
                    holder.updateCommentCount(0);
                }

            }
        });

        //Change Color of Like Button
        firebaseFirestore.collection("Posts").document(postId).collection("Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot.exists()) {
                    holder.postLikeBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.upvoteaccent));
                } else {
                    holder.postLikeBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.upvote));
                }

            }
        });
        //Likes feature
        holder.postLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts").document(postId).collection("Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("Posts").document(postId).collection("Likes").document(currentUserId).set(likesMap);
                        } else {
                            firebaseFirestore.collection("Posts").document(postId).collection("Likes").document(currentUserId).delete();
                        }
                    }
                });
            }
        });

        //Comment Page Intent
        holder.postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                context.startActivity(new Intent(context, CommentActivity.class));
                Intent commentIntent = new Intent(context, CommentActivity.class);
                commentIntent.putExtra("post_id", postId);
                context.startActivity(commentIntent);

            }
        });

        //Delete Post
        holder.postDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts").document(postId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        list.remove(position);
                        user_list.remove(position);

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView descView;
        private final TextView questionView;
        private final TextView tagView;
        private TextView postDate;
        private TextView postUserName;
        private TextView postLikeCount;
        private TextView postCommentCount;

        private CircleImageView postUserImage;
        private final ImageView postLikeBtn;
        private final ImageView postCommentBtn;
        private final Button postDeleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            descView = itemView.findViewById(R.id.desc);
            questionView = itemView.findViewById(R.id.question);
            tagView = itemView.findViewById(R.id.tag);
            postLikeBtn = itemView.findViewById(R.id.post_like_btn);
            postCommentBtn = itemView.findViewById(R.id.post_comment_icon);
            postDeleteBtn = itemView.findViewById(R.id.post_delete_btn);

        }

        public void setTime(String date) {

            postDate = itemView.findViewById(R.id.date);
            postDate.setText(date);

        }

        public void setUserData(String name, String image) {
            postUserName = itemView.findViewById(R.id.user_name);
            postUserImage = itemView.findViewById(R.id.user_image);

            postUserName.setText(name);
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.default_image);
            Glide.with(context).applyDefaultRequestOptions(placeholderRequest).load(image).into(postUserImage);

        }

        public void updateLikesCount(int count) {
            postLikeCount = itemView.findViewById(R.id.post_like_count);
            postLikeCount.setText("+" + count + " Upvote");
        }

        public void updateCommentCount(int count) {
            postCommentCount = itemView.findViewById(R.id.post_comment_count);
            postCommentCount.setText("+" + count + " Comments");
        }
    }
}
