package com.gachon.smartedu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.gachon.smartedu.LectureListItem;
import com.gachon.smartedu.R;
import com.gachon.smartedu.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LectureListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ImageButton add_lec_btn;
    private static final int add_lec_requestCode = 1;
    private static final int add_lec_resultCode = 100;

    List<LectureListItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        recyclerView = findViewById(R.id.lecture_rv);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);recyclerView.setHasFixedSize(false);

        Toolbar toolBar = findViewById(R.id.lectureList_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // 강의 추가 버튼 입력 이벤트
        add_lec_btn = (ImageButton) findViewById(R.id.add_lecture_btn);

        add_lec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LectureListActivity.this, AddLectureActivity.class);
                startActivityForResult(intent, add_lec_requestCode);
            }
        });

    }


    // Toolbar에 menu 버튼 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lecturelist_toolbar, menu);

        return true;
    }

    // Menu Item click event
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        DrawerLayout lecturelist_drawer = findViewById(R.id.lecturelist_drawer);
        switch(item.getItemId())
        {
            case R.id.toolbar_menu:
                lecturelist_drawer.openDrawer(GravityCompat.END);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // AddLectureActivity result 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == add_lec_requestCode && resultCode == add_lec_resultCode) {
            // Professor: 받아온 result로 lecture 생성
//            if(professor == true)
            list.add(new LectureListItem(data.getStringExtra("LectureName"),
                    "학점: " + data.getStringExtra("Credit") +
                    " / 학생정원: " + data.getStringExtra("MaxNum")));

            recyclerView.setAdapter(new RecyclerAdapter(list));


        }
    }




}