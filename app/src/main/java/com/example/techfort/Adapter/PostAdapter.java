package com.example.techfort.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.techfort.Activity.CommentActivity;
import com.example.techfort.Activity.DiscussActivity;
import com.example.techfort.Activity.VideoActivity;
import com.example.techfort.Activity.VideoName;
import com.example.techfort.Model.Post;
import com.example.techfort.Model.User;
import com.example.techfort.Model.VideoModel;
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

    public List<VideoModel> list;
    public List<User> user_list;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public PostAdapter(Context context, List<VideoModel> list) {
        this.list = list;
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

//        holder.setIsRecyclable(false);

        VideoModel model = list.get(position);
        holder.textView.setText(model.getName());

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("uri",model.getUrl());
                context.startActivity(intent);

            }
        });



    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        VideoView view;
        ImageView img;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.videoView);
            textView=itemView.findViewById(R.id.Videoname);
            img=itemView.findViewById(R.id.thumbnail);


        }

    }
}
