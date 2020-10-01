package com.example.lmsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class Login extends AppCompatActivity {

    //Declaring variables
    TextView txtRegister;
    EditText emailLogin, passwordLogin;
    Button Login;
    ProgressDialog progressDialog;
    FirebaseAuth fbAuth;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Mapping variables to the items
        txtRegister = (TextView)findViewById(R.id.txtRegister);
        emailLogin = (EditText)findViewById(R.id.loginEmail);
        passwordLogin = (EditText)findViewById(R.id.loginPassword);
        Login = (Button)findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in..");

        fbAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //on-click listener for button Login
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // getting inputs
                String email = emailLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                //validation
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailLogin.setError("Invalid Email");
                    emailLogin.setFocusable(true);
                }
                else if (TextUtils.isEmpty(emailLogin.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter your email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(passwordLogin.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if (passwordLogin.length()<8) {
                    passwordLogin.setError("Password minimum 8 characters required");
                    passwordLogin.setFocusable(true);
                }  //end of validation

                else {
                    progressDialog.show();

                    // logging in by email and password
                    fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                progressDialog.dismiss();
                                String userEmail = fbAuth.getCurrentUser().getEmail();
                                Toast.makeText(Login.this, "Successfully Logged in: " + userEmail, Toast.LENGTH_SHORT).show();
                            }

                            else{
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Logging in by type
                    CollectionReference usersRef = firebaseFirestore.collection("Users");
                    String userID = fbAuth.getCurrentUser().getUid();
                    usersRef.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String type = document.getString("Type");
                                    if(type.equals("Student")) {
                                        startActivity(new Intent(Login.this, StudentHomePage.class));
                                    } else if (type.equals("Teacher")) {
                                        startActivity(new Intent(Login.this, TeacherHomePage.class));
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        // on-click listener to text Register
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}