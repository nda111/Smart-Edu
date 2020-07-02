package com.gachon.smartedu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText login_id, login_pw;
    private Button login_btn;
    private TextView login_register, find_id, find_pw;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // EditText
        login_id = (EditText) findViewById(R.id.login_id);
        login_pw = (EditText) findViewById(R.id.login_pw);

        // Button
        login_btn = (Button) findViewById(R.id.login_btn);

        // Text button
        login_register = (TextView) findViewById(R.id.login_register);

        // 로그인 버튼 누르면 인증 확인
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        // 회원가입 버튼 누르면 RegisterActivity 호출
        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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

    // 로그인 진행
    private void logIn() {

        String email = login_id.getText().toString().trim();
        String password = login_pw.getText().toString().trim();

        if (email.length() < 1) {
            Toast.makeText(LoginActivity.this, "이메일을 입력하세요",
                    Toast.LENGTH_SHORT).show();
        } else if (password.length() < 1) {
            Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");

                                Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                                // Call LectureList Activity
                                Intent intent = new Intent(LoginActivity.this, LectureListActivity.class);
                                startActivity(intent);
                                finish();

                                //UI
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "가입된 사용자가 아닙니다",
                                        Toast.LENGTH_SHORT).show();
                                //UI
                                // ...
                            }

                            // ...
                        }
                    });
        }
    }
}