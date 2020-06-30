package com.gachon.smartedu.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.gachon.smartedu.Item.LectureListItem;
import com.gachon.smartedu.R;
import com.gachon.smartedu.adapter.RecyclerAdapter;
import com.gachon.smartedu.messaging.activity.MessageListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LectureListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ImageButton add_lec_btn, message_btn;
    private ArrayList myLIDList = new ArrayList();
    private static final int add_lec_requestCode = 1;
    private static final int add_lec_resultCode = 100;
    private static final int register_lec_requestCode = 2;
    private Integer id;

    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference;


    List<LectureListItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

//        // Check user position
//        checkUserPosition();
      

        // Get my lecture list
        findMyLectureFromFirebaseDB();
        getMyLecture();

        recyclerView = findViewById(R.id.lecture_rv);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);recyclerView.setHasFixedSize(false);

        Toolbar toolBar = findViewById(R.id.lectureList_toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // Add lecture button event
        add_lec_btn = (ImageButton) findViewById(R.id.add_lecture_btn);
        add_lec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Find current user's position
                final String userUID = mAuth.getUid();
                dbReference = fbDatabase.getReference("Users");
                dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap :snapshot.getChildren()) {
                            String UID = snap.getKey();
                            Log.e("UID", snap.child("user position").getValue().toString() );

                            if(UID.equals(userUID)) {
                                String userp = snap.child("user position").getValue().toString();
                                Log.e("UID if", userp );
                                // If Professor -> AddLectureActivity
                                if (userp.equals("교수")) {
                                    Intent intent = new Intent(LectureListActivity.this, AddLectureActivity.class);
                                    startActivityForResult(intent, add_lec_requestCode);
                                }
                                // If Student -> RegisterLectureActivity
                                else if (userp.equals("학생"))
                                {
                                    Intent intent = new Intent(LectureListActivity.this, RegisterLectureActivity.class);
                                    startActivityForResult(intent, register_lec_requestCode);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("findMyLecture", "loadPost:onCancelled", error.toException());
                    }
                });

            }
        });

        // 메시지 버튼 입력 이벤트
        message_btn = (ImageButton) findViewById(R.id.message_btn);

        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LectureListActivity.this, MessageListActivity.class);
                startActivity(intent);
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

    // Find my lectures and add LID in arraylist
    private void findMyLectureFromFirebaseDB() {
        final String myUID = mAuth.getCurrentUser().getUid();

        dbReference = fbDatabase.getReference("LectureList");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot snap :snapshot.getChildren()) {
                   String lecturePfUID = snap.child("professor uid").getValue().toString();

                   if(myUID.equals(lecturePfUID)) {
                       myLIDList.add(snap.getKey());
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("findMyLecture", "loadPost:onCancelled", error.toException());
            }
        });
    }

    private void getMyLecture() {

        dbReference = fbDatabase.getReference("LectureList");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()) {
                    String LID = snap.getKey().toString();

                    if(myLIDList.contains(LID)) {
                        list.add(new LectureListItem(snap.child("name").getValue().toString(),
                                "학점: " + snap.child("credit").getValue().toString() +
                                        " / 학생정원: " + snap.child("max participant").getValue().toString()));

                        recyclerView.setAdapter(new RecyclerAdapter(list));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("findMyLecture", "loadPost:onCancelled", error.toException());
            }
        });
    }

//    // Check User : Professor or Student
//    public void checkUserPosition() {
//        final String myUID = mAuth.getCurrentUser().getUid();
//
//        dbReference = fbDatabase.getReference("Users");
//        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snap :snapshot.getChildren()) {
//                    if(myUID.equals(snap.getKey())) {
//                        if(snap.child("user position").getValue().toString().equals("학생"))
//                        {
//
//                        }
//                        else {
//
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("findMyLecture", "loadPost:onCancelled", error.toException());
//            }
//        });
//    }





}