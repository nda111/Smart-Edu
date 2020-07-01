package com.gachon.smartedu.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gachon.smartedu.Item.LectureListItem;
import com.gachon.smartedu.R;
import com.gachon.smartedu.adapter.RecyclerAdapter;
import com.gachon.smartedu.messaging.activity.MessageListActivity;
import com.google.android.material.navigation.NavigationView;
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

    private final static String TAG = "LectureListActivity";
    RecyclerView recyclerView;
    private ImageButton add_lec_btn, message_btn;
    private ArrayList myLIDList = new ArrayList();
    private Integer myUserInfo;
    private static final int add_lec_requestCode = 1;
    private static final int add_lec_resultCode = 100;
    private static final int reg_lec_resultCode = 200;
    private static final int register_lec_requestCode = 2;
    private Integer id;
    private Context mContext;

    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference;

    List<LectureListItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);
        mContext = getApplicationContext();

        // Get my lecture list
        findMyLectureFromFirebaseDB(); // If professor, get my lecture list
        findMyRegisteredLectureFromFirebaseDB(); // If Student, get my registered lecture list
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
        final DrawerLayout lecturelist_drawer = findViewById(R.id.lecturelist_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        switch(item.getItemId())
        {
            case R.id.toolbar_menu:
                lecturelist_drawer.openDrawer(GravityCompat.END);
//                lecturelist_drawer.
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        lecturelist_drawer.closeDrawers();

                        int id = menuItem.getItemId();
                        String title = menuItem.getTitle().toString();

                        if(id == R.id.logout){
                            FirebaseAuth.getInstance().signOut();
                            Log.d(TAG, "onNavigationItemSelected: 로그아웃 시도중");
                            finish();
                        }

                        return true;
                    }
                });

                break;
        }

        return super.onOptionsItemSelected(item);
    }
//    // Menu Item click event
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        final DrawerLayout lecturelist_drawer = findViewById(R.id.lecturelist_drawer);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//
//        switch(item.getItemId())
//        {
//            case R.id.toolbar_menu:
//                lecturelist_drawer.openDrawer(GravityCompat.END);
////                lecturelist_drawer.
//                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        menuItem.setChecked(true);
//                        lecturelist_drawer.closeDrawers();
//
//                        int id = menuItem.getItemId();
//                        String title = menuItem.getTitle().toString();
//
//                        if(id == R.id.logout){
//                            FirebaseAuth.getInstance().signOut();
//                            Log.d(TAG, "onNavigationItemSelected: 로그아웃 시도중");
//                            finish();
//                        }
//
//                        return true;
//                    }
//                });
//
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    // Get AddLectureActivity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == add_lec_requestCode && resultCode == add_lec_resultCode) {
            // Professor: create lecture with intent data
//            if(professor == true)
            list.add(new LectureListItem(data.getStringExtra("LectureName"),
                    "학점: " + data.getStringExtra("Credit") +
                    " / 학생정원: " + data.getStringExtra("MaxNum"),
                    null)); //새로운 과목에 대한 LID 가져와야 함

            recyclerView.setAdapter(new RecyclerAdapter(list));
        }
        else if (requestCode == register_lec_requestCode && resultCode == reg_lec_resultCode){
            list.add(new LectureListItem(data.getStringExtra("LectureName"),
                    "학점: " + data.getStringExtra("Credit") +
                            " / 학생정원: " + data.getStringExtra("MaxNum")));

            recyclerView.setAdapter(new RecyclerAdapter(list));
        }
    }

    // Find my lectures and add LID in arraylist (Professor)
    private void findMyLectureFromFirebaseDB() {
        checkUserPosition();


        final String myUID = mAuth.getCurrentUser().getUid();

        dbReference = fbDatabase.getReference("LectureList");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot snap :snapshot.getChildren()) {
                   // If Student position, return
                   if(myUserInfo == 0){
                       return;
                   }

                   String lecturePfUID = snap.child("professor uid").getValue().toString();

                   if(myUID.equals(lecturePfUID)) {
                       myLIDList.add(snap.getKey());
                       Log.e("add","1");
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("findMyLecture", "loadPost:onCancelled", error.toException());
            }
        });
    }

    // Find my registered lectures and add LID in arraylist (Student)
    private void findMyRegisteredLectureFromFirebaseDB() {
        checkUserPosition();

        final String myUID = mAuth.getCurrentUser().getUid();

        dbReference = fbDatabase.getReference("LectureList");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()) {
                    // If professor position, return
                    if(myUserInfo == 1){
                        return;
                    }

                    for(DataSnapshot check : snap.child("member").getChildren())
                    {

                        if(check.getValue().equals(myUID)) {
                            myLIDList.add(snap.getKey());
                            Log.e("check", "1");
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



    private void getMyLecture() {
        checkUserPosition();

        dbReference = fbDatabase.getReference("LectureList");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()) {
                    String LID = snap.getKey();

                    // If Student, get registered lectures
                    if(myUserInfo == 0){
                        findMyRegisteredLectureFromFirebaseDB();

                        if(myLIDList.contains(LID)) {
                            list.add(new LectureListItem(snap.child("name").getValue().toString(),
                                    "학점: " + snap.child("credit").getValue().toString() +
                                            " / 학생정원: " + snap.child("max participant").getValue().toString(), LID));

                            recyclerView.setAdapter(new RecyclerAdapter(list));
                        }
                    }
                    // If Professor, get my lectures
                    else if(myUserInfo == 1){

                        if(myLIDList.contains(LID)) {
                            list.add(new LectureListItem(snap.child("name").getValue().toString(),
                                    "학점: " + snap.child("credit").getValue().toString() +
                                            " / 학생정원: " + snap.child("max participant").getValue().toString(), LID));

                            recyclerView.setAdapter(new RecyclerAdapter(list));
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


    // Check User : Professor or Student
    private void checkUserPosition() {
        final String myUID = mAuth.getCurrentUser().getUid();

        dbReference = fbDatabase.getReference("Users");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap :snapshot.getChildren()) {
                    if(myUID.equals(snap.getKey())) {
                        if(snap.child("user position").getValue().toString().equals("학생"))
                        {
                            myUserInfo = 0;
                        }
                        else {
                            myUserInfo = 1;
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






}