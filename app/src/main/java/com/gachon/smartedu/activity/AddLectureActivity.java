package com.gachon.smartedu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gachon.smartedu.R;

public class AddLectureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        // 툴바에 뒤로가기 버튼 만들기
        Toolbar toolBar = findViewById(R.id.add_lecture_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] credit_items = {"1학점", "2학점", "3학점", "4학점", "P/F"};
        final String[] grade_pc_items = {"상대평가", "절대평가"};

        // 학점 spinner 설정
        Spinner credit_spinner = (Spinner) findViewById(R.id.credit_spinener);
        ArrayAdapter<String> credit_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, credit_items);
        credit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        credit_spinner.setAdapter(credit_adapter);

        credit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 평가방법 spinner 설정
        Spinner grade_spinner = (Spinner) findViewById(R.id.gd_policy_spinener);
        ArrayAdapter<String> grade_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, grade_pc_items);
        grade_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade_spinner.setAdapter(grade_adapter);

        grade_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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