package com.gachon.smartedu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachon.smartedu.activity.AddLectureActivity;
import com.gachon.smartedu.activity.LectureDetailActivity;
import com.gachon.smartedu.activity.LectureListActivity;

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
        holder.textView1.setText(list.get(position).tv1);
        holder.textView2.setText(list.get(position).tv2);

        holder.lec_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LectureDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView textView1, textView2;
    LinearLayout lec_item;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        lec_item = itemView.findViewById(R.id.lec_item);
        textView1 = itemView.findViewById(R.id.tv1);
        textView2 = itemView.findViewById(R.id.tv2);
    }
}
