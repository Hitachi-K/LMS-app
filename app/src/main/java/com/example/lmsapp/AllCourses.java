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

public class AllCourses extends AppCompatActivity {

    TextView FullName;
    FirebaseAuth fbAuth;
    FirebaseFirestore firebaseFirestore;
    ChipNavigationBar buttonNah;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        //Mapping the variables to the items
        FullName = (TextView)findViewById(R.id.txtFullName);
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
                        startActivity(new Intent(getApplicationContext(), StudentProfile.class));
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



    /*public void changeFragment(View view){
        Fragment fragment;
        if (view == findViewById(R.id.btnAllCourses)){
            fragment = new all_courses();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment,fragment);
            ft.commit();
        }
        if (view == findViewById(R.id.btnMyCourses)){
            fragment = new my_courses() ;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment,fragment);
            ft.commit();
        }
    }*/
}