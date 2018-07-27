package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Rooms;
import com.example.icode.yokalodge.models.Users;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    Rooms rooms;

    ListView listView;

    FirebaseListAdapter firebaseListAdapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //gets reference to the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOME");

        //checks if the actionBar is not equal to null and set the Home button on
        if(getSupportActionBar() != null){
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = findViewById(R.id.home_listView);

        rooms = new Rooms();

        populateRooms();

    }

    //methods for getting available rooms from db and populating on the listView
    public void populateRooms(){

        //creating and initializing query from the database
        Query query = FirebaseDatabase.getInstance().getReference().child("Rooms");

        //listOptions to get the query and layout resource file to populate the data
        FirebaseListOptions<Rooms> options = new FirebaseListOptions.Builder<Rooms>()
                .setLayout(R.layout.room_list_item)
                .setQuery(query,Rooms.class)
                .build();

        //list adapter to populate the various views with their respective mapped data from the database
        firebaseListAdapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {

                //TextView and imageView to populate the data from the database
                TextView room_number = v.findViewById(R.id.room_number);
                TextView price = v.findViewById(R.id.price);
                ImageView imageView = v.findViewById(R.id.room_image);

                //instance of the room Class and type casting to the Object model
                Rooms rooms = (Rooms) model;

                //setting the text from the database to the TextViews
                room_number.setText(" Room Number: " + rooms.getRoom_number().toString());
                price.setText(" Price: GHS " + rooms.getPrice().toString());
                //picasso library to load the image into the imageView
                Picasso.with(HomeActivity.this).load(rooms.getRoom_image().toString()).into(imageView);
            }
        };

        listView.setAdapter(firebaseListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Rooms rooms = (Rooms)adapterView.getItemAtPosition(i);

               // Users users =(Users)adapterView.getItemAtPosition(i);

                Intent intentPayment = new Intent(HomeActivity.this,MakePaymentActivity.class);
                /*intentPayment.putExtra("user_name",users.getUser_name());
                intentPayment.putExtra("key",users.getUser_name());
                */
                intentPayment.putExtra("room_number",rooms.getRoom_number());
                intentPayment.putExtra("price",rooms.getPrice());
                startActivity(intentPayment);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseListAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        firebaseListAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_user,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //sends user to the about activity
            case R.id.about_us:
                startActivity(new Intent(HomeActivity.this,AboutUsUserActivity.class));
                break;
                //logs user out of the system
            case R.id.sign_out:

                progressDialog = ProgressDialog.show(HomeActivity.this,"",null,true,true);
                progressDialog.setMessage("Please wait...");
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        timer.cancel();
                    }
                },10000);
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
