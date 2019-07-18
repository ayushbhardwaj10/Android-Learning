package com.ab.flashchatnewfirebase;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private FirebaseAuth mAuth;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {    // id is obtained when password is written in EditText and it's compared with R.integer.login to check if the password is entered.
                    attemptLogin(); // it's involed when user presses "ENTER" in  the phone.
                    return true;
                }
                return false;
            }
        });

        // TODO: Grab an instance of FirebaseAuth
        mAuth=FirebaseAuth.getInstance();

    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        attemptLogin() ;

    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.ab.flashchatnewfirebase.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    // Complete the attemptLogin() method
    private void attemptLogin()
    { String Email= mEmailView.getText().toString();
      String Password= mPasswordView.getText().toString();

       if(TextUtils.isEmpty(Email))
        {  mEmailView.setError("Email is emplty");
         return;
        }
        if(TextUtils.isEmpty((Password))){
          mPasswordView.setError("Password is empty");
          return;
        }
      else
          {
          Toast.makeText(this, "Login in progress",Toast.LENGTH_SHORT).show();
      }

        // TODO: Use FirebaseAuth to sign in with email & password
    mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {   //CreateUserWithEmailAndPassword returns a Task Type Object
        @Override  // Executed when sign-in task is completed, now it can either be a success or a failure.
        public void onComplete(@NonNull Task<AuthResult> task) {
            Log.d("FlashChat","SignIn with Email Complete"+ task.isSuccessful());

            if(!task.isSuccessful())
            {
                Log.d("FlashChat","Problem Signing in :" + task.getException());
                showErrorDialogue("There was a problem signing in");
            }
            // Login successfull here.. Now taking user to main chat activity
            else { Intent intent = new Intent(LoginActivity.this, MainChatActivity.class);
                  finish();    // Taking the user to main chat connected to firebase database.
                  startActivity(intent);

            }

        }
    });

    }

    // Show error on screen with an alert dialog
     public void showErrorDialogue(String message){
         new AlertDialog.Builder(this)
                 .setTitle("Oops")
                 .setMessage(message)
                 .setPositiveButton(android.R.string.ok,null)
                 .setIcon(android.R.drawable.ic_dialog_alert)   // android.R means resources of android inbuilt not from the local resources.
                 .show();

     }

}

