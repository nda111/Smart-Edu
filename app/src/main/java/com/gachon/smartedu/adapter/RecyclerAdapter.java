package com.gachon.smartedu.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachon.smartedu.Item.LectureListItem;
import com.gachon.smartedu.R;
import com.gachon.smartedu.activity.LectureDetailActivity;

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
        holder.lectureName.setText(list.get(position).name);
        holder.lectureInfo.setText(list.get(position).lecture_info);

        holder.lec_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LectureDetailActivity.class);

                intent.putExtra("LectureName",list.get(position).name);
                // LID 추가
                intent.putExtra("LID", list.get(position).lid);

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
    TextView lectureName, lectureInfo;
    LinearLayout lec_item;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        lec_item = itemView.findViewById(R.id.lec_item);
        lectureName = itemView.findViewById(R.id.lecture_name_item);
        lectureInfo = itemView.findViewById(R.id.lecture_info_item);
    }
}
