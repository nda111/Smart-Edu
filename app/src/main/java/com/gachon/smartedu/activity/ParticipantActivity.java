package com.gachon.smartedu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.gachon.smartedu.Item.LectureListItem;
import com.gachon.smartedu.Item.ParticipantItem;
import com.gachon.smartedu.R;
import com.gachon.smartedu.adapter.ParticipantAdapter;
import com.gachon.smartedu.adapter.RecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParticipantActivity extends AppCompatActivity {

    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference;
    List<ParticipantItem> list = new ArrayList<>();
    private final static String TAG = "ParticipantActivity";
    ArrayList<String> uidList = new ArrayList<>();
    RecyclerView recyclerView;
    boolean isProfessor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        // 툴바에 뒤로가기 버튼 만들기
        Toolbar toolBar = findViewById(R.id.participant_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.participant_rv);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);recyclerView.setHasFixedSize(false);

        Intent intent = getIntent();
        String LID = intent.getExtras().getString("LID");

        getUID(LID);
        getParticipant();

    }

    private void getUID(final String targetLID) {

        dbReference = fbDatabase.getReference("LectureList");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()) {
                    String LID = snap.getKey().toString();
                    if(targetLID.contentEquals(LID)) {
                        // 교수님 UID 찾기
                        if (isProfessor) {
                            String uID = snap.child("professor uid").getValue().toString();
                            uidList.add(uID);
                            isProfessor = false;
                        }
                        // 수업을 신청한 학생들의 UID 모두 찾아서 리스트로!
                        for(DataSnapshot check : snap.child("member").getChildren()) {
                            String uID = check.getValue().toString();
                            uidList.add(uID);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("findUID", "loadPost:onCancelled", error.toException());
            }
        });
    }

    private void getParticipant() {

        dbReference = fbDatabase.getReference("Users");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()) {
                    String UID = snap.getKey().toString();
                    if(uidList.contains(UID)) {
                        // UID로 학번, 이름, 역할 가져오기
                        ParticipantItem participantItem = new ParticipantItem();
                        participantItem.setUserId(snap.child("identification number").getValue().toString());
                        participantItem.setName(snap.child("name").getValue().toString());
                        participantItem.setRole(snap.child("user position").getValue().toString());

                        list.add(participantItem);
                    }
                }
                recyclerView.setAdapter(new ParticipantAdapter(list));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("findParticipant", "loadPost:onCancelled", error.toException());
            }
        });
    }

    // 툴바 뒤로가기 버튼 활성화
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}