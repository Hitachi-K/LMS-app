package com.example.lmsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.google.firebase.firestore.Query;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Subjects extends AppCompatActivity {


    //Declaring variables
    private SubjectAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Subjects");
    TextView FullName;
    FirebaseAuth fbAuth;
    ChipNavigationBar buttonNah;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        setUpRecyclerView();

        FloatingActionButton buttonAddNote = findViewById(R.id.add_subject);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Subjects.this, NewSubjectActivity.class));
            }
        });

        //Mapping variables to items
        FullName = (TextView)findViewById(R.id.txtFullName);
        buttonNah = findViewById(R.id.bottom_nav);


        buttonNah.setItemSelected(R.id.course, true);

        //on-selected item listener for the bottonNav
        buttonNah.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
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
        db = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        //getting the User ID
        userID = fbAuth.getCurrentUser().getUid();



        //Obtaining the Full Name of the current user
        DocumentReference documentReference = db.collection("Users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                FullName.setText(value.getString("Name"));
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = notebookRef.orderBy("priority", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        adapter = new SubjectAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}