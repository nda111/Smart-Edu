package com.gachon.smartedu.activity;

import androidx.annotation.NonNull;
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

import com.gachon.smartedu.Item.UserItem;
import com.gachon.smartedu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddLectureActivity extends AppCompatActivity {
    private Button add_btn;
    private static final int add_lec_resultCode = 100;
    private EditText lecture_name, max_stu_num;
    private Spinner credit_spinner, grade_spinner;
    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

        // Activate back button in Toolbar
        Toolbar toolBar = findViewById(R.id.add_lecture_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] credit_items = {"1학점", "2학점", "3학점", "4학점", "P/F"};
        final String[] grade_pc_items = {"상대평가", "절대평가"};

        // Set Credit spinner
        credit_spinner = (Spinner) findViewById(R.id.credit_spinener);
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

        // Set grading policy spinner
        grade_spinner = (Spinner) findViewById(R.id.gd_policy_spinener);
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

        lecture_name = (EditText) findViewById(R.id.lecture_name);
        max_stu_num = (EditText) findViewById(R.id.max_stu_num);

        // 강의 개설 버튼 입력 이벤트
        add_btn = (Button) findViewById(R.id.add_btn);

        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.e("ClickError", lecture_name.getEditableText().toString());

                // Check error case
                try{
                    if(lecture_name.getEditableText().toString().getBytes().length <= 0 || max_stu_num.getEditableText().toString().getBytes().length <= 0
                            || Integer.parseInt(max_stu_num.getEditableText().toString()) <= 0){
                        Log.e("Toast", "test");
                        Toast.makeText(AddLectureActivity.this, "항목을 올바르게 채워주세요", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.e("Intent", lecture_name.getEditableText().toString());
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("LectureName", lecture_name.getEditableText().toString());
                        resultIntent.putExtra("Credit", credit_spinner.getSelectedItem().toString());
                        resultIntent.putExtra("GradePolicy", grade_spinner.getSelectedItem().toString());
                        resultIntent.putExtra("MaxNum", max_stu_num.getEditableText().toString());
                        setResult(add_lec_resultCode, resultIntent);

                        // Create New Lecture in DB
                        addLectureListToDB();

                        finish();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(AddLectureActivity.this, "학생정원을 숫자로 입력해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addLectureListToDB() {
        final String lectureName = lecture_name.getText().toString().trim();
        final String maxStudent = max_stu_num.getText().toString().trim();
        final String credit = credit_spinner.getSelectedItem().toString().trim();
        final String gradePolicy = grade_spinner.getSelectedItem().toString().trim();
        final String pfUID = mAuth.getCurrentUser().getUid();

        // Create Lecture DB
        dbReference = fbDatabase.getReference("Users");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()) {
                    Log.e("professorName",snap.child("name").getValue().toString());
                    if(pfUID.equals(snap.getKey())) {

                        // Save the table in firebase DB
                        HashMap<Object,String> hashMap = new HashMap<>();

                        hashMap.put("name", lectureName);
                        hashMap.put("professor name", snap.child("name").getValue().toString());
                        hashMap.put("max participant", maxStudent);
                        hashMap.put("credit", credit);
                        hashMap.put("grade policy", gradePolicy);
                        hashMap.put("professor uid", pfUID);


                        dbReference = fbDatabase.getReference("LectureList").push();
                        dbReference.setValue(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("findUsersName", "loadPost:onCancelled", error.toException());
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