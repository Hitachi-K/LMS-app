package com.example.lmsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    //defining item variables
    Button btnCancel, btnRegister;
    EditText regName, regEmail, regUsername, regContact, regPassword;
    CheckBox checkStudent, checkTeacher;
    DatabaseReference databaseReference;
    User user;
    int counter;

    //Methods to clear all inputs
    public void clearControls() {
        regName.setText("");
        regUsername.setText("");
        regContact.setText("");
        regPassword.setText("");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //mapping the variables to the items
        regName = (EditText)findViewById(R.id.txtRegName);
        regContact = (EditText)findViewById(R.id.txtRegContact);
        regEmail = (EditText)findViewById(R.id.txtRegEmail);
        regUsername = (EditText)findViewById(R.id.txtRegUsername);
        regPassword = (EditText)findViewById(R.id.txtRegPassword);
        btnRegister= (Button)findViewById(R.id.btnRegister);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        checkStudent = (CheckBox)findViewById(R.id.checkBoxStudent);
        checkTeacher = (CheckBox)findViewById(R.id.checkBoxTeacher);

        user = new User();

        //Defining values
        final String checkStudentValue = "Student";
        final String checkTeacherValue = "Teacher";

        //real-time database connection
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    counter = (int)dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Enabling on-click for button register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (TextUtils.isEmpty(regName.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter your name", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(regEmail.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter your email", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(regContact.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please enter your Contact", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(regUsername.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter UserName", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(regPassword.getText().toString())) {
                        Toast.makeText(getApplicationContext(),"Please Enter Password", Toast.LENGTH_SHORT).show();
                    }
                    else if (regPassword.length()<8) {
                        regPassword.setError("Password minimum 8 characters required");
                        regPassword.setFocusable(true);
                    }
                    else {
                        user.setName(regName.getText().toString().trim());
                        user.setEmail(regEmail.getText().toString().trim());
                        user.setContact(Long.parseLong(regContact.getText().toString().trim()));
                        user.setUserName(regUsername.getText().toString().trim());
                        user.setPassword(regPassword.getText().toString().trim());
                        databaseReference.child("U"+(counter + 1)).setValue(user);

                        if (checkStudent.isChecked()) {
                            user.setType(checkStudentValue);
                            databaseReference.child("U"+(counter + 1)).setValue(user);
                        }

                        if (checkTeacher.isChecked()) {
                            user.setType(checkTeacherValue);
                            databaseReference.child("U"+(counter + 1)).setValue(user);
                        }
                        Toast.makeText(getApplicationContext(),"Successfully Registered", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"Invalid Contact Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Enabling on-click for button Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCancel = new Intent(Register.this, StartingPage.class);
                startActivity(intentCancel);
            }
        });
    }
}