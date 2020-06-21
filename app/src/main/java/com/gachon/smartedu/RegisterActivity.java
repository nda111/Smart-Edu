package com.gachon.smartedu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_email, register_pw, register_pw2;
    private TextView register_pw_check, register_pw2_check;
    private Button register_num_check, check_id, register_btn;

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

    //실질적으로 회원가입 진행
    private void register() {

        String email = register_email.getText().toString().trim();
        String password = register_pw.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //UI
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //UI
                        }

                        // ...
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