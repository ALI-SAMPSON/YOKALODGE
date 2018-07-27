package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
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
    private EditText editTextUsername;
    private EditText editTextPassword;

    NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        nestedScrollView = findViewById(R.id.nestedScrollView);

    }

    //checks for the availability of network on the device
    public boolean isNetworkAvailable(){
        boolean have_WIFI = false;
        boolean have_MobileDate = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info : networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI")){
                if(info.isConnected()){
                    have_WIFI = true;
                }
            }
            if(info.getTypeName().equalsIgnoreCase("MOBILE")){
                have_MobileDate = true;
            }
        }
        return have_MobileDate || have_WIFI;
    }

    //method to be called when the login Button is clicked or tapped
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
            Toast.makeText(LoginActivity.this,"Email and Password are required fields",Toast.LENGTH_SHORT).show();
        }
        else{
            loginUser();
        }

    }

    //method for logging user into the system
    public void loginUser(){

        if(isNetworkAvailable()){
            //get the text from the EditText
            final String username = editTextUsername.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();

            //initialization of the instance of the ProgressDialog
            progressDialog = ProgressDialog.show(LoginActivity.this,"",null,true,true);
            progressDialog.setMessage("Please wait..."); //set a message on the progressDialog

            //progressDialog.setContentView(R.layout.material_design_progressdialog);
            //progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dbRef.child(username).addValueEventListener(new ValueEventListener() {
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

                            clearBothTextFields(); //call to this method
                            Toast.makeText(LoginActivity.this, "You have successfully logged in", Toast.LENGTH_SHORT).show();
                            Intent intentLogin = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intentLogin);
                        }
                        else if(!password.equals(users.getPassword())){
                            final Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    timer.cancel();
                                }
                            },3000);
                            Toast.makeText(LoginActivity.this,"Incorrect Username or Password",Toast.LENGTH_LONG).show();
                            clearPasswordTextField();//call to this method
                        }
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
                        Toast.makeText(LoginActivity.this,"User does not exist in database",Toast.LENGTH_LONG).show();
                        clearBothTextFields();//call to this method
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, databaseError.toException().toString(),Toast.LENGTH_LONG).show();

                }
            });
        }
        else if(!isNetworkAvailable()){
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    timer.cancel();
                }
            },2000);
            Snackbar.make(nestedScrollView,"No Internet",Snackbar.LENGTH_INDEFINITE).show();
        }

    }

    //method called when the link to the SignUp Activity is clicked or tapped
    public void onSignUpLinkClick(View View){
        //starts the Sign Up Activity
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));

    }

    //method called when the link to the SignUp Activity is clicked or tapped
    public void onAdminLoginButtonLinkClick(View view){
        //starts the AdminLoginActivity
        startActivity(new Intent(LoginActivity.this,AdminLoginActivity.class));
    }

    // clears the Username and Password EditText
    public void clearBothTextFields(){
        editTextUsername.setText(null);
        editTextPassword.setText(null);
    }

    public void clearPasswordTextField(){
        editTextPassword.setText(null);
    }

}
