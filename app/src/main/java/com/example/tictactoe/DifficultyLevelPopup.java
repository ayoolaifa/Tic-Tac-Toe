package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultyLevelPopup extends AppCompatActivity {
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_level_popup);

        username = getIntent().getStringExtra("username");
        Button easy = (Button) findViewById(R.id.easyDifficultyBtn);
        Button medium = (Button) findViewById(R.id.mediumDifficultyBtn);
        Button hard = (Button) findViewById(R.id.hardDifficultyBtn);

        // On Click Listener to set the difficulty to easy on single player mode
        easy.setOnClickListener(view -> {

            Intent i = new Intent(DifficultyLevelPopup.this, SinglePlayer.class);
            i.putExtra("username", username);
            i.putExtra("difficulty", "Easy");
            startActivity(i);
        });

        // On Click Listener to set the difficulty to medium on single player mode
        medium.setOnClickListener(view -> {
            Intent i = new Intent(DifficultyLevelPopup.this, SinglePlayer.class);
            i.putExtra("username", username);
            i.putExtra("difficulty", "Medium");
            startActivity(i);
        });

        // On Click Listener to set the difficulty to hard on single player mode
        hard.setOnClickListener(view -> {
            Intent i = new Intent(DifficultyLevelPopup.this, SinglePlayer.class);
            i.putExtra("username", username);
            i.putExtra("difficulty", "Hard");
            startActivity(i);
        });

        ImageView myImageView = (ImageView) findViewById(R.id.imageView);
        myImageView.setImageResource(R.drawable.tictactoetitle);

    }
}
