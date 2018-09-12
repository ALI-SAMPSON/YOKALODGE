package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddRoomsActivity extends AppCompatActivity {

    //class variables
    private EditText editTextRoomNumber;
    private EditText editTextPrice;

    private FirebaseDatabase roomdB;
    private DatabaseReference roomRef;

    private CircleImageView circleImageView;
    private int image_request_code = 2;

    StorageReference storage;

    //for image location
    String imageLocation;

    StorageReference imagePath;

    private Rooms rooms;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private ProgressBar progressBar1;

    private NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);

        //checks if there is a toolbar, if yes it set the Home Button on it
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("ADD ROOMS");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        circleImageView = findViewById(R.id.circularImageView);
        editTextRoomNumber = findViewById(R.id.editTextRoomNumber);
        editTextPrice = findViewById(R.id.editTextPrice);

        rooms = new Rooms();
        roomdB = FirebaseDatabase.getInstance();
        roomRef = roomdB.getReference().child("Rooms");

        storage = FirebaseStorage.getInstance().getReference();

        progressBar = findViewById(R.id.progressBar);

        progressBar1 = findViewById(R.id.progressBar1);

        nestedScrollView = findViewById(R.id.nestedScrollView);

        mAuth = FirebaseAuth.getInstance();

    }

    //method to handle the select image from gallery when user click the image Button
    public void onSelectImage(View view){
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent,image_request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == image_request_code && resultCode == RESULT_OK && data.getData() != null){
            Uri uri = data.getData();
            circleImageView.setImageURI(uri);

            // displays the progressBar
            progressBar.setVisibility(View.VISIBLE);

            imagePath = storage
                    .child("Hotel Room Images")
                    .child(uri.getLastPathSegment());
            imageLocation = uri.getLastPathSegment();
            imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //String image = taskSnapshot.getDownloadUrl().toString();
                    // hides the progressBar
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddRoomsActivity.this, "Good!...Fill in the details to proceed", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // hides the progressBar
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(nestedScrollView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == image_request_code && resultCode == RESULT_OK){
            //getting the uri of the image and setting it to the imagePath
            try{
                //gets the image Uri and sets it to the imageButton
                Uri uri = data.getData();
                circleImageView.setImageURI(uri);

                progressBar.setVisibility(View.VISIBLE);

                imagePath = storage.child("Rooms Images").child(uri.getLastPathSegment());
                imageLocation = uri.getLastPathSegment();
                imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddRoomsActivity.this, "Good!...Fill in the details to proceed", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddRoomsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                progressBar.setVisibility(View.GONE);
            }
            catch (Exception e){
                progressBar.setVisibility(View.GONE);
                Snackbar.make(nestedScrollView,e.getMessage(),Snackbar.LENGTH_LONG).show();
            }
        }
    }*/

    //onclick listener for the Add Button
    public void onAddRoomButtonClick(View view){

        //get input from the editText fields
        String room_number = editTextRoomNumber.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();

        if(room_number.isEmpty()){
            editTextRoomNumber.setError(getString(R.string.error_empty_field));
            editTextRoomNumber.requestFocus();
            return;
        }
        else if(price.isEmpty()){
            editTextPrice.setError(getString(R.string.error_empty_field));
            editTextPrice.requestFocus();
            return;
        }
        else if(room_number.isEmpty() && price.isEmpty()){
            Snackbar.make(nestedScrollView,"Both fields cannot be left blank",Snackbar.LENGTH_LONG).show();
            return;
            //Toast.makeText(AddRoomsActivity.this,"Both fields cannot be left blank",Toast.LENGTH_LONG).show();
        }
        else{
            addRoom();
        }
    }

    // get values from fields
    /*private void getValues(){
        //get input from the editText fields
        String room_number = editTextRoomNumber.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();

        //set the values to the fields in the database
        rooms.setRoom_image(imagePath.toString());
        rooms.setRoom_number(room_number);
        rooms.setPrice(price);
    }*/

    //method to add rooms to the database
    public void addRoom(){

        progressBar1.setVisibility(View.VISIBLE);

        //get input from the editText fields
        String room_number = editTextRoomNumber.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();

        //set the values to the fields in the database
        rooms.setRoom_image(imageLocation);
        rooms.setRoom_number(room_number);
        rooms.setPrice(price);

        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomRef.child(rooms.getRoom_number()).setValue(rooms);
                clearTextFields();
                // hides rhe progressBar
                progressBar1.setVisibility(View.GONE);
                Snackbar.make(nestedScrollView,"Room added successfully",Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // hides rhe progressBar
                progressBar1.setVisibility(View.GONE);
                Snackbar.make(nestedScrollView,databaseError.getMessage(),Snackbar.LENGTH_LONG).show();
            }
        });


        /*roomRef.child(rooms.getRoom_number())
                .setValue(rooms)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    clearTextFields();
                    Snackbar.make(nestedScrollView,"Room added successfully",Snackbar.LENGTH_LONG).show();
                    //Toast.makeText(AddRoomsActivity.this,"Room added successfully",Toast.LENGTH_LONG).show();
                }
                else{
                    Snackbar.make(nestedScrollView,task.getException().getMessage(),Snackbar.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });*/

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
                AddRoomsActivity.this.finish();
            startActivity(new Intent(AddRoomsActivity.this,AboutUsAdminActivity.class));
                break;
            case android.R.id.home:
                //send user back to the adminDashboard
                AddRoomsActivity.this.finish();
                startActivity(new Intent(AddRoomsActivity.this,AdminDashBoardActivity.class));
                break;
                default:
                    break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Method for clearing all textfields after Login Button is Clicked
    public void clearTextFields() {
        //Clears all text from the EditText
        editTextRoomNumber.setText(null);
        editTextPrice.setText(null);

    }

}
