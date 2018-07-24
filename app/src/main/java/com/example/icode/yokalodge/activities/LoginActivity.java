package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Users;
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

    ProgressDialog progressDialog;

    private FirebaseDatabase dB;
    private DatabaseReference dbRef;
    private Users users;

    //Email and password EditText
    private AppCompatEditText appCompatEditTextEmail;
    private AppCompatEditText appCompatEditTextPassword;

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
        dB = FirebaseDatabase.getInstance();
        dbRef = dB.getReference().child("Users");

        appCompatEditTextEmail = findViewById(R.id.appCompatEditTextEmail);
        appCompatEditTextPassword = findViewById(R.id.appCompatEditTextPassword);


    }




    //method to be called when the login Button is clicked or tapped
    public void onLoginButtonClick(View view){

        String error_field = "This field cannot be left blank";

        String email = appCompatEditTextEmail.getText().toString().trim();
        String password = appCompatEditTextPassword.getText().toString().trim();

        //checks if text entered are valid and textfields are not empty
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            appCompatEditTextEmail.setError("Invalid Email Address...Please enter a valid email address");
            Toast.makeText(LoginActivity.this,"Invalid Email Address...Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }
        else if(Patterns.EMAIL_ADDRESS.matcher("").matches()){
            appCompatEditTextEmail.setError(error_field);
            Toast.makeText(LoginActivity.this,"Email is a required field",Toast.LENGTH_SHORT).show();
        }
        else if(password.equalsIgnoreCase("")){
            appCompatEditTextPassword.setError(error_field);
            Toast.makeText(LoginActivity.this,"Password is a required field", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher("").matches() && password.equalsIgnoreCase("")){
            Toast.makeText(LoginActivity.this,"Email and Password are required fields",Toast.LENGTH_SHORT).show();
        }
        else{
            loginUser();
        }

    }

    //method for logging user into the system
    public void loginUser(){

        //get the text from the EditText
        String email = appCompatEditTextEmail.getText().toString().trim();
        final String password = appCompatEditTextPassword.getText().toString().trim();

        //initialization of the instance of the ProgressDialog
        progressDialog = ProgressDialog.show(LoginActivity.this,"",null,true,true);
        progressDialog.setMessage("Please wait..."); //set a message on the progressDialog

        //progressDialog.setContentView(R.layout.material_design_progressdialog);
        //progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dbRef.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets a snapshot of the data in the database
                if(dataSnapshot.exists()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    //checks if password entered equals the one in the database
                    if(password.equals(users.getPassword())){
                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                timer.cancel();
                            }
                        }, 10000);

                        Toast.makeText(LoginActivity.this, "You have successfully logged in", Toast.LENGTH_SHORT).show();
                        Intent intentLogin = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intentLogin);
                    }
                    else{
                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                timer.cancel();
                            }
                        },5000);
                    }
                    Toast.makeText(LoginActivity.this,"Incorrect Email or Password",Toast.LENGTH_LONG).show();
                }
                else{

                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            timer.cancel();
                        }
                    },5000);
                    Toast.makeText(LoginActivity.this,"User does not exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, databaseError.toException().toString(),Toast.LENGTH_LONG).show();

            }
        });



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
