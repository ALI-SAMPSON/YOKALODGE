package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Admin;
import com.example.icode.yokalodge.models.CurrentAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class EditCredentialsActivity extends AppCompatActivity {

    DatabaseReference adminRef;

    DatabaseReference currentAdminRef;

    Admin admin;

    CurrentAdmin currentAdmin;

    private EditText editTextUsername;
    private EditText editTextPassword;

    //instance of a Relative layout
    private RelativeLayout relativeLayout;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_credentials);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("UPDATE CREDENTIALS");

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        adminRef = FirebaseDatabase.getInstance().getReference().child("Admin");

        currentAdminRef = FirebaseDatabase.getInstance().getReference().child("CurrentAdmin");

        //instances of the classes
        admin = new Admin();
        currentAdmin = new CurrentAdmin();

        //instances of the classes
        relativeLayout = findViewById(R.id.relativeLayout);

        //sets the username and password EditTExt fields with the current logged in Admin details
        editTextUsername.setText(currentAdmin.getCurrent_user_name());
        editTextPassword.setText(currentAdmin.getCurrent_password());

    }

    //update button method
    public void onUpdateButtonClick(View view){
        //calls this method;
       updateCredentials();
    }

    //method to perform update on Admin credentials
    public void updateCredentials(){
        //get text from the editText fields
        final String username  = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog = ProgressDialog.show(EditCredentialsActivity.this,"",null,true,true);
                progressDialog.setMessage("Please wait...");

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        timer.cancel();
                    }
                },10000);
                //set the text from the EditTExt fields to those in the database fields
                dataSnapshot.getRef().child("username").setValue(username);
                dataSnapshot.getRef().child("password").setValue(password);
                Snackbar.make(relativeLayout,"Credentials Updated successfully",Snackbar.LENGTH_LONG).show();
                //Toast.makeText(EditCredentialsActivity.this,"Credentials Updated successfully",Toast.LENGTH_LONG).show();
                //EditCredentialsActivity.this.finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(relativeLayout,databaseError.toException().toString(),Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //opens the AboutUsActivity for admin
            case R.id.about_us:
                startActivity(new Intent(EditCredentialsActivity.this, AboutUsAdminActivity.class));
                break;
                //goes back to the AdminDashBoard
            case android.R.id.home:
                startActivity(new Intent(EditCredentialsActivity.this,AdminDashBoardActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
