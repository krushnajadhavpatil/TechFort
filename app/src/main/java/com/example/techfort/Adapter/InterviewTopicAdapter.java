package com.example.techfort.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Activity.InterviewContent;
import com.example.techfort.Model.InterviewTopicModel;
import com.example.techfort.R;

import java.util.ArrayList;

public class InterviewTopicAdapter extends RecyclerView.Adapter<InterviewTopicAdapter.ViewHolder> {

    Context context;
    ArrayList<InterviewTopicModel> topicModel;

    public InterviewTopicAdapter(Context context, ArrayList<InterviewTopicModel> topicModel) {
        this.context = context;
        this.topicModel = topicModel;
    }

    @NonNull
    @Override
    public InterviewTopicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topiclayout, null);
        return new InterviewTopicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterviewTopicAdapter.ViewHolder holder, int position) {
        InterviewTopicModel model = topicModel.get(position);
        holder.textView.setText(model.getTopicName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, InterviewContent.class);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.topicName);
        }
    }
}
