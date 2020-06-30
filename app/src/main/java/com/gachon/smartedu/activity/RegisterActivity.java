package com.gachon.smartedu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gachon.smartedu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_email, register_pw, register_pw2, register_name, register_num;
    private TextView register_pw_check, register_pw2_check;
    private Button register_num_check, register_btn;
    private String userinfo; //교수인지 학생인지 구분
    private Integer check_info = 0; // 학번 인증 통과시 1로 변환
    private FirebaseAuth mAuth;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        // 툴바에 뒤로가기 버튼 만들기
        Toolbar toolBar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //이메일
        register_email = (EditText) findViewById(R.id.register_email);

        // 비밀번호
        register_pw = (EditText) findViewById(R.id.register_pw);
        register_pw2 = (EditText) findViewById(R.id.register_pw2);

        register_pw_check = (TextView) findViewById(R.id.register_pw_check);
        register_pw2_check = (TextView) findViewById(R.id.register_pw2_check);

        // 이름
        register_name = (EditText) findViewById(R.id.register_name);

        // 학번 or 교수번호
        register_num = (EditText) findViewById(R.id.register_num);

        // 학번 or 교수번호 인증
        register_num_check = (Button) findViewById(R.id.register_num_check);

        register_num_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(register_num.length() == 9)
                {
                    userinfo = "학생";
                    check_info = 1;
                    Toast.makeText(RegisterActivity.this, "학생인증완료",
                            Toast.LENGTH_SHORT).show();
                }
                else if(register_num.length() == 4)
                {
                    userinfo = "교수";
                    check_info = 1;
                    Toast.makeText(RegisterActivity.this, "교수인증완료",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    check_info = 0;
                    Toast.makeText(RegisterActivity.this, "인증실패",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 비밀번호는 4자리 이상만 가능
        register_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(register_pw.getText().length() > 0 && register_pw.getText().length() < 6) {
                    register_pw_check.setText("6자리 이상 입력하세요");
                }
                else if(register_pw.getText().length() >= 6) {
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

        // 가입 버튼
        register_btn = (Button) findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    // Start register method
    private void register() {

        String email = register_email.getText().toString().trim();
        String password = register_pw.getText().toString().trim();
        String passwordCheck = register_pw2.getText().toString().trim();


        if (register_name.length() < 1) {
            Toast.makeText(RegisterActivity.this, "이름을 입력해주세요",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else if (check_info == 0){
            Toast.makeText(RegisterActivity.this, "학번 or 사번 인증을 해주세요",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else if (email.length() < 1) {
            Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.length() < 6 ) {
            Toast.makeText(RegisterActivity.this, "올바른 비밀번호를 설정해주세요",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if(password.equals(passwordCheck)) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");

                                FirebaseUser user = mAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = register_name.getText().toString().trim();
                                String reg_num = register_num.getText().toString().trim();
                                String userposition = userinfo;

                                // Save the table in firebase DB
                                HashMap<Object,String> hashMap = new HashMap<>();

                                hashMap.put("uid", uid);
                                hashMap.put("email", email);
                                hashMap.put("name", name);
                                hashMap.put("identification number", reg_num);
                                hashMap.put("user position",userposition);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMap);

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                                finish();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "중복된 아이디 혹은 이메일 형식이 아닙니다",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        else {
            Toast.makeText(RegisterActivity.this, "비밀번호를 확인해주세요",
                    Toast.LENGTH_SHORT).show();
            return;
        }


    }

    // Activate back button in Toolbar
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