package com.example.icode.yokalodge.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.icode.yokalodge.R;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //gets an instance of the ActionBar
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.hide();
        }

    }

    //login button method for admin
    public void onLoginButtonClick(View view){

    }

    //link to the user login interface
    public void onLoginButtonLinkClick(View view){
        //starts the Users LoginActivity
        startActivity(new Intent(AdminLoginActivity.this,LoginActivity.class));
    }
}
