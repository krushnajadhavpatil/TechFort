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
import com.example.techfort.Model.BookmarkModel;
import com.example.techfort.R;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    public List<BookmarkModel> bookmarkList;
    Context context;

    public BookmarkAdapter(List<BookmarkModel> bookmarkList, Context context) {
        this.bookmarkList = bookmarkList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
        context = parent.getContext();
        return new BookmarkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {

        BookmarkModel model = bookmarkList.get(position);
        holder.textView.setText(model.getTopicName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return bookmarkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bookmark_topic);
        }
    }
}
