package com.example.techfort.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techfort.Activity.WebDevTopicActivity;
import com.example.techfort.Model.WebDevModel;
import com.example.techfort.R;

import java.util.ArrayList;

public class WebDevAdapter extends RecyclerView.Adapter<WebDevAdapter.ViewHolder> {

    ArrayList<WebDevModel> WebDevModels;
    Context context;

    public WebDevAdapter(Context context, ArrayList<WebDevModel> webDevModels) {
        WebDevModels = webDevModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WebDevModel model = WebDevModels.get(position);

        holder.textView.setText(model.getWebName());
        Glide.with(context)
                .load(model.getWebImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebDevTopicActivity.class);
                intent.putExtra("webId", model.getWebId());
                intent.putExtra("webName", model.getWebName());
                intent.putExtra("webImage", model.getWebImage());
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return WebDevModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.category);
        }
    }
}
