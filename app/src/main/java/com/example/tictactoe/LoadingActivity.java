package com.example.tictactoe;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class LoadingActivity extends AppCompatActivity {

    Handler handler = new Handler();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://tic-tac-toe-2f8a4-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    int requestCodes = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Setting the image view in activity_loading.xml to a drawable
        ImageView myImageView = (ImageView) findViewById(R.id.imageViewLoading);
        myImageView.setImageResource(R.drawable.tictactoetitle);

        // Checking if the permissions have already been accepted in previous
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_DENIED  ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.SCHEDULE_EXACT_ALARM,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED,
                            Manifest.permission.USE_FULL_SCREEN_INTENT},
                    requestCodes);
        } else{
            // Causes a delay of 1.5 seconds
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CreateDatabase();
                    startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                }
            }, 1500);
        }
    }

    // Function used when permissions are denied.
    public void Denied(){
        Log.d("Granted" , "Denied");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("You Have Denied Access To The Storage");

        builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Function to check if permissions have been accepted by the user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int count =0;
        if (requestCode == requestCodes) {
            if (grantResults.length > 0 ){
                for(int i : grantResults){
                    if (i == PackageManager.PERMISSION_GRANTED){
                        count +=1;
                    }
                }

                if (count == grantResults.length-1){
                    CreateDatabase();
                    startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                }else{
                    Denied();
                }
            }
        }
    }

    // Function to create database of data stored in firebase realtime database
    // Function also stores this data on the phones storage
    public void CreateDatabase(){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, MyDatabaseScheme.DATABASE_VERSION, MyDatabaseScheme.DATABASE_VERSION);

        mDatabase.child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()){
                    dbHelper.insertRecordAccounts(String.valueOf(s.child("username").getValue()), String.valueOf(s.child("password").getValue()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });

        mDatabase.child("Leaderboard").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot difficultySnapshot : snapshot.getChildren()){
                    String difficulty = String.valueOf(difficultySnapshot.getKey());
                    for (DataSnapshot usernameSnapshot : difficultySnapshot.getChildren()){
                        String username = String.valueOf(usernameSnapshot.getKey());
                        int pscore = Integer.parseInt(String.valueOf(usernameSnapshot.child("Player Score").getValue()));
                        int cscore = Integer.parseInt(String.valueOf(usernameSnapshot.child("Computer Score").getValue()));

                        dbHelper.insertRecordLeadboard(username, difficulty, pscore, cscore);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });

    }
}