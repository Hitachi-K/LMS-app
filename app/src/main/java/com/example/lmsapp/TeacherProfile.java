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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class TeacherProfile extends AppCompatActivity {

    //Declaring variables
    TextView FullName,Email,Contact,Password;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    Button Logout, edit, delete;
    ChipNavigationBar buttonNah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        //mapping variables to items
        Logout = (Button)findViewById(R.id.btnLogout);
        buttonNah = findViewById(R.id.bottom_nav);
        edit = findViewById(R.id.btnEdit);
        FullName = findViewById(R.id.txtTeacherFullName);
        Email = findViewById(R.id.txtTeacherEmail);
        Contact = findViewById(R.id.txtTeacherContact);
        delete = findViewById(R.id.btnDelete);

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

        buttonNah.setItemSelected(R.id.profile, true);

        //on-selected item listener for the bottonNav
        buttonNah.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                switch (id) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),TeacherHomePage.class));

                        break;
                    case R.id.course:
                        startActivity(new Intent(getApplicationContext(), SubjectContent.class));

                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), TeacherProfile.class));

                        break;
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherProfile.this);
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

                                        Toast.makeText(TeacherProfile.this, "profile deleted", Toast.LENGTH_SHORT).show();;
                                        startActivity(new Intent(TeacherProfile.this, Register.class));
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

        // on click-listener to edit
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherProfile.this, UpdateStudentProfile.class);
                startActivity(intent);
            }
        });
    }
}