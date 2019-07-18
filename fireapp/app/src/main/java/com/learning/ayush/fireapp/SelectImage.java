package com.learning.ayush.fireapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SelectImage extends Activity {
    private Button imgSelectBtn;
    private StorageReference mStorage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image);

        mStorage= FirebaseStorage.getInstance().getReference();
        imgSelectBtn= findViewById(R.id.galleryBtn);

    }
}
