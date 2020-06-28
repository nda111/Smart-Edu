package com.gachon.smartedu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.gachon.smartedu.R;

public class LectureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_detail);

        // Get Lecture Name, Lecture LID
        Intent intent = getIntent();

        String name = intent.getExtras().getString("LectureName");
        // String LID = intetn.getExtras().getString("LID");

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