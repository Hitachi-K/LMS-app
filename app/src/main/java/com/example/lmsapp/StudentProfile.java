package com.example.lmsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class StudentProfile extends AppCompatActivity {

    //Declaring variables
    Button Logout;
    ChipNavigationBar buttonNah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        //mapping variables to items
        Logout = (Button)findViewById(R.id.btnLogout);
        buttonNah = findViewById(R.id.bottom_nav);

        buttonNah.setItemSelected(R.id.profile, true);

        //on-selected item listener for the bottonNav
        buttonNah.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switch (id) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),StudentHomePage.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.course:
                        startActivity(new Intent(getApplicationContext(), AllCourses.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), StudentProfile.class));
                        overridePendingTransition(0,0);
                        break;
                }
            }
        });

        // on-click listener for logout button
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

    }
}