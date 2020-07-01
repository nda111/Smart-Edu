package com.gachon.smartedu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.gachon.smartedu.Item.LectureListItem;
import com.gachon.smartedu.Item.ParticipantItem;
import com.gachon.smartedu.R;
import com.gachon.smartedu.adapter.RecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class ParticipantActivity extends AppCompatActivity {

    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference;
    List<ParticipantItem> list = null;
    private final static String TAG = "ParticipantActivity";
    List<String> uidList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        Intent intent = getIntent();
        String LID = intent.getExtras().getString("LID");

        getParticipant(LID);

        // 툴바에 뒤로가기 버튼 만들기
        Toolbar toolBar = findViewById(R.id.participant_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getParticipant(final String targetLID) {

        dbReference = fbDatabase.getReference("LectureList");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()) {
                    String LID = snap.getKey().toString();
                    if(targetLID.contentEquals(LID)) {
                        // 수업을 신청한 학생들의 UID 모두 찾아서 리스트로!
                        for (DataSnapshot snap1 : snap.getChildren()) {
                            String UID = snap1.child("UID").getValue().toString();
                            uidList.add(UID);
                        }
                        Log.d(TAG, "onDataChange: 44444444444444");
                        Log.i(TAG, uidList.get(0));
//                        recyclerView.setAdapter(new RecyclerAdapter(list));
                    }
                }
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