package com.example.lmsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateStudentProfile extends AppCompatActivity {

    //defining variables
    EditText name, contact;
    Button edit;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    String userID;

    //Methods to clear all inputs
    public void clearControls() {
        name.setText("");
        contact.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_profile);

        // mapping variables to the items
        name = findViewById(R.id.editFullName);
        contact = findViewById(R.id.editContact);
        edit = findViewById(R.id.edit);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating..");

        //instantiating
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullname = name.getText().toString().trim();
                final String contactNo = contact.getText().toString().trim();

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Full name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(contact.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Contact", Toast.LENGTH_SHORT).show();
                }

                else {
                    progressDialog.show();


                    userID = firebaseAuth.getCurrentUser().getUid();
                    final String userEmail = firebaseAuth.getCurrentUser().getEmail();
                    DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", fullname);
                    user.put("Contact", contactNo);

                    documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateStudentProfile.this, "Successfully updated: " + userEmail, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Intent to Student profile
                    startActivity(new Intent(getApplicationContext(), StudentProfile.class));
                }
                clearControls();  //method to clear the inputs
            }
        });

    }
}