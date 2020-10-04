package com.example.lmsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class SubjectContent extends AppCompatActivity {

    //Declaring variables
    Button addContent, ViewContent;
    FirebaseAuth fbAuth;
    FirebaseFirestore firebaseFirestore;
    TextView FullName;
    ChipNavigationBar buttonNah;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_content);

        // mapping variables to the items
        FullName = (TextView)findViewById(R.id.txtFullName);
        addContent = findViewById(R.id.btnAddContent);
        ViewContent = findViewById(R.id.btnViewContent);
        buttonNah = findViewById(R.id.bottom_nav);

        buttonNah.setItemSelected(R.id.course, true);

        //on-selected item listener for the bottonNav
        buttonNah.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switch (id) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),StudentHomePage.class));

                        break;
                    case R.id.course:
                        startActivity(new Intent(getApplicationContext(), AllCourses.class));

                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), TeacherProfile.class));

                        break;
                }
            }
        });

        addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubjectContent.this, AddSubjectContent.class);
                startActivity(intent);
            }
        });

        ViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubjectContent.this, ViewContent.class);
                startActivity(intent);
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
    }

}