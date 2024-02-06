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
import com.example.techfort.Activity.ProgTopicActivity;
import com.example.techfort.Model.ProgrammingModel;
import com.example.techfort.R;

import java.util.ArrayList;

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.ProgrammingViewHolder> {

    ArrayList<ProgrammingModel> programmingModels;
    Context context;
    private static String progId;

    public ProgrammingAdapter(Context context, ArrayList<ProgrammingModel> programmingModels) {

        this.context = context;
        this.programmingModels = programmingModels;

    }

    //Sending cat id without intent
    public static String getProgId() {
        return progId;
    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, null);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {

      ProgrammingModel model = programmingModels.get(position);

        holder.textView.setText(model.getProgName());
        Glide.with(context)
                .load(model.getProgImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progId = model.getProgId();
                Intent intent = new Intent(context, ProgTopicActivity.class);
                intent.putExtra("progId", model.getProgId());
                intent.putExtra("progName", model.getProgName());
                intent.putExtra("progImage", model.getProgImage());
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return programmingModels.size();
    }

    public class ProgrammingViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.category);

        }
    }
}
