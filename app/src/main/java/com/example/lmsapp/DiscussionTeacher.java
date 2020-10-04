package com.example.lmsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DiscussionTeacher extends AppCompatActivity {

    //Declaring variables
    TextView FullName;
    FirebaseAuth fbAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    ChipNavigationBar bottonNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_teacher);

        //Mapping variables to items
        FullName = (TextView)findViewById(R.id.txtFullName);
        bottonNav = findViewById(R.id.bottom_nav);

        //on-selected item listener for the bottonNav
        bottonNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
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
    }
}