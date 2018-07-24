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
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    //an instance of the Firebase Authentication class
    private FirebaseAuth mAuth;

    //instance variable of the FirebaseReference and DatabaseReference
    private FirebaseDatabase usersdB;
    private DatabaseReference usersRef;

    //an instance of the User class
    private Users users;

    //instance of the ProgressDialog class
    ProgressDialog progressDialog;

    //instance variables of the various views
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private EditText editTextMobileNumber;

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
        
        editTextUsername = findViewById(R.id.editTextUsername);
        //sets the input type for the email editText
        //appCompatEditTextEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextMobileNumber = findViewById(R.id.editTextMobileNumber);

        //spinner or drop down view and its arrayAdapter instantiation
        appCompatSpinnerGender = findViewById(R.id.spinnerGender);
        arrayAdapterGender = ArrayAdapter.createFromResource(this,R.array.gender,R.layout.spinner_item);
        arrayAdapterGender.setDropDownViewResource(R.layout.spinner_dropdown_item);
        appCompatSpinnerGender.setAdapter(arrayAdapterGender);

        //getting the instances
        users = new Users();
        usersdB = FirebaseDatabase.getInstance();
        usersRef = usersdB.getReference().child("Users");

    }


    //checks for a valid email

   /* public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }*/


    //method for the sign Up button
    public void onSignUpButtonClick(View view){

        //get the text from the EditText
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirm_password = editTextConfirmPassword.getText().toString().trim();
        String mobile_number = editTextMobileNumber.getText().toString().trim();
        //String gender = appCompatSpinnerGender.getSelectedItem().toString().trim();

        //string for error handling
        String error_field = "This field cannot be left blank";
        String password_length = "Password length cannot be less than 6";

        /*checks the field to make sure they are not
        ** empty before user logs in and of accurate number of characters
        */
        if(username.equalsIgnoreCase("")){
            //appCompatButton.setEnabled(false);
            editTextUsername.setError(error_field);
            //Toast.makeText(SignUpActivity.this, "Username is a required field",Toast.LENGTH_SHORT).show();
        }
        else if(password.equalsIgnoreCase("")){
            //appCompatButton.setEnabled(false);
            editTextPassword.setError(error_field);
            //Toast.makeText(SignUpActivity.this,"Password is a required field",Toast.LENGTH_SHORT).show();
        }
        else if(password.length() < 6 ){
            editTextPassword.setError(password_length);
        }
        else if(confirm_password.equalsIgnoreCase("")){
            //appCompatButton.setEnabled(false);
            editTextConfirmPassword.setError(error_field);
            //Toast.makeText(SignUpActivity.this,"Confirm Password is a required field",Toast.LENGTH_SHORT).show();
        }
        else if(!confirm_password.equalsIgnoreCase(password)){
            //appCompatButton.setEnabled(true);
            editTextConfirmPassword.setError("Password does not match");
            Toast.makeText(SignUpActivity.this, "Password does not match",Toast.LENGTH_SHORT).show();
        }
        else if(!confirm_password.equalsIgnoreCase(password) && !password.equalsIgnoreCase(confirm_password)) {
            //appCompatButton.setEnabled(true);
            editTextConfirmPassword.setError("Password does not match");
            Toast.makeText(SignUpActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
        }
        else if(mobile_number.equalsIgnoreCase("")){
            //appCompatButton.setEnabled(true);
            editTextMobileNumber.setError(error_field);
            //Toast.makeText(SignUpActivity.this, "Mobile Number is a required field",Toast.LENGTH_SHORT).show();
        }
        else{
            //appCompatButton.setEnabled(true);
            signUpUser();
        }

    }

    //sign Up method
    public void signUpUser(){

        //create a new progressDialog and sets a message on it
        progressDialog = ProgressDialog.show(SignUpActivity.this,"" , null,true,true);
        progressDialog.setMessage("Please wait...");

        //get text from the EditText fields
        String username = editTextUsername.getText().toString().trim();
        //isEmailValid(email);
        String password = editTextPassword.getText().toString().trim();
        String confirm_password = editTextConfirmPassword.getText().toString().trim();
        String mobile_number = editTextMobileNumber.getText().toString().trim();
        String gender = appCompatSpinnerGender.getSelectedItem().toString().trim();

        //get the values from the fields and sets them to that of the values in the database
        users.setUser_name(username);
        users.setPassword(password);
        users.setConfirm_password(confirm_password);
        users.setMobile_number(mobile_number);
        users.setGender(gender);

        usersRef.child(users.getUser_name()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    Toast.makeText(SignUpActivity.this, " You have Successfully Signed Up ",Toast.LENGTH_SHORT).show();
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
                    },5000); //the timer will count 5 seconds..
                    Toast.makeText(SignUpActivity.this, " Cannot connect to the database, Please Try Again...! ",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //link to the User login page
    public void onLoginLinkButtonClick(View view){
        //starts the LoginActivity when user clicks the button
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }

    //clear text from the textfields
public void clearTextFields(){
        editTextUsername.setText(null);
        editTextPassword.setText(null);
        editTextConfirmPassword.setText(null);
        editTextMobileNumber.setText(null);
}
    

    
}
