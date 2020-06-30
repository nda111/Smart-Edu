package com.gachon.smartedu.adapter;

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
    public void onBindViewHolder(@NonNull final CustomViewHolder viewHolder, int position) {
        Log.e("onBindViewHolder", "in");
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
                                HashMap<Object,String> hashMap = new HashMap<>();

                                hashMap.put("UID", myUID);

                                dbReference = fbDatabase.getReference("LectureList").child(LID);
                                dbReference.child("member").setValue(hashMap);
                                Toast.makeText(view.getContext(), "등록되었습니다",
                                        Toast.LENGTH_SHORT).show();
                                viewHolder.registeredTxt.setVisibility(View.VISIBLE);
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