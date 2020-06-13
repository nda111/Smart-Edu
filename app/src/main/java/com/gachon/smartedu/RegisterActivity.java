package com.gachon.smartedu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_pw, register_pw2;
    private TextView register_pw_check, register_pw2_check;
    private Button register_num_check, check_id, register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 툴바에 뒤로가기 버튼 만들기
        Toolbar toolBar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 비밀번호
        register_pw = (EditText) findViewById(R.id.register_pw);
        register_pw2 = (EditText) findViewById(R.id.register_pw2);

        register_pw_check = (TextView) findViewById(R.id.register_pw_check);
        register_pw2_check = (TextView) findViewById(R.id.register_pw2_check);


        // 비밀번호는 4자리 이상만 가능
        register_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(register_pw.getText().length() > 0 && register_pw.getText().length() < 4) {
                    register_pw_check.setText("4자리 이상 입력하세요");
                }
                else if(register_pw.getText().length() >= 4) {
                    register_pw_check.setText("사용가능");
                }
                else if(register_pw.getText().length() == 0) {
                    register_pw_check.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        // 비밀번호 한번 더 확인
        register_pw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(register_pw2.getText().length() != 0 && register_pw.getText().toString().equals(register_pw2.getText().toString())) {
                    register_pw2_check.setText("사용가능");
                }
                else if(register_pw2.getText().length() != 0 && !register_pw.getText().toString().equals(register_pw2.getText().toString())) {
                    register_pw2_check.setText("비밀번호가 다릅니다");
                }
                else if(register_pw2.getText().length() == 0) {
                    register_pw2_check.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

//        // 인증 버튼
//        register_num_check = (Button) findViewById(R.id.register_num_check);
//        register_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        // 이메일 중복 확인 버튼
//        check_id =(Button) findViewById(R.id.check_id);
//        check_id.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        // 가입 버튼
//        register_btn = (Button) findViewById(R.id.register_btn);
//        register_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });



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