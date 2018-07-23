package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.icode.yokalodge.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    //an instance of the Firebase Authentication class
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.hide();
        }

        //instantiation of the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart(){
        super.onStart();
        //checks if the user is signed and updates the UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUi(currentUser);
    }


    //method to be called when the login Button is clicked or tapped
    public void onLoginButtonClick(View view){
        //initialization of the instance of the ProgressDialog
        progressDialog = ProgressDialog.show(LoginActivity.this,"Logging In",null,true,true);
        progressDialog.setMessage("Please wait..."); //set a message on the progressDialog

        //progressDialog.setContentView(R.layout.material_design_progressdialog);
        //progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progressDialog.dismiss();
                timer.cancel();
            }
        },10000);

        Toast.makeText(LoginActivity.this,"You have successfully logged in",Toast.LENGTH_SHORT).show();
        Intent intentLogin = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intentLogin);
    }

    //method called when the link to the SignUp Activity is clicked or tapped
    public void onSignUpLinkClick(View View){
        //starts the Sign Up Activity
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));

    }

    public void onAdminLoginButtonLinkClick(View view){
        //starts the AdminLoginActivity
        startActivity(new Intent(LoginActivity.this,AdminLoginActivity.class));
    }

}
