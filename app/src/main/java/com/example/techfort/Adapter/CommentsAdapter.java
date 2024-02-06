package com.example.techfort.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.techfort.Model.Comments;
import com.example.techfort.Model.User;
import com.example.techfort.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<com.example.techfort.Adapter.CommentsAdapter.ViewHolder> {

    public List<Comments> commentslist;
    public List<User> user_list;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public CommentsAdapter(List<Comments> commentslist, Context context, List<User> user_list) {
        this.commentslist = commentslist;
        this.user_list = user_list;
        this.context = context;
    }

    @NonNull
    @Override
    public com.example.techfort.Adapter.CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        return new com.example.techfort.Adapter.CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.techfort.Adapter.CommentsAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        Comments model = commentslist.get(position);
        holder.comment_message.setText(model.getMessage());

        String postId = commentslist.get(position).PostId;
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        String post_user_id = commentslist.get(position).getUser_id();

        //User Data will be update here

        String userName = user_list.get(position).getName();
        String userImage = user_list.get(position).getImage();

        holder.setUserData(userName, userImage);

    }

    @Override
    public int getItemCount() {
        return commentslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView comment_message;
        private CircleImageView commentUserImage;
        private TextView commentUserName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_message = itemView.findViewById(R.id.comment_message);

        }
        public void setUserData(String name, String image) {
            commentUserName = itemView.findViewById(R.id.comment_username);
            commentUserImage = itemView.findViewById(R.id.comment_image);

            commentUserName.setText(name);
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.default_image);
            Glide.with(context).applyDefaultRequestOptions(placeholderRequest).load(image).into(commentUserImage);

        }
    }
}
