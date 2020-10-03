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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import static android.app.Activity.RESULT_OK;

public class add_Content extends Fragment {

    //declaring variables
    Button btnUpload;
    ImageButton selectItem;
    TextView status;
    String userID;
    Uri pdfUri; //url thats meant for local storage

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

        userID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("Files").document(userID);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //init progress dialog
        progressDialog = new ProgressDialog(getActivity());

        return view;
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