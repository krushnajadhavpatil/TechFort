package com.example.techfort.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Activity.QuizActivity;
import com.example.techfort.Model.WeeklyQuizModel;
import com.example.techfort.R;

import java.util.ArrayList;

public class WeeklyQuizAdapter extends RecyclerView.Adapter<WeeklyQuizAdapter.WeeklyQuizViewHolder> {

    Context context;
    ArrayList<WeeklyQuizModel> weeklyQuizModel;

    public WeeklyQuizAdapter(Context context, ArrayList<WeeklyQuizModel> weeklyQuizModel) {
        this.context = context;
        this.weeklyQuizModel = weeklyQuizModel;
    }

    @NonNull
    @Override
    public WeeklyQuizAdapter.WeeklyQuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weeklyquizlayout, null);
        return new WeeklyQuizAdapter.WeeklyQuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyQuizAdapter.WeeklyQuizViewHolder holder, int position) {
        WeeklyQuizModel model = weeklyQuizModel.get(position);
        holder.textView.setText(model.getWeeklyQuizCategoryName());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra("wqCatId", model.getWeeklyQuizCategoryId());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return weeklyQuizModel.size();
    }

    public class WeeklyQuizViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public WeeklyQuizViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.weeklyQuizCategoryName);
        }
    }
}

