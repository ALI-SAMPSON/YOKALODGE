package com.example.icode.yokalodge.activities;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.example.icode.yokalodge.R;

public class AddRoomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);

        //gets reference to the toolbar and sets a title on it
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ADD ROOMS");

        //checks if there is a toolbar, if yes it set the Home Button on it
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about_us:
                //starts the aboutUs activity for admin
            startActivity(new Intent(AddRoomsActivity.this,AboutUsAdminActivity.class));
                break;
            case android.R.id.home:
                //send user back to the adminDashboard
                startActivity(new Intent(AddRoomsActivity.this,AdminDashBoardActivity.class));
                break;
                default:
                    break;
        }

        return super.onOptionsItemSelected(item);
    }
}
