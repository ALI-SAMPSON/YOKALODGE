package com.example.icode.yokalodge.activities;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Timer;
import java.util.TimerTask;

public class AddRoomsActivity extends AppCompatActivity {

    //class variables
    private EditText editTextRoomNumber;
    private EditText editTextPrice;

    private FirebaseDatabase roomdB;
    private DatabaseReference roomRef;

    private ImageButton imageButton;
    private int image_request_code = 2;

    StorageReference storage;

    //for image location
    String imageLocation;

    StorageReference imagePath;

    Rooms rooms;

    ProgressDialog progressDialog;



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

        imageButton = findViewById(R.id.imageBtn);
        editTextRoomNumber = findViewById(R.id.editTextRoomNumber);
        editTextPrice = findViewById(R.id.editTextPrice);

        rooms = new Rooms();
        roomdB = FirebaseDatabase.getInstance();
        roomRef = roomdB.getReference().child("Rooms");

        storage = FirebaseStorage.getInstance().getReference();

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
        if(requestCode == image_request_code && resultCode == RESULT_OK){
            //getting the uri of the image and setting it to the imagePath
            try{
                //gets the image Uri and sets it to the imageButton
                Uri uri = data.getData();
                imageButton.setImageURI(uri);
                imagePath = storage.child("Rooms").child(uri.getLastPathSegment());
                imageLocation = uri.getLastPathSegment();
                imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddRoomsActivity.this, "Good!...Fill in the details to proceed", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddRoomsActivity.this, "Image not Uploaded...Please make sure the image is of valid type!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //onclick listener for the Add Button
    public void onAddRoomButtonClick(View view){

        String error_fill_text = "This field cannot be left blank";

        //get input from the editText fields
        String room_number = editTextRoomNumber.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();

        if(room_number.equals("")){
            editTextRoomNumber.setError(error_fill_text);
        }
        else if(price.equals("")){
            editTextPrice.setError(error_fill_text);
        }
        else if(room_number.equals("") && price.equals("")){
            Toast.makeText(AddRoomsActivity.this,"Both fields cannot be left blank",Toast.LENGTH_LONG).show();
        }
        else{
            addRoom();
        }
    }

    //method to add rooms to the database
    public void addRoom(){

        progressDialog = ProgressDialog.show(AddRoomsActivity.this,"",null,true,true);
        progressDialog.setMessage("Please wait...");

        //get input from the editText fields
        String room_number = editTextRoomNumber.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();

        //set the values to the fields in the database
        rooms.setRoom_image(imageLocation);
        rooms.setRoom_number(room_number);
        rooms.setPrice(price);


        roomRef.child(rooms.getRoom_number()).setValue(rooms).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            timer.cancel();
                        }
                    },5000);
                    clearTextFields();
                    Toast.makeText(AddRoomsActivity.this,"Room added successfully",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AddRoomsActivity.this,"Error adding room...Please Try again",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddRoomsActivity.this,e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
            }
        });

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

    //Method for clearing all textfields after Login Button is Clicked
    public void clearTextFields() {
        //Clears all text from the EditText
        editTextRoomNumber.setText("");
        editTextPrice.setText("");

    }

}
