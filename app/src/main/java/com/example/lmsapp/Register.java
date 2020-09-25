package com.example.lmsapp;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    //defining item variables
    Button btnCancel, btnRegister;
    EditText regName, regUsername, regContact, regPassword;
    CheckBox checkStudent, checkTeacher;
    DatabaseReference databaseReference;
    User user;

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
        regUsername = (EditText)findViewById(R.id.txtRegUsername);
        regPassword = (EditText)findViewById(R.id.txtRegPassword);
        btnRegister= (Button)findViewById(R.id.btnRegister);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        checkStudent = (CheckBox)findViewById(R.id.checkBoxStudent);
        checkTeacher = (CheckBox)findViewById(R.id.checkBoxTeacher);

        user = new User();

        //Enabling on-click for button register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

                try {
                    if (TextUtils.isEmpty(regName.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter your name", Toast.LENGTH_SHORT).show();
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
                    else {
                        user.setName(regName.getText().toString().trim());
                        user.setContact(Long.parseLong(regContact.getText().toString().trim()));
                        user.setUserName(regUsername.getText().toString().trim());
                        user.setPassword(regPassword.getText().toString().trim());
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