package com.example.lmsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentProfile extends AppCompatActivity {

    //defining item variable
    EditText txtStudentID, txtStudentUsername, txtStudentEmail, txtStudentContact, txtStudentPassword;
    Button btnEdit;
    DatabaseReference dbref;
    int counter;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        //mapping the variables to the widgets
        txtStudentID = (EditText)findViewById(R.id.txtStudentID);
        txtStudentUsername = (EditText)findViewById(R.id.txtStudentUsername);
        txtStudentEmail = (EditText)findViewById(R.id.txtStudentEmail);
        txtStudentContact = (EditText)findViewById(R.id.txtStudentContact);
        txtStudentPassword = (EditText)findViewById(R.id.txtStudentPassword);
        btnEdit = (Button)findViewById(R.id.btnEdit);

        user = new User();

        // connection with the real-time database
        dbref = FirebaseDatabase.getInstance().getReference().child("User");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    counter = (int)dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //to obtain the data by type
        Query checkType = dbref.orderByChild("type").equalTo("Student");

        checkType.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    txtStudentID.setText(dataSnapshot.child("U1").child("name").getValue().toString());
                    txtStudentUsername.setText(dataSnapshot.child("U1").child("userName").getValue().toString());
                    txtStudentEmail.setText(dataSnapshot.child("U1").child("email").getValue().toString());
                    txtStudentContact.setText(dataSnapshot.child("U1").child("contact").getValue().toString());
                    txtStudentPassword.setText(dataSnapshot.child("U1").child("password").getValue().toString());
                }

                else {
                    Toast.makeText(getApplicationContext(), "no data source available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}