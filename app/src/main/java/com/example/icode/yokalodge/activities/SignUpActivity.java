package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class SignUpActivity extends AppCompatActivity {

    //an instance of the Firebase Authentication class
    private FirebaseAuth mAuth;

    //instance variable of the FirebaseReference and DatabaseReference
    private FirebaseDatabase dB;
    private DatabaseReference dbRef;

    //an instance of the User class
    private Users users;

    //instance of the ProgressDialog class
    ProgressDialog progressDialog;

    //instance variable of the various views
    private AppCompatEditText appCompatEditTextEmail;
    private AppCompatEditText appCompatEditTextPassword;
    private AppCompatEditText appCompatEditTextConfirmPassword;

    private AppCompatEditText appCompatEditTextMobileNumber;

    private AppCompatSpinner appCompatSpinnerGender;
    private ArrayAdapter<CharSequence> arrayAdapterGender;

    private AppCompatButton appCompatButton;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.hide();
        }
        
        appCompatEditTextEmail = findViewById(R.id.appCompatEditTextEmail);
        appCompatEditTextPassword = findViewById(R.id.appCompatEditTextPassword);
        appCompatEditTextConfirmPassword = findViewById(R.id.appCompatEditTextConfirmPassword);
        appCompatEditTextMobileNumber = findViewById(R.id.appCompatEditTextMobileNumber);
        appCompatButton = findViewById(R.id.appCompatButtonLogin);

        //spinner or drop down view and its arrayAdapter instantiation
        appCompatSpinnerGender = findViewById(R.id.spinnerGender);
        arrayAdapterGender = ArrayAdapter.createFromResource(this,R.array.gender,R.layout.spinner_item);
        arrayAdapterGender.setDropDownViewResource(R.layout.spinner_dropdown_item);
        appCompatSpinnerGender.setAdapter(arrayAdapterGender);

        //getting the instances
        users = new Users();
        dB = FirebaseDatabase.getInstance();
        dbRef = dB.getReference().child("Users");

        //instantiation of the FirebaseAuth instance
        //mAuth = FirebaseAuth.getInstance();
    }

    //method for the sign Up button
    public void onSignUpButtonClick(View view){

        //get the text from the EditText
        String email = appCompatEditTextEmail.getText().toString().trim();
        String password = appCompatEditTextPassword.getText().toString().trim();
        String confirm_password = appCompatEditTextConfirmPassword.getText().toString().trim();
        String mobile_number = appCompatEditTextMobileNumber.getText().toString().trim();
        String gender = appCompatSpinnerGender.getSelectedItem().toString().trim();

        String error_field = "This field cannot be left blank";

        //checks the field to make sure they are not empty before user logs in
        if(email.equalsIgnoreCase("")){
            //appCompatButton.setEnabled(false);
            appCompatEditTextEmail.setError(error_field);
            Toast.makeText(SignUpActivity.this, "Email is a required field",Toast.LENGTH_SHORT).show();
        }
        else if(password.equalsIgnoreCase("")){
            //appCompatButton.setEnabled(false);
            appCompatEditTextPassword.setError(error_field);
            Toast.makeText(SignUpActivity.this,"Password is a required field",Toast.LENGTH_SHORT).show();
        }
        else if(confirm_password.equalsIgnoreCase("")){
            //appCompatButton.setEnabled(false);
            appCompatEditTextConfirmPassword.setError(error_field);
            Toast.makeText(SignUpActivity.this,"Confirm Password is a required field",Toast.LENGTH_SHORT).show();
        }
        else if(!confirm_password.equalsIgnoreCase(password)){
            //appCompatButton.setEnabled(true);
            appCompatEditTextConfirmPassword.setError("Password does not match");
            Toast.makeText(SignUpActivity.this, "Password does not match",Toast.LENGTH_SHORT).show();
        }
        else if(!confirm_password.equalsIgnoreCase(password) && !password.equalsIgnoreCase(confirm_password)) {
            //appCompatButton.setEnabled(true);
            appCompatEditTextConfirmPassword.setError("Password does not match");
            Toast.makeText(SignUpActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
        }
        else if(mobile_number.equalsIgnoreCase("")){
            //appCompatButton.setEnabled(true);
            appCompatEditTextConfirmPassword.setError(error_field);
            Toast.makeText(SignUpActivity.this, "Mobile Number is a required field",Toast.LENGTH_SHORT).show();
        }
        else if(!email.equalsIgnoreCase("") && (!password.equalsIgnoreCase("")) ){
            //appCompatButton.setEnabled(true);
            signUpUser();
        }

        
    }

    //sign Up method
    public void signUpUser(){

        progressDialog = ProgressDialog.show(SignUpActivity.this,"Signing Up" , null,true,true);
        progressDialog.setMessage("Please wait...");

        //get text from the EditText fields
        String email = appCompatEditTextEmail.getText().toString().trim();
        String password = appCompatEditTextPassword.getText().toString().trim();
        String confirm_password = appCompatEditTextConfirmPassword.getText().toString().trim();
        String mobile_number = appCompatEditTextMobileNumber.getText().toString().trim();
        String gender = appCompatSpinnerGender.getSelectedItem().toString().trim();

        //get the values from the fields and sets them to that of the values in the database
        users.setEmail_address(email);
        users.setPassword(password);
        users.setConfirm_password(confirm_password);
        users.setMobile_number(mobile_number);
        users.setGender(gender);

        dbRef.child(users.getEmail_address()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            timer.cancel();
                        }
                    },5000);
                    // clears the textfields after a successful Sign Up
                    clearTextFields();
                    //creates and displays a toast
                    Toast.makeText(SignUpActivity.this, "You have Successfully Signed Up",Toast.LENGTH_SHORT).show();
                    //starts the home activity after a successful Sign Up
                    //startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
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
                    Toast.makeText(SignUpActivity.this, "",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //clear textfields
public void clearTextFields(){
        appCompatEditTextEmail.setText(null);
        appCompatEditTextPassword.setText(null);
        appCompatEditTextConfirmPassword.setText(null);
        appCompatEditTextMobileNumber.setText(null);
}
    

    
}
