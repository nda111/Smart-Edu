package com.gachon.smartedu.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gachon.smartedu.R;
import com.google.firebase.database.FirebaseDatabase;

public class AddLectureActivity extends AppCompatActivity {
    private Button add_btn;
    private static final int add_lec_resultCode = 100;
    private final FirebaseDatabase Database = FirebaseDatabase.getInstance();

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
        final Spinner credit_spinner = (Spinner) findViewById(R.id.credit_spinener);
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
        final Spinner grade_spinner = (Spinner) findViewById(R.id.gd_policy_spinener);
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

        // 강의 개설 버튼 입력 이벤트
        add_btn = (Button) findViewById(R.id.add_btn);
        final EditText lecture_name = findViewById(R.id.lecture_name);
        final EditText max_num = findViewById(R.id.max_stu_num);

        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.e("ClickError", lecture_name.getEditableText().toString());

                // Check error case
                try{
                    if(lecture_name.getEditableText().toString().getBytes().length <= 0 || max_num.getEditableText().toString().getBytes().length <= 0
                            || Integer.parseInt(max_num.getEditableText().toString()) <= 0){
                        Log.e("Toast", "test");
                        Toast.makeText(AddLectureActivity.this, "항목을 올바르게 채워주세요", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.e("Intent", lecture_name.getEditableText().toString());
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("LectureName", lecture_name.getEditableText().toString());
                        resultIntent.putExtra("Credit", credit_spinner.getSelectedItem().toString());
                        resultIntent.putExtra("GradePolicy", grade_spinner.getSelectedItem().toString());
                        resultIntent.putExtra("MaxNum", max_num.getEditableText().toString());
                        setResult(add_lec_resultCode, resultIntent);
                        finish();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(AddLectureActivity.this, "학생정원을 숫자로 입력해주세요", Toast.LENGTH_SHORT).show();
                }

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