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

import com.gachon.smartedu.Item.ParticipantItem;
import com.gachon.smartedu.Item.RegisterLectureItem;
import com.gachon.smartedu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.CustomViewHolder> {

    List<ParticipantItem> list;

//    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
//    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    private DatabaseReference dbReference;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView participantUserId, ParticipantName, ParticipantPosition;

        public CustomViewHolder(View itemView) {
            super(itemView);

            participantUserId = itemView.findViewById(R.id.userId);
            ParticipantName = itemView.findViewById(R.id.userName);
            ParticipantPosition = itemView.findViewById(R.id.userPosition);
        }
    }

    public ParticipantAdapter(List<ParticipantItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ParticipantAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_participant_list, viewGroup, false);
        return new ParticipantAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ParticipantAdapter.CustomViewHolder viewHolder, int position) {

        if (list.get(position).getUserId().length() != 4) {
            viewHolder.participantUserId.setText(list.get(position).getUserId().substring(0,4)+"xxxxx");
            viewHolder.ParticipantPosition.setText("Student ("+list.get(position).getRole()+")");
        } else {
            viewHolder.ParticipantPosition.setText("Teacher ("+list.get(position).getRole()+")");
        }
        viewHolder.ParticipantName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}