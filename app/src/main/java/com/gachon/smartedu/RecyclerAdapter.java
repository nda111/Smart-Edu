package com.gachon.smartedu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
    List<LectureListItem> list;

    public RecyclerAdapter(List<LectureListItem> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecture_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        holder.imageView.setImageResource(list.get(position).image);
        holder.textView1.setText(list.get(position).tv1);
        holder.textView2.setText(list.get(position).tv2);

        holder.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), list.get(position).tv1, Toast.LENGTH_SHORT).show();
            }
        });

        holder.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), list.get(position).tv2, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class RecyclerViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView1, textView2;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.img);
        textView1 = itemView.findViewById(R.id.tv1);
        textView2 = itemView.findViewById(R.id.tv2);
    }
}
