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
import com.example.techfort.Model.WebDevTopicModel;
import com.example.techfort.R;

import java.util.ArrayList;

public class WebDevTopicAdapter extends RecyclerView.Adapter<WebDevTopicAdapter.WebDevTopicViewHolder> {

    Context context;
    ArrayList<WebDevTopicModel> webDevTopicModel;

    public WebDevTopicAdapter(Context context, ArrayList<WebDevTopicModel> webDevTopicModel) {
        this.context = context;
        this.webDevTopicModel = webDevTopicModel;
    }

    @NonNull
    @Override
    public WebDevTopicAdapter.WebDevTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topiclayout, null);
        return new WebDevTopicAdapter.WebDevTopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WebDevTopicAdapter.WebDevTopicViewHolder holder, int position) {

        WebDevTopicModel model = webDevTopicModel.get(position);
        holder.textView.setText(model.getTopicName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Content.class);
                intent.putExtra("topicId", model.getTopicId());
                intent.putExtra("topicName", model.getTopicName());
                intent.putExtra("topicUrl", model.getTopicUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return webDevTopicModel.size();
    }

    public class WebDevTopicViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public WebDevTopicViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.topicName);
        }
    }
}
