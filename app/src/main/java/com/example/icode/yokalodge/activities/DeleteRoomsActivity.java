package com.example.icode.yokalodge.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.icode.yokalodge.R;

public class DeleteRoomsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_rooms);

        //getting reference to the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DELETE ROOMS");

        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.about_us:
                //starts the aboutUs activity for admin
                startActivity(new Intent(DeleteRoomsActivity.this,AboutUsAdminActivity.class));
                break;
            case android.R.id.home:
                //send user back to the adminDashboard
                startActivity(new Intent(DeleteRoomsActivity.this,AdminDashBoardActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
