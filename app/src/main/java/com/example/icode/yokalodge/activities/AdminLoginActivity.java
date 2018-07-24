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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

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

        admin = new Admin();

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
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        adminRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //gets a snapshot of the data in the database
                    Admin admin = dataSnapshot.getValue(Admin.class);
                    //checks if password entered equals the one in the database
                    if(password.equals(admin.getPassword())){
                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                timer.cancel();
                            }
                        },5000);
                        clearBothTextFields(); //call to this method
                        Toast.makeText(AdminLoginActivity.this,"You have successfully Logged In", Toast.LENGTH_LONG).show();
                        //opens the admin DashBoard
                        startActivity(new Intent(AdminLoginActivity.this,AdminDashBoardActivity.class));
                    }
                    //checks if password entered does not equal the one in the database
                    else if(!password.equals(admin.getPassword())){
                        final  Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                timer.cancel();
                            }
                        },5000);
                        clearPasswordTextField();
                        Toast.makeText(AdminLoginActivity.this,"Incorrect Username and Password", Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            timer.cancel();
                        }
                    },5000);
                    Toast.makeText(AdminLoginActivity.this,"Admin does not exist in database...please try again later!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminLoginActivity.this,databaseError.toException().toString(),Toast.LENGTH_LONG).show();
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
