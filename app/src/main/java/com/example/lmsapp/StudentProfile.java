package com.example.lmsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class StudentProfile extends AppCompatActivity {

    //Declaring variables
    TextView FullName,Email,Contact,Password;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    Button Logout, buttonEdit, delete;
    ChipNavigationBar buttonNah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        //mapping variables to items
        Logout = (Button)findViewById(R.id.btnLogout);
        buttonNah = findViewById(R.id.bottom_nav);
        buttonEdit = findViewById(R.id.btnEdit);
        FullName = findViewById(R.id.txtStudentFullName);
        Email = findViewById(R.id.txtStudentEmail);
        Contact = findViewById(R.id.txtStudentContact);
        delete = findViewById(R.id.btnDelete);

        buttonNah.setItemSelected(R.id.profile, true);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fstore.collection("Users").document(userId);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Email.setText(documentSnapshot.getString("Email"));
                Contact.setText(documentSnapshot.getString("Contact"));
                FullName.setText(documentSnapshot.getString("Name"));

            }
        });

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfile.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete profile ");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DocumentReference documentReference = fstore.collection("Users").document(userId);

                        documentReference.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(StudentProfile.this, "profile deleted", Toast.LENGTH_SHORT).show();;
                                        startActivity(new Intent(StudentProfile.this, Register.class));
                                        finish();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog;
                alertDialog = builder.create();
                alertDialog.show();

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

        //on-click listener for edit
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this, UpdateStudentProfile.class);
                startActivity(intent);
            }
        });
    }
}