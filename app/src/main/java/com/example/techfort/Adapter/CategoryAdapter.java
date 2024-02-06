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
import com.example.techfort.Activity.Compiler;
import com.example.techfort.Activity.CourseMaterial;
import com.example.techfort.Activity.InterviewTopicActivity;
import com.example.techfort.Activity.ProgrammingActivity;
import com.example.techfort.Activity.TopicActivity;
import com.example.techfort.Activity.WebDevActivity;
import com.example.techfort.Model.CategoryModel;
import com.example.techfort.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private static String catId;
    Context context;
    ArrayList<CategoryModel> categoryModels;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {

        this.context = context;
        this.categoryModels = categoryModels;

    }

    //Sending cat id without intent
    public static String getCatId() {
        return catId;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel model = categoryModels.get(position);

        holder.textView.setText(model.getCategoryName());
        Glide.with(context)
                .load(model.getCategoryImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getCategoryName().equals("Web Development")) {
                    catId = model.getCategoryId();
                    Intent intent = new Intent(context, WebDevActivity.class);
                    intent.putExtra("catId", model.getCategoryId());
                    intent.putExtra("categoryName", model.getCategoryName());
                    intent.putExtra("categoryImage", model.getCategoryImage());
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (model.getCategoryName().equals("Programming Languages")) {
                    catId = model.getCategoryId();
                    Intent intent = new Intent(context, ProgrammingActivity.class);
                    intent.putExtra("catId", model.getCategoryId());
                    intent.putExtra("categoryName", model.getCategoryName());
                    intent.putExtra("categoryImage", model.getCategoryImage());
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (model.getCategoryName().equals("Coding Area")) {
                    Intent intent = new Intent(context, Compiler.class);
                    intent.putExtra("catId", model.getCategoryId());
                    intent.putExtra("categoryName", model.getCategoryName());
                    intent.putExtra("categoryImage", model.getCategoryImage());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (model.getCategoryName().equals("CheetSheets")) {
                    Intent intent = new Intent(context, CourseMaterial.class);
                    intent.putExtra("catId", model.getCategoryId());
                    intent.putExtra("categoryName", model.getCategoryName());
                    intent.putExtra("categoryImage", model.getCategoryImage());
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (model.getCategoryName().equals("Interview Questions")) {
                    Intent intent = new Intent(context, InterviewTopicActivity.class);
                    intent.putExtra("catId", model.getCategoryId());
                    intent.putExtra("categoryName", model.getCategoryName());
                    intent.putExtra("categoryImage", model.getCategoryImage());
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    catId = model.getCategoryId();
                    Intent intent = new Intent(context, TopicActivity.class);
                    intent.putExtra("catId", model.getCategoryId());
                    intent.putExtra("categoryName", model.getCategoryName());
                    intent.putExtra("categoryImage", model.getCategoryImage());
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.category);
        }
    }


}
