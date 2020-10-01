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

public class TeacherHomePage extends AppCompatActivity {

    //Declaring Variables
    TextView FullName;
    Button subjects, schedule, teacherProfile, ExamReports, DiscussionPage;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth fbAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home_page);

        //mapping variables to items
        FullName = (TextView)findViewById(R.id.txtTeacherName);
        subjects = (Button)findViewById(R.id.btnTeacherMySubjects);
        teacherProfile = (Button)findViewById(R.id.btnTeacherViewProfile);
        ExamReports = (Button)findViewById(R.id.btnExamReports);
        DiscussionPage = (Button)findViewById(R.id.btnDiscussionPage);

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

        teacherProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfile = new Intent(TeacherHomePage.this, TeacherHomePage.class);
                startActivity(intentProfile);
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
                Intent intentDiscussion = new Intent(TeacherHomePage.this, DiscussionPage.class);
                startActivity(intentDiscussion);
            }
        });
    }
}