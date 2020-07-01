package com.gachon.smartedu.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
    List<LectureListItem> list;
    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference;

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
                // LID 추가 필요

                v.getContext().startActivity(intent);
            }
        });

        // Remove lecture button event
        holder.lec_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("강의삭제");
                builder.setMessage("해당 과목을 삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Remove the view
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, list.size());

                                String myUID = mAuth.getCurrentUser().getUid();
                                dbReference = fbDatabase.getReference("Users");
                                dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.w("Remove Lecture", "loadPost:onCancelled", error.toException());
                                    }
                                });
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();

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
    LinearLayout lec_item, lec_out;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        lec_item = itemView.findViewById(R.id.lec_item);
        lec_out = itemView.findViewById(R.id.lec_out);
        lectureName = itemView.findViewById(R.id.lecture_name_item);
        lectureInfo = itemView.findViewById(R.id.lecture_info_item);
    }
}
