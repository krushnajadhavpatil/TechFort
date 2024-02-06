package com.example.techfort.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfort.Activity.PdfViewer;
import com.example.techfort.Model.CourseMaterialModel;
import com.example.techfort.R;

import java.util.List;


public class CourseMaterialAdapter extends RecyclerView.Adapter<CourseMaterialAdapter.EbookViewHolder> {

    private Context context;
    private List<CourseMaterialModel> list;

    public CourseMaterialAdapter(Context context, List<CourseMaterialModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull

    @Override
    public EbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coursematerial_singlerow, parent, false);
        return new EbookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EbookViewHolder holder, final int position) {

        holder.ebookName.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdfViewer.class);
                intent.putExtra("pdfUrl", list.get(position).getPdfUrl());
                intent.putExtra("name",list.get(position).getName());
                context.startActivity(intent);
            }
        });
//        holder.ebookDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(list.get(position).getPdfUrl()));
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EbookViewHolder extends RecyclerView.ViewHolder {
        private TextView ebookName;


        public EbookViewHolder(@NonNull View itemView) {
            super(itemView);
            ebookName = itemView.findViewById(R.id.ebookName);

        }
    }
}
