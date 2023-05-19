package com.example.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    Button userGuide;
    ShareActionProvider shareActionProvider;
    String username;
    TableLayout table;
    ConstraintLayout ViewScore;
    TextView pScoreEasy, pScoreMedium, pScoreHard, cScoreEasy, cScoreMedium, cScoreHard, usernameTxtBox;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://tic-tac-toe-2f8a4-default-rtdb.europe-west1.firebasedatabase.app").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Getting the toolbar on the screen
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("username");

        // Setting an On Click Listener to the back button on the toolbar
        // Changes Intent to Starting Page
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartingPage.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        // On Click Listener to change intent to the user guide (webView)
        userGuide = findViewById(R.id.userGuideBtn);
        userGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserGuide.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        pScoreEasy = findViewById(R.id.pScoreEasy);
        cScoreEasy = findViewById(R.id.cScoreEasy);
        pScoreMedium = findViewById(R.id.pScoreMedium);
        cScoreMedium = findViewById(R.id.cScoreMedium);
        pScoreHard = findViewById(R.id.pScoreHard);
        cScoreHard = findViewById(R.id.cScoreHard);
        usernameTxtBox = findViewById(R.id.username_settings);
        usernameTxtBox.setText("Welcome " + username + "!!!");

        // Getting user data (player score and computer score) for each difficulty level and displaying it on a table
        mDatabase.child("Leaderboard").child("Easy").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pScoreEasy.setText(snapshot.child("Player Score").getValue().toString());
                cScoreEasy.setText(snapshot.child("Computer Score").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.child("Leaderboard").child("Medium").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pScoreMedium.setText(snapshot.child("Player Score").getValue().toString());
                        cScoreMedium.setText(snapshot.child("Computer Score").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        mDatabase.child("Leaderboard").child("Hard").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pScoreHard.setText(snapshot.child("Player Score").getValue().toString());
                        cScoreHard.setText(snapshot.child("Computer Score").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        ImageView myImageView = (ImageView) findViewById(R.id.imageViewSettings);
        myImageView.setImageResource(R.drawable.tictactoetitle);

        ViewScore = findViewById(R.id.viewScoreRelativeLayout);

        table = findViewById(R.id.settingsTable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating menu so it can be visible on toolabr
        getMenuInflater().inflate(R.menu.share_action_provider_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        // making the share icon be a share action provider
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        // Creating intent for what is going to be shared by this application to other applications
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this cool app!");

        // Creating intent for choosing which application the text will be shared with
        Intent shareOnTwitterIntent = new Intent(Intent.ACTION_SEND);
        shareOnTwitterIntent.setType("text/plain");
        shareOnTwitterIntent.putExtra(Intent.EXTRA_TEXT, "Check out this cool app on Twitter!");
        shareOnTwitterIntent.setPackage("com.twitter.android");

        Intent chooserIntent = Intent.createChooser(shareIntent, "Share via");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareOnTwitterIntent);
        shareActionProvider.setShareIntent(chooserIntent);
        return true;
    }

}
