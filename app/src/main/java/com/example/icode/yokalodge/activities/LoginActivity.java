package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    // global or class variables
    ProgressDialog progressDialog;

    ProgressBar progressBar;

    private FirebaseDatabase dB;
    private DatabaseReference dbRef;
    private Users users;

    private FirebaseAuth mAuth;

    //Email and password EditText
    private EditText editTextEmail;
    private EditText editTextPassword;

    NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // removes status bar and to make background fit Screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.hide();
        }

        //instantiation of the Database objects
        dB = FirebaseDatabase.getInstance();
        dbRef = dB.getReference().child("Users");

        //get reference to the EditText fields defined in the xml file
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        nestedScrollView = findViewById(R.id.nestedScrollView);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

    }

    //method to be called when the login Button is clicked or tapped
    public void onLoginButtonClick(View view){

        //get text from the EditText fields
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //checks if text entered are valid and textfields are not empty
        if(email.isEmpty()){
            editTextEmail.setError(getString(R.string.error_empty_email));
            editTextEmail.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(getString(R.string.email_invalid));
            editTextEmail.requestFocus();
            return;
        }
        else if(password.isEmpty()){
            editTextPassword.setError(getString(R.string.error_empty_password));
            editTextPassword.requestFocus();
            return;
        }
        else if(password.length() < 6){
            editTextPassword.setError(getString(R.string.error_password_length));
            editTextPassword.requestFocus();
            return;
        }
        else if(email.isEmpty() && password.isEmpty()){
            Toast.makeText(LoginActivity.this,"Email and Password are required fields",Toast.LENGTH_LONG).show();
        }
        else{
            loginUser();
        }

    }

    //method for logging user into the system
    public void loginUser(){

        //get text from the EditText fields
          String email = editTextEmail.getText().toString().trim();
          String password = editTextPassword.getText().toString().trim();

          progressBar.setVisibility(View.VISIBLE);

           /* //initialization of the instance of the ProgressDialog
            progressDialog = ProgressDialog.show(LoginActivity.this,"",null,true,true);
            progressDialog.setMessage("Please wait..."); //set a message on the progressDialog*/

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // display a successful login message
                                Toast.makeText(LoginActivity.this,getString(R.string.login_successful),Toast.LENGTH_SHORT).show();

                                clearBothTextFields();
                                // finishes this activity and starts a new one
                                LoginActivity.this.finish();
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            }
                            else{
                                // display a successful login message
                                Snackbar.make(nestedScrollView,task.getException().getMessage(),Snackbar.LENGTH_LONG).show();
                            }

                            progressBar.setVisibility(View.GONE);
                        }
                    });

    }

    //method called when the link to the SignUp Activity is clicked or tapped
    public void onSignUpLinkClick(View View){
        // finishes the current activity and open the Sign Up Activity
        LoginActivity.this.finish();
        //starts the Sign Up Activity
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));

    }

    //method called when the link to the SignUp Activity is clicked or tapped
    public void onAdminLoginButtonLinkClick(View view){
        // finishes the current activity and open the AdminLoginActivity Activity
        LoginActivity.this.finish();
        //starts the AdminLoginActivity
        startActivity(new Intent(LoginActivity.this,AdminLoginActivity.class));
    }

    // clears the Username and Password EditText
    public void clearBothTextFields(){
        editTextEmail.setText(null);
        editTextPassword.setText(null);
    }

    public void clearPasswordTextField(){
        editTextPassword.setText(null);
    }

    // method to reset user password
    public void onButtonResetPassword(View view) {
        // finishes the current activity and open the resetPassword Activity
        LoginActivity.this.finish();
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }
}
