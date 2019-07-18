package com.learning.ayush.fireapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {
    private Firebase mFirebase;
    private EditText emailText;
    private EditText passwordText;
    private Button loginbutton;
    private Button nextButton;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://fir-2-783da.firebaseio.com/users/"); //firebase

        emailText= findViewById(R.id.email);
        passwordText= findViewById(R.id.password);
        loginbutton= findViewById(R.id.Login);
        nextButton= findViewById(R.id.nextBtn);



        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              ////////// To store the data in firebase///////////////////
                String email= emailText.getText().toString();
                String password=String.valueOf(passwordText.getText());
                Firebase childOfFirebase = mFirebase.child(email);
                Firebase pwd= childOfFirebase.child("Password");
                pwd.setValue(password);
                if(isNetworkAvailable()== false)
                    Toast.makeText(MainActivity.this,"Poor Connection..!!",Toast.LENGTH_SHORT).show();
                else   Toast.makeText(MainActivity.this,"Successfull !!",Toast.LENGTH_SHORT).show();


            }



        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,RetrieveClass.class);
                startActivity(intent);

            }
        });
    }
}
