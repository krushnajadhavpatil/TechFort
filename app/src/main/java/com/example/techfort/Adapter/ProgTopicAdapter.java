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

import com.example.techfort.Activity.ProgContentActivity;
import com.example.techfort.Model.ProgTopicModel;
import com.example.techfort.R;

import java.util.ArrayList;

public class ProgTopicAdapter extends RecyclerView.Adapter<ProgTopicAdapter.ProgTopicViewHolder> {

    Context context;
    ArrayList<ProgTopicModel> topicModel;
    private static String progTopicId;
    public final String TAG = ProgTopicAdapter.class.getSimpleName();

    public ProgTopicAdapter(Context context, ArrayList<ProgTopicModel> topicModel) {
        this.context = context;
        this.topicModel = topicModel;
    }

    //Sending topic id without intent
    public static String getProgTopicId() {
        return progTopicId;
    }

    @NonNull
    @Override
    public ProgTopicAdapter.ProgTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topiclayout, null);
        return new ProgTopicAdapter.ProgTopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgTopicAdapter.ProgTopicViewHolder holder, int position) {
        ProgTopicModel model = topicModel.get(position);
        holder.textView.setText(model.getProgName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " TopicId  " + model.getProgTopicId());
                progTopicId = model.getProgTopicId();
                Intent intent = new Intent(context, ProgContentActivity.class);
                intent.putExtra("topicName", model.getProgName());
                intent.putExtra("topicUrl", model.getProgUrl());
                intent.putExtra("topicId",model.getProgTopicId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicModel.size();
    }

    public class ProgTopicViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ProgTopicViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.topicName);
        }
    }
}
