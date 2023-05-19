package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class UserGuide extends AppCompatActivity {

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        username = getIntent().getStringExtra("username");

        // Setting the webView to html file in assets folder
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/html/index.html");

        // Getting the toolbar on the screen
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Guide");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting an On Click Listener to the back button on the toolbar
        // Changes Intent to Starting Page
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}