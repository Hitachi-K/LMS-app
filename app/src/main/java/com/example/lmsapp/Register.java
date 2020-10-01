package com.example.lmsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    //defining item variables
    Button btnCancel, btnRegister;
    EditText regName, regEmail, regUsername, regContact, regPassword;
    CheckBox checkStudent, checkTeacher;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID, userEmail, type;

    //Progress dialog
    ProgressDialog progressDialog;

    //Methods to clear all inputs
    public void clearControls() {
        regName.setText("");
        regEmail.setText("");
        regContact.setText("");
        regContact.setText("");
        regPassword.setText("");
        checkStudent.setChecked(false);
        checkTeacher.setChecked(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //mapping the variables to the items
        regName = (EditText)findViewById(R.id.txtRegName);
        regContact = (EditText)findViewById(R.id.txtRegContact);
        regEmail = (EditText)findViewById(R.id.txtRegEmail);
        regPassword = (EditText)findViewById(R.id.txtRegPassword);
        btnRegister= (Button)findViewById(R.id.btnRegister);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        checkStudent = (CheckBox)findViewById(R.id.checkBoxStudent);
        checkTeacher = (CheckBox)findViewById(R.id.checkBoxTeacher);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering..");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Defining values
        final String checkStudentValue = "Student";
        final String checkTeacherValue = "Teacher";


        //Enabling on-click for button register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // declared variables for email and password
                final String email = regEmail.getText().toString().trim();
                final String password = regPassword.getText().toString().trim();
                final String name = regName.getText().toString().trim();
                final long contact = Long.parseLong(regContact.getText().toString().trim());

                try {

                    // validation
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        regEmail.setError("Invalid Email");
                        regEmail.setFocusable(true);
                    }
                    else if (TextUtils.isEmpty(regName.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter your name", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(regEmail.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter your email", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(regContact.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please enter your Contact", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(regPassword.getText().toString())) {
                        Toast.makeText(getApplicationContext(),"Please Enter Password", Toast.LENGTH_SHORT).show();
                    }
                    else if (regPassword.length()<8) {
                        regPassword.setError("Password minimum 8 characters required");
                        regPassword.setFocusable(true);
                    }  //end of validation

                    else {

                        //Storing data by type Student
                        if (checkStudent.isChecked()) {
                            final String typeStudent = checkStudentValue;

                            progressDialog.show();

                            // Registering method
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(Register.this,new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();

                                                userID = firebaseAuth.getCurrentUser().getUid();
                                                userEmail = firebaseAuth.getCurrentUser().getEmail();
                                                DocumentReference docRef = firebaseFirestore.collection("Users").document(userID);
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("Name", name);
                                                user.put("Contact", contact);
                                                user.put("Email", email);
                                                user.put("Password", password);
                                                user.put("Type", typeStudent);

                                                docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("TAG", "onSuccess: Registered " + userID);
                                                        Toast.makeText(Register.this, "Successfully registered: " + userEmail, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //Intent to Student Home Page
                                                    startActivity(new Intent(getApplicationContext(), StudentHomePage.class));
                                            }
                                            else {
                                                // If sign in fails, display a message to the user.
                                                progressDialog.dismiss();
                                                Toast.makeText(Register.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            clearControls();  //method to clear the inputs
                        }

                        // Storing data by type Teacher
                        if (checkTeacher.isChecked()) {
                            final String typeTeacher = checkTeacherValue;

                            progressDialog.show();

                            // Registering method
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(Register.this,new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();

                                                userID = firebaseAuth.getCurrentUser().getUid();
                                                userEmail = firebaseAuth.getCurrentUser().getEmail();
                                                DocumentReference docRef = firebaseFirestore.collection("Users").document(userID);
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("Name", name);
                                                user.put("Contact", contact);
                                                user.put("Email", email);
                                                user.put("Password", password);
                                                user.put("Type", typeTeacher);

                                                docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("TAG", "onSuccess: Registered " + userID);
                                                        Toast.makeText(Register.this, "Successfully registered: " + userEmail, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                // intent to Teacher home page
                                                    startActivity(new Intent(getApplicationContext(), TeacherHomePage.class));
                                            }
                                            else {
                                                // If sign in fails, display a message to the user.
                                                progressDialog.dismiss();
                                                Toast.makeText(Register.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            clearControls();  //method to clear the inputs
                        }

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