package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class EditCredentialsActivity extends AppCompatActivity {

    DatabaseReference roomRef;

    Admin admin;

    private EditText editTextUsername;
    private EditText editTextPassword;

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

        roomRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        admin = new Admin();

        //editTextUsername.setText(admin.getUsername().toString());
        //editTextPassword.setText(admin.getPassword().toString());
    }

    //update button method
    public void onUpdateButtonClick(View view){
       /* roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                },5000);
                dataSnapshot.getRef().child("username").setValue(editTextUsername.getText().toString());
                dataSnapshot.getRef().child("password").setValue(editTextPassword.getText().toString());
                Toast.makeText(EditCredentialsActivity.this,"Credentials Updated successfully",Toast.LENGTH_LONG).show();
                EditCredentialsActivity.this.finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

       String username = admin.getUsername().toString();
       String password = admin.getPassword().toString();

       admin.setUser_name(editTextUsername.getText().toString());
       admin.setPassword(editTextPassword.getText().toString());

        roomRef.child(admin.getUsername()).setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        timer.cancel();
                    }
                },5000);
                Toast.makeText(EditCredentialsActivity.this,"Credentials Updated successfully",Toast.LENGTH_LONG).show();
                EditCredentialsActivity.this.finish();
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
