package com.learning.ayush.fireapp;


import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class FireApp extends Application {
    Firebase firebase;

    public void onCreate() {
        super.onCreate();
        firebase.setAndroidContext(this);

    }
}

