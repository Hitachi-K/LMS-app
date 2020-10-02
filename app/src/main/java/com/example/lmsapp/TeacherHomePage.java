package com.example.lmsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class TeacherHomePage extends AppCompatActivity {

    //Declaring Variables
    TextView FullName;
    Button subjects, schedule, attendance, ExamReports, DiscussionPage;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth fbAuth;
    ChipNavigationBar buttonNah;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home_page);

        //mapping variables to items
        FullName = (TextView)findViewById(R.id.txtFullName);
        subjects = (Button)findViewById(R.id.btnTeacherMySubjects);
        schedule = (Button)findViewById(R.id.btnTeacherSchedule);
        attendance = (Button)findViewById(R.id.btnAttendance);
        ExamReports = (Button)findViewById(R.id.btnTeacherExamReports);
        DiscussionPage = (Button)findViewById(R.id.btnTeacherDiscussion);
        buttonNah = findViewById(R.id.bottom_nav);

        buttonNah.setItemSelected(R.id.home, true);

        //on-selected item listener for the bottonNav
        buttonNah.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switch (id) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),TeacherHomePage.class));
                        break;
                    case R.id.course:
                        startActivity(new Intent(getApplicationContext(), Subjects.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), TeacherProfile.class));
                        break;
                }
            }
        });

        //Instantiating
        firebaseFirestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        //getting the User ID
        userID = fbAuth.getCurrentUser().getUid();

        //Obtaining the Full Name of the current user
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                FullName.setText(value.getString("Name"));
            }
        });

        subjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSubject = new Intent(TeacherHomePage.this, Subjects.class);
                startActivity(intentSubject);
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(TeacherHomePage.this, Attendance.class);
                startActivity(intentProfile);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSchedule = new Intent(TeacherHomePage.this, Schedules.class);
                startActivity(intentSchedule);
            }
        });

        ExamReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentExam = new Intent(TeacherHomePage.this, ExamReport.class);
                startActivity(intentExam);
            }
        });

        DiscussionPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDiscussion = new Intent(TeacherHomePage.this, DiscussionTeacher.class);
                startActivity(intentDiscussion);
            }
        });
    }
}