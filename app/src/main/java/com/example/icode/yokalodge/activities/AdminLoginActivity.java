package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;

    //instance of the Admin class
    private Admin admin;

    //database objects
    private FirebaseDatabase admindB;
    private DatabaseReference adminRef;

    //an instance of the ProgressDialog Class
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //gets an instance of the ActionBar
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.hide();
        }

        //get reference to the EditText fields defined in the xml file
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        //instantiation of the Database objects
        admindB = FirebaseDatabase.getInstance();
        adminRef = admindB.getReference().child("Admin");


    }

    //login button method for admin
    public void onLoginButtonClick(View view){

        //string for error handling
        String error_field = "This field cannot be left blank";
        String password_length = "Password length cannot be less than 6";

        //get text from the EditText fields
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //checks if text entered are valid and textfields are not empty
        if(username.equals("")){
            editTextUsername.setError(error_field);
            //Toast.makeText(LoginActivity.this,"Username is a required field",Toast.LENGTH_SHORT).show();
        }
        else if(password.equals("")){
            editTextPassword.setError(error_field);
            //Toast.makeText(LoginActivity.this,"Password is a required field", Toast.LENGTH_SHORT).show();
        }
        else if(password.length() < 6){
            editTextPassword.setError(password_length);
        }
        else if(username.equals("") && password.equals("")){
            Toast.makeText(AdminLoginActivity.this,"Email and Password are required fields",Toast.LENGTH_SHORT).show();
        }
        else{
            loginAdmin();
        }

    }

    //method for logging Admin into the system
    public void loginAdmin(){

        progressDialog = ProgressDialog.show(AdminLoginActivity.this,"",null,true,true);
        progressDialog.setMessage("Please wait...");

        //getting input from the editText
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        admin.setUser_name(username);
        admin.setPassword(password);

        adminRef.child(admin.getUsername()).setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    //runs the progress Dialog for a limited time frame and dismisses it
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            timer.cancel();
                        }
                    },5000);
                    clearBothTextFields();
                    Toast.makeText(AdminLoginActivity.this, "You have Logged In Successfully", Toast.LENGTH_LONG).show();
                }
                else{
                    //runs the progress Dialog for a limited time frame and dismisses it
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            timer.cancel();
                        }
                    },5000);
                    clearBothTextFields();
                    Toast.makeText(AdminLoginActivity.this,"Cannot connect to the database",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    // clears the Username and Password EditText
    public void clearBothTextFields(){
        editTextUsername.setText(null);
        editTextPassword.setText(null);
    }

    public void clearPasswordTextField(){
        editTextPassword.setText(null);
    }

    //link to the user login interface
    public void onLoginButtonLinkClick(View view){
        //starts the Users LoginActivity
        startActivity(new Intent(AdminLoginActivity.this,LoginActivity.class));
    }
}
