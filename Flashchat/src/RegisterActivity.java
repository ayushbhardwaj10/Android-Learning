package com.ab.flashchatnewfirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    // Constants
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    // Firebase instance variables
   private FirebaseAuth mAuth;   // Required for User Authorization to firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        // Get hold of an instance of FirebaseAuth
      mAuth= FirebaseAuth.getInstance();

    }

    // Executed when Sign Up button is pressed.
    public void signUp(View v)
    {
        attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //  Call create FirebaseUser() here
             createFirebaseUser();
        }
    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        String confirmPassword= mConfirmPasswordView.getText().toString();
        return confirmPassword.equals(password) && password.length()>4 ;
    }

    //  Create a Firebase user
   private void createFirebaseUser()  // This method will help to create a new user on firebase server
   {
       String email = mEmailView.getText().toString();
       String password = mPasswordView.getText().toString();
       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //CreateUserWithEmailAndPassword returns a Task Type Object.
           @Override // Executed when user creation task is completed, now it can either be a success or a failure.
           public void onComplete(@NonNull Task<AuthResult> task) {
               Log.d("FlashChat", "CreateUser Complete !" + task.isSuccessful());
               if (!task.isSuccessful())             // task.isSuccessful can be either 0 or 1 based on if the user is created successfuly in Firebase
               {
                   Log.d("FlashChat", "User Creating Failed");
                   showErrorDialog("Registration Attempt Failed");
               } else   // Creation of user in Firebase successfull here
                   {
                     saveDisplayName(); // Create the display name for the user only if CreateUser is Successful in firebase
                     // Now the user is registered to the app and we have to change the activity to login activity
                       Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                       finish();
                       startActivity(intent);
                   }

           }
       });
   }


    //  Save the display name to Shared Preferences
      public void saveDisplayName()
      {
          String displayName= mUsernameView.getText().toString();
          SharedPreferences prefs = getSharedPreferences(CHAT_PREFS,0); // Chat_PREFS is random string name given by us and mode :0 is default value.
          prefs.edit().putString(DISPLAY_NAME_KEY,displayName).apply();       // Object gets ready to accept some datd by edit(), DISPLAY_NAME_KEY is any key name provided by us for the actual displayname string.
                                                                              // . apply() will commit the data and save the information to the device
      }



    // Create an alert dialog to show in case registration failed
    private void showErrorDialog(String message)
    { new AlertDialog.Builder(this)
            .setTitle("Oops")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok,null)
            .setIcon(android.R.drawable.ic_dialog_alert)   // android.R means resources of android inbuilt not from the local resources.
            .show();

    }



}
