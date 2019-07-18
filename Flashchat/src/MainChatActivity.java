package com.ab.flashchatnewfirebase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference; // isused to read or write data at a referenced position.
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // TODO: Set up the display name and get the Firebase reference
         setDisplayName();

         mDatabaseReference = FirebaseDatabase.getInstance().getReference(); // Databasereference reference to a database location in firebase cloud
        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        // TODO: Send the message when the "enter" button is pressed
        mInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        // TODO: Add an OnClickListener to the sendButton to send a message
      mSendButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              sendMessage();
          }
      });
    }

    // TODO: Retrieve the display name from the Shared Preferences
       private void setDisplayName(){
           SharedPreferences prefs= getSharedPreferences(RegisterActivity.CHAT_PREFS,0);
           mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY,null);  // Passing the Display name key to get the specific username of every user which is saved in his device(as preferences)
           if(mDisplayName== null)
               mDisplayName= "Anonymous";
       }

    private void sendMessage()
    {
        Log.d("FlashChat","I sent the message");

        //  Grab the text the user typed in and push the message to Firebase
        String input = mInputText.getText().toString();
        if(!TextUtils.isEmpty(input)){
            InstantMessage chat= new InstantMessage(input,mDisplayName);
            mDatabaseReference.child("message").push().setValue(chat); // Child is message means the data is stored in a cloud location called message.
           // It takes argument as a class object whose data members it stores in cloud. Thats why we created a new class to store values.
            mInputText.setText(""); // Make the input text as empty again for user to enter more msgs.
        }

    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.
    @Override
    public void onStart(){  // Executes by OS after OnCreate() method.
        super.onStart();
        mAdapter = new ChatListAdapter(this,mDatabaseReference,mDisplayName);
       mChatListView.setAdapter(mAdapter);   // Listview now know what to display through the adapter provided.
    }

    @Override
    public void onStop() {
        super.onStop();     // Called when user no longer van see the app
        // Remove the Firebase event listener on the adapter.
        mAdapter.cleanUp();

    }

}
