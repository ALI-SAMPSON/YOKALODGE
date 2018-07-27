package com.example.icode.yokalodge.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Rooms;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class ViewAddedRoomsActivity extends AppCompatActivity {

    ListView listView;

    private FirebaseDatabase roomdB;
    private DatabaseReference roomRef;

    Rooms rooms;

    ImageView imageView;

    //ArrayList<Rooms> arrayList;

    //ArrayAdapter<Rooms> arrayAdapter;

    FirebaseListAdapter firebaseListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_added_rooms);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DELETE ROOMS");

        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.view_rooms_listView);

        rooms = new Rooms();

        //roomdB = FirebaseDatabase.getInstance();
        //roomRef = roomdB.getReference().child("Rooms");

        viewRooms();
    }

    //methods for getting available rooms from db and populating on the listView
    public void viewRooms(){

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
                Rooms rooms = (Rooms)model;

                //setting the text from the database to the TextViews
                room_number.setText(" Room Number: " + rooms.getRoom_number().toString());
                price.setText(" Price: GHS " + rooms.getPrice().toString());
                //picasso library to load the image into the imageView
                Picasso.with(ViewAddedRoomsActivity.this).load(rooms.getRoom_image().toString()).into(imageView);

                //Picasso.with(ViewAddedRoomsActivity.this).load(rooms.getRoom_image().toString()).into(imageView);

            }
        };

        listView.setAdapter(firebaseListAdapter);

        //onclick listener for listView , listening for event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Rooms rooms = (Rooms) adapterView.getItemAtPosition(i);
                //starts the deleteRoomsActivity
                Intent intentDelete = new Intent(ViewAddedRoomsActivity.this,DeleteRoomsActivity.class);
                intentDelete.putExtra("room_number",rooms.getRoom_number());
                intentDelete.putExtra("price",rooms.getPrice());
                intentDelete.putExtra("room_image", rooms.getRoom_image());
                intentDelete.putExtra("key", rooms.getRoom_number());
                startActivity(intentDelete);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseListAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.about_us:
                //start the AboutUsAdmin Activity
                startActivity(new Intent(ViewAddedRoomsActivity.this,AboutUsAdminActivity.class));
                break;

            case android.R.id.home:
                //goes back to the AdminDashboard
                startActivity(new Intent(ViewAddedRoomsActivity.this,AdminDashBoardActivity.class));
                break;
                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }
}
