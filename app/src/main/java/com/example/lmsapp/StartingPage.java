package com.example.lmsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartingPage extends AppCompatActivity {

    //Register Button
    Button buttonReg;

    //on-click text
    TextView txtLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_page);

        //mapping the variables to the buttons
        buttonReg = (Button)findViewById(R.id.btnRegister);
        txtLogin = (TextView)findViewById(R.id.txtViewLink1);

        //on-click method for register button
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartingPage.this, Register.class);
                startActivity(intent);
            }
        });

        //on-click method for textViewLogin
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(StartingPage.this, Login.class);
                startActivity(intent2);
            }
        });
    }
}