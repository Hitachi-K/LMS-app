package com.example.lmsapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.net.ConnectException;

public class add_Content extends Fragment {

    //declaring variables
    Button btnUpload;
    ImageButton selectItem;
    TextView status;
    String userID;

    //permissions constants
    private static final int STORAGE_REQUEST_CODE = 100;

    //arrays of permission to be requested
    String storagePermissions[];
    String readStoragePermissions[];

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
                    //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, );
                }
            }
        });

        userID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("Files").document(userID);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //init arrays of permission
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        readStoragePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        //init progress dialog
        progressDialog = new ProgressDialog(getActivity());

        return view;
    }

    private void selectPdf() {
    }

    private boolean checkStoragePermission() {
        //check of storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        //request runtime storage permission
        ActivityCompat.requestPermissions(getActivity(), readStoragePermissions, STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*This method is called when user press allow or deny from permissions request dialog
        here we will handle the permission cases (Allowed & denied)
         */
        switch (requestCode) {
            case STORAGE_REQUEST_CODE: {

                // picking from gallery, first check if storage permissions allowed or not
                if (grantResults.length>0) {
                    boolean readStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (readStorageAccepted) {
                        //permissions enabled
                        pickfromStorage();
                    }
                    else {
                        //permission denied
                        Toast.makeText(getActivity(), "Please enable storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void pickfromStorage() {
    }

}