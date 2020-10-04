package com.example.lmsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewContent extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    RecyclerView mRecyclerView;
    ArrayList<DownModel> downModelArrayList = new ArrayList<>();
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);

        firebaseFirestore = FirebaseFirestore.getInstance();

         setUpRV();
         setUpFB();
         dataFromFirebase();
    }

    private void dataFromFirebase() {
        if (downModelArrayList.size() > 0) {
           downModelArrayList.clear();

           //firebaseFirestore = FirebaseFirestore.getInstance();

           firebaseFirestore.collection("Files").get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           for(DocumentSnapshot documentSnapshot: task.getResult()) {

                               DownModel downModel = new DownModel(documentSnapshot.getString("name"),
                                       documentSnapshot.getString("URI"));
                               downModelArrayList.add(downModel);
                           }

                           myAdapter = new MyAdapter(ViewContent.this, downModelArrayList);
                           mRecyclerView.setAdapter(myAdapter);
                       }
                   })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ViewContent.this, "Error :-", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void setUpFB() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setUpRV() {
        mRecyclerView = findViewById(R.id.recycle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}