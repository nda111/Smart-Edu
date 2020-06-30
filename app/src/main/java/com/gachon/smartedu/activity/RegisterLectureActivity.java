package com.gachon.smartedu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gachon.smartedu.Item.LectureListItem;
import com.gachon.smartedu.Item.RegisterLectureItem;
import com.gachon.smartedu.R;
import com.gachon.smartedu.adapter.RecyclerAdapter;
import com.gachon.smartedu.adapter.RegisterLectureAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterLectureActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<RegisterLectureItem> list = new ArrayList<>();
    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_lecture);
        recyclerView = findViewById(R.id.register_lecture_rv);


        dbReference = fbDatabase.getReference("LectureList");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()) {
                    Log.e("findRegisterLectureList", snap.child("professor name").getValue().toString());
                    list.add(new RegisterLectureItem(snap.child("name").getValue().toString(),
                            snap.child("professor name").getValue().toString(),
                            snap.child("credit").getValue().toString(),
                            snap.child("grade policy").getValue().toString(),
                            snap.child("max participant").getValue().toString(),
                            snap.getKey()));
                }
                recyclerView.setAdapter(new RegisterLectureAdapter(list));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("findRegisterLectureList", "loadPost:onCancelled", error.toException());
            }
        });






    }
}
