package com.example.techfort.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Activity.Content;
import com.example.techfort.R;
import com.example.techfort.Model.TopicModel;

import java.util.ArrayList;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    Context context;
    ArrayList<TopicModel> topicModel;
    private static String topicId;

    //Sending cat id without intent
    public static String getTopicId() {
        return topicId;
    }



    public TopicAdapter(Context context, ArrayList<TopicModel> topicModel) {
        this.context = context;
        this.topicModel = topicModel;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topiclayout, null);
        return new TopicAdapter.TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
     TopicModel model = topicModel.get(position);
        holder.textView.setText(model.getTopicName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                topicId = model.getTopicId();
                Intent intent = new Intent(context, Content.class);
                intent.putExtra("topicId",model.getTopicId());
                intent.putExtra("topicName",model.getTopicName());
                intent.putExtra("topicUrl",model.getTopicUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicModel.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.topicName);
        }
    }

}
