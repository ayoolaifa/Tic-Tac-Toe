package com.example.tictactoe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class StartingPage extends AppCompatActivity {

    Button SinglePlayerButton, MultiPlayerButton, Tutorial, LeaderboardButton, LogOut, Settings;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);

        username = getIntent().getStringExtra("username");

        // On Click Listener for Log out button
        LogOut = findViewById(R.id.button_log_out);
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        // On Click Listener for single player button. changes intent to difficulty level screen
        SinglePlayerButton = (Button) findViewById(R.id.SinglePlayer);
        SinglePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DifficultyLevelPopup.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        // On Click Listener for multi player button. changes intent to two player game
        MultiPlayerButton = (Button) findViewById(R.id.MultiPlayer);
        MultiPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(StartingPage.this, MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        // On Click Listener to move intent outside of the application
        Tutorial = (Button) findViewById(R.id.Tutorial);
        Tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("https://www.exploratorium.edu/explore/puzzles/tictactoe");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);

            }
        });

        // On Click Listener to move intent to Leaderboard Screen
        LeaderboardButton = (Button) findViewById(R.id.LeaderboardBtn);
        LeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingPage.this, LeaderboardActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        // On Click Listener to move intent to Settings Screen
        Settings = findViewById(R.id.settings_btn);
        Settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingPage.this, SettingsActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        ImageView myImageView = (ImageView) findViewById(R.id.imageView);
        myImageView.setImageResource(R.drawable.tictactoetitle);
    }
}