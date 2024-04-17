package com.example.techfort.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Activity.Content;
import com.example.techfort.Model.ContentModel;
import com.example.techfort.R;
import com.example.techfort.Model.TopicModel;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    Context context;
    ArrayList<ContentModel> content;
//    private static String topicId;

    //Sending cat id without intent
//    public static String getTopicId() {
//        return topicId;
//    }



    public ContentAdapter(Context context, ArrayList<ContentModel> content) {
        this.context = context;
        this.content = content;
        Log.d("mytag", "ContentAdapter: "+content.size());
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_layout, null);
        return new ContentAdapter.ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        ContentModel model = content.get(position);
        holder.textViewHead.setText(model.getHeading());
        holder.textViewData.setText(model.getInfo());


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                topicId = model.getTopicId();
//                Intent intent = new Intent(context, Content.class);
//                intent.putExtra("topicId",model.getTopicId());
//                intent.putExtra("topicName",model.getTopicName());
//                intent.putExtra("topicUrl",model.getTopicUrl());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {

        return content.size();
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewHead,textViewData;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHead = itemView.findViewById(R.id.topic_text);
            textViewData = itemView.findViewById(R.id.topic_data);

        }
    }

}
