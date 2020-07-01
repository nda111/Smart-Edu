package com.gachon.smartedu.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachon.smartedu.Item.RegisterLectureItem;
import com.gachon.smartedu.R;
import com.gachon.smartedu.activity.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.List;

public class RegisterLectureAdapter extends RecyclerView.Adapter<RegisterLectureAdapter.CustomViewHolder> {
    List<RegisterLectureItem> list;

    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView regLectureName, regPfName,regCredit, regGradeP, regMaxNum, registeredTxt;
        Button regBtn;

        public CustomViewHolder(View itemView) {
            super(itemView);
            regLectureName = itemView.findViewById(R.id.reg_lec_name);
            regPfName = itemView.findViewById(R.id.reg_pf_name);
            regCredit = itemView.findViewById(R.id.reg_credit);
            regGradeP = itemView.findViewById(R.id.reg_gradeP);
            regMaxNum = itemView.findViewById(R.id.reg_maxNum);
            regBtn = itemView.findViewById(R.id.reg_btn);
            registeredTxt = itemView.findViewById(R.id.registered_txt);

        }
    }


    public RegisterLectureAdapter(List<RegisterLectureItem> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.register_lecture_list_item, viewGroup, false);

        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder viewHolder, int position) {
        viewHolder.regLectureName.setText(list.get(position).lectureName);
        viewHolder.regPfName.setText("교수: " + list.get(position).pfName);
        viewHolder.regCredit.setText(list.get(position).credit);
        viewHolder.regGradeP.setText(list.get(position).gradeP);
        viewHolder.regMaxNum.setText("학생정원: " + list.get(position).maxNum + "/");

        final String LID = list.get(position).LID;

        // Click Register button event
        viewHolder.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String myUID = mAuth.getCurrentUser().getUid();

                // Register in
                dbReference = fbDatabase.getReference("LectureList");
                dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap :snapshot.getChildren()){
                            // Find this lecture and add current user
                            if(snap.getKey().equals(LID)){
                                // Save the table in firebase DB
                                Integer checkNum = 0;

                                if(snap.child("member").getValue() != null){
                                    for (DataSnapshot check : snap.child("member").getChildren()){
                                        if(check.getValue().equals(myUID)){
                                            checkNum = 1;
                                        }
                                    }
                                }

                                dbReference = fbDatabase.getReference("LectureList").child(LID);
                                if(snap.child("member").getValue() == null)
                                {
                                    dbReference.child("member").child("1").setValue(myUID);
                                }
                                else if(checkNum == 1){
                                    Toast.makeText(view.getContext(), "이미 등록한 강의입니다다",
                                           Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else{
                                    Long i = snap.child("member").getChildrenCount()+1;
                                    dbReference.child("member").child(i.toString()).setValue(myUID);
                                }



                                Toast.makeText(view.getContext(), "등록되었습니다",
                                        Toast.LENGTH_SHORT).show();
                                viewHolder.registeredTxt.setVisibility(View.VISIBLE);

                                // Send Intent to LectureListActivity
                                Intent resultIntent = new Intent();

                                resultIntent.putExtra("LectureName", snap.child("name").getValue().toString());
                                resultIntent.putExtra("Credit", snap.child("credit").getValue().toString());
                                resultIntent.putExtra("GradePolicy", snap.child("grade policy").getValue().toString());
                                resultIntent.putExtra("MaxNum", snap.child("max participant").getValue().toString());
                                ((Activity)view.getContext()).setResult(200, resultIntent);

                                ((Activity)view.getContext()).finish();
                                break;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("Register in", "loadPost:onCancelled", error.toException());
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}