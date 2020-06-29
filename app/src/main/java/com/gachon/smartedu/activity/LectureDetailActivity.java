package com.gachon.smartedu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.gachon.smartedu.R;

public class LectureDetailActivity extends AppCompatActivity {
    LinearLayout participant, post, assignment, announce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_detail);

        // Get Lecture Name, Lecture LID
        Intent intent = getIntent();

        String name = intent.getExtras().getString("LectureName");
        // String LID = intetn.getExtras().getString("LID");

        participant = findViewById(R.id.participant);
        post = findViewById(R.id.post);
        assignment = findViewById(R.id.assignment);
        announce = findViewById(R.id.announce);

        participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LectureDetailActivity.this, ParticipantActivity.class);

                startActivity(intent);
                }
            });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LectureDetailActivity.this, PostActivity.class);

                startActivity(intent);
            }
        });

        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LectureDetailActivity.this, AssignmentActivity.class);

                startActivity(intent);
            }
        });

        announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LectureDetailActivity.this, AnnounceActivity.class);

                startActivity(intent);
            }
        });

        // 툴바에 뒤로가기 버튼 만들기
        Toolbar toolBar = findViewById(R.id.lecture_detail_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setTitle(name);
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