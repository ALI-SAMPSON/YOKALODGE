package com.example.icode.yokalodge.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.icode.yokalodge.R;
import com.example.icode.yokalodge.models.Payments;
import com.example.icode.yokalodge.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class MakePaymentActivity extends AppCompatActivity {

    //class variables for various views
    ProgressDialog progressDialog;

    Payments payments;

    Users users;

    private EditText editTextUsername;
    private EditText editTextRoomNumber;
    private EditText editTextPrice;
    private EditText editTextMobileNumber;

    DatabaseReference paymentRef;

    DatabaseReference usersRef;

    RelativeLayout relativeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MAKE PAYMENT");

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTextRoomNumber = findViewById(R.id.editTextRoomNumber);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextMobileNumber = findViewById(R.id.editTextMobileNumber);

        relativeLayout = findViewById(R.id.relativeLayout);

        payments = new Payments();

        users = new Users();

        //String key = getIntent().getExtras().get("key").toString();

        //usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

        paymentRef = FirebaseDatabase.getInstance().getReference().child("Payments");


        //editTextUsername.setText(getIntent().getStringExtra("user_name"));
        editTextRoomNumber.setText(getIntent().getStringExtra("room_number"));
        editTextPrice.setText(getIntent().getStringExtra("price"));

        onEditTextClick();
    }

    //sets error message on the editTextViews when user clicks on to edit
    public void onEditTextClick(){

        //error strings
        final String error_room_number = "Room number field cannot be edited";
        final String error_price = "Price field cannot be edited";

        editTextRoomNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextRoomNumber.setError(error_room_number);
            }
        });

        editTextPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPrice.setError(error_price);
            }
        });
    }

    //method for handling make payments
    public void onMakePaymentButtonClick(View view){

        //gets input from the fields
        String user_name = editTextUsername.getText().toString().trim();
        String mobile_number = editTextMobileNumber.getText().toString().trim();

        //error strings
        String error_username = "Username is a required field...Please enter valid information to proceed with your payment";
        String error_mobile_number = "Password is a required field...Please enter valid information to proceed with your payment";

        //checks if the fields are not empty
        if(user_name.equals("")){
            editTextUsername.setError(error_username);
        }
        else if(mobile_number.equals("")){
            editTextMobileNumber.setError(error_mobile_number);
        }
        else if(user_name.equals("") && mobile_number.equals("")){
            //Toast.makeText(MakePaymentActivity.this,"Both username and password are required fields",Toast.LENGTH_LONG).show();
            Snackbar.make(relativeLayout,"Both username and password are required fields",Snackbar.LENGTH_LONG).show();
        }
        else{
            makePayment(); //method call
        }

    }

    //method for making the payments
    public void makePayment(){

        progressDialog = ProgressDialog.show(MakePaymentActivity.this,"",null,true,true);
        progressDialog.setMessage("Please wait...");

        //gets text from the user
        String user_name = editTextUsername.getText().toString().trim();
        final String room_number = editTextRoomNumber.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String mobile_number = editTextMobileNumber.getText().toString().trim();

        //sets the values from the EditText Fields to those in the database
        payments.setUser_name(user_name);
        payments.setRoom_number(room_number);
        payments.setPrice(price);
        payments.setMobile_number(mobile_number);

        //sets the values to the database by using the username as the child
        paymentRef.setValue(payments.getRoom_number()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(room_number.equals(payments.getRoom_number())){
                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                timer.cancel();
                            }
                        },5000);
                        //Toast.makeText(MakePaymentActivity.this,"Payment made successfully",Toast.LENGTH_LONG).show();
                        Snackbar.make(relativeLayout,"Room is already booked", Snackbar.LENGTH_LONG).show();
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
                        //Toast.makeText(MakePaymentActivity.this,"Payment made successfully",Toast.LENGTH_LONG).show();
                        Snackbar.make(relativeLayout,"Payment made successfully", Snackbar.LENGTH_LONG).show();

                        //sends a notification to the user of voting successfully
                        Intent intent = new Intent(MakePaymentActivity.this, MakePaymentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MakePaymentActivity.this, 0, intent, 0);
                        Notification notification = new Notification.Builder(MakePaymentActivity.this)
                                .setSmallIcon(R.mipmap.app_icon_round)
                                .setContentTitle("Yoka Logde")
                                .setContentText(" You have successfully made payment for room " + payments.getRoom_number().toString())
                                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                .setContentIntent(pendingIntent).getNotification();
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        nm.notify(0, notification);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        timer.cancel();
                    }
                },5000);
                //Toast.makeText(MakePaymentActivity.this,"Payment made successfully",Toast.LENGTH_LONG).show();
                Snackbar.make(relativeLayout,e.getStackTrace().toString(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_user,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            //opens the about us activity
            case R.id.about_us:
                startActivity(new Intent(MakePaymentActivity.this,AboutUsUserActivity.class));
                break;
                //sends user to the Home Activity
            case android.R.id.home:
                startActivity(new Intent(MakePaymentActivity.this,HomeActivity.class));
                break;
                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }
}
