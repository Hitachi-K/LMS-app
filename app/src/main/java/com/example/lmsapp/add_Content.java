package com.example.lmsapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class add_Content extends Fragment {

    //declaring variables
    Button btnUpload;
    ImageButton selectItem;
    TextView status;
    String userID;
    Uri pdfUri; //uri that's meant for local storage

    //permissions constants
    private static final int STORAGE_REQUEST_CODE = 100;
    private static final int PDF_ACTIVITY_FOR_RESULT = 200;

    //Progress dialog
    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;


    public add_Content() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add__content, container, false);
        //mapping variables to the items
        selectItem = view.findViewById(R.id.selectFile);
        btnUpload = view.findViewById(R.id.btnUpload);
        status = view.findViewById(R.id.status);

        selectItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                {
                    selectPdf();
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE );
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfUri!=null) { //user has successfully selected
                    uploadFile(pdfUri);
                }
                else {
                    Toast.makeText(getActivity(), "Select a File", Toast.LENGTH_SHORT).show();
                }
            }

        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    private void uploadFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading..");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference = firebaseStorage.getReference(); //returns path

        storageReference.child("Uploads").child(fileName).putFile(pdfUri)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String url = taskSnapshot.getUploadSessionUri().toString(); //return the url of the uploaded file
                // Store the uri in the database
                userID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = firebaseFirestore.collection("Files").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("URI", url);

                documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(), "File successfully uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else {
                            Toast.makeText(getActivity(), "File upload unsuccessful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "File not successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                // track the progress of our upload.
                int currentProgress = (int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }

    private void selectPdf() {
        // to offer user to select a file using file manager
        // we will be using an intent

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files

        startActivityForResult(intent, PDF_ACTIVITY_FOR_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // check whether user has selected a file or not (ex:pdf)

        if (requestCode == PDF_ACTIVITY_FOR_RESULT && resultCode== RESULT_OK && data!=null)
        {
            pdfUri = data.getData(); //return the uri of the selected file
            status.setText("A file is selected: "+ data.getData().getLastPathSegment());
        }
        else {
            Toast.makeText(getActivity(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*This method is called when user press allow or deny from permissions request dialog
        here we will handle the permission cases (Allowed & denied)
         */
        if (requestCode == STORAGE_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else {
            Toast.makeText(getActivity(), "Please give Storage access permission", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}