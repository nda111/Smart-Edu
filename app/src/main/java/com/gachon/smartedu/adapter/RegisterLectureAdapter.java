package com.gachon.smartedu.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachon.smartedu.Item.RegisterLectureItem;
import com.gachon.smartedu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

public class RegisterLectureAdapter extends RecyclerView.Adapter<RegisterLectureAdapter.CustomViewHolder> {
    List<RegisterLectureItem> list;

    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView regLectureName, regPfName,regCredit, regGradeP, regMaxNum;
        Button regBtn;

        public CustomViewHolder(View itemView) {
            super(itemView);
            regLectureName = itemView.findViewById(R.id.reg_lec_name);
            regPfName = itemView.findViewById(R.id.reg_pf_name);
            regCredit = itemView.findViewById(R.id.reg_credit);
            regGradeP = itemView.findViewById(R.id.reg_gradeP);
            regMaxNum = itemView.findViewById(R.id.reg_maxNum);
            regBtn = itemView.findViewById(R.id.reg_btn);
            Log.e("CustomViewHolder", "in");

        }
    }


    public RegisterLectureAdapter(List<RegisterLectureItem> list) {
        Log.e("RegisterLectureAdapter", "in");
        this.list = list;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.e("onCreateViwHolder", "in");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.register_lecture_list_item, viewGroup, false);

        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, int position) {
        Log.e("onBindViewHolder", "in");
        viewHolder.regLectureName.setText(list.get(position).lectureName);
        viewHolder.regPfName.setText("교수: " + list.get(position).pfName);
        viewHolder.regCredit.setText(list.get(position).credit);
        viewHolder.regGradeP.setText(list.get(position).gradeP);
        viewHolder.regMaxNum.setText("학생정원: " + list.get(position).maxNum + "/");

        String LID = list.get(position).LID;

        // Click Register button event
        viewHolder.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}