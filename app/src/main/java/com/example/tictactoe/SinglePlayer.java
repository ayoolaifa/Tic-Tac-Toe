package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.MotionEventCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class SinglePlayer extends AppCompatActivity implements View.OnClickListener {
    private final Button[][] buttons = new Button[3][3];
    private final String Computer = "O";
    private boolean XTurn = true;
    private int roundCount;
    private int Xpoints;
    private int Opoints;
    private TextView textViewPlayer;
    private TextView textViewComputer;
    private String difficultyLevel;
    SharedPreferences prefs;
    private int depth;
    private String username;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://tic-tac-toe-2f8a4-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    int computerScoreDatabase;
    int playerScoreDatabase;
    ComputerMove computerMove;
    boolean bound = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        // request for notification permissions
        requestNotificationPermission();

        // gets extra values put into intent
        difficultyLevel = getIntent().getStringExtra("difficulty");
        username = getIntent().getStringExtra("username");

        prefs = getSharedPreferences(username, MODE_PRIVATE);

        // depth level (difficulty) for mimimax algorithm is set here
        if (difficultyLevel.equals("Easy")){
            depth = 1;
        }else if (difficultyLevel.equals("Medium")){
            depth = 3;
        }else {
            depth = 4;
        }


        // get the current scores (player and computer) for the currently logged in user
        mDatabase.child("Leaderboard").child(difficultyLevel).child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()){
                    if (String.valueOf(s.getKey()).equals("Player Score")){
                        playerScoreDatabase = Integer.parseInt((String.valueOf(s.getValue())));
                    } else if (String.valueOf(s.getKey()).equals("Computer Score")){
                        computerScoreDatabase =Integer.parseInt((String.valueOf(s.getValue())));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // On Click Listener for return to the starting page
        Button BackButton = findViewById(R.id.button_back);
        Intent intent = new Intent(getApplicationContext(), StartingPage.class);
        intent.putExtra("username", username);
        BackButton.setOnClickListener(v -> startActivity(intent));

        textViewPlayer = findViewById(R.id.text_view_player);
        textViewComputer = findViewById(R.id.text_view_computer);

        GridLayout gridLayout = findViewById(R.id.singlePlayerBoard);

        // Checks which rotation the device is currently in
        // This is to set the width and height of the game board
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int buttonSizeWidth;
        int buttonSizeHeight;
        if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180){
            buttonSizeWidth = (int) (330.0f / 3.0f);
            buttonSizeHeight = (int) (510.0f / 3.0f);
        }else {
            buttonSizeWidth = (int) (570.0f / 3.0f);
            buttonSizeHeight = (int) (252.0f / 3.0f);
        }

        Resources r = getResources();
        int buttonWidthSizeDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, buttonSizeWidth, r.getDisplayMetrics());
        int buttonHeightSizeDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, buttonSizeHeight, r.getDisplayMetrics());

        int count = 0;
        // Creating the buttons for the board. Giving each button an onclicklistener
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button(this);
                button.setTag(new int[]{i, j});
                GridLayout.LayoutParams buttonLayout = new GridLayout.LayoutParams();
                buttonLayout.width = buttonWidthSizeDp;
                buttonLayout.height = buttonHeightSizeDp;
                button.setLayoutParams(buttonLayout);
                button.setTextSize(40);
                button.setId(count);
                count++;
                button.setOnClickListener(this);
                gridLayout.addView(button);
                buttons[i][j] = button;
            }
        }

        //Reset game button//
        Button buttonReset = findViewById(R.id.button_reset2);
        buttonReset.setOnClickListener(v -> resetGame());
    }

    // Function that check if he notification permission is enabled on the device
    // if it is not then the application takes user to notification settings
    private void requestNotificationPermission() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Check if the app has notification permission
        boolean isNotificationEnabled = notificationManager.areNotificationsEnabled();

        if (!isNotificationEnabled) {
            // Prompt the user to enable notification permission
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        }
    }

    // Function that determines what happens when button on game board is pressed
    @Override
    public void onClick(View v) {
        // checks if position on board has already been played on
        Button clickedButton = (Button) v;
        if(!(clickedButton.getText().equals(""))){
            return;
        }

        if (XTurn) { // if it is players turn
            Toast.makeText(this, "Player's Turn", 1000);
            if (clickedButton.getText().toString().isEmpty()) {
                clickedButton.setText("X");
                roundCount++;

                // Checks if anyone has won
                WhoHasWon();

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(getApplicationContext(), MakeMove.class);

                // When alarm is up then this is sent to the broadcasr reciever
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                long triggerTimeMillis = System.currentTimeMillis() + 30000;
                // Sets an alarm manager for 30 seconds.
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent);
            }
        }

        if (!XTurn) { // when it is the computers turn

            Toast.makeText(this, "Computer's Turn", 1000);
            Handler handler = new Handler();
            // make a 1 second delay so computer doesnt make move instantly
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String[][] field = StringArrayOfButtons();
                    String RowAndColumn = computerMove.BestMove(field, depth);
                    String[] array = RowAndColumn.split(",");
                    int Row = Integer.parseInt(array[0]);
                    int Column = Integer.parseInt(array[1]);
                    buttons[Row][Column].setText(Computer);
                    roundCount++;
                    WhoHasWon();
                }
            }, 1000);
        }
    }

    // Updates the score on the firebase database
    private void updateScoreOnDatabase(boolean playerWins){
        if (playerWins) {
            playerScoreDatabase += 1;
            mDatabase.child("Leaderboard").child(difficultyLevel).child(username)
                    .child("Player Score").setValue(playerScoreDatabase);
        }else if (!(playerWins)){
            computerScoreDatabase += 1;
            mDatabase.child("Leaderboard").child(difficultyLevel).child(username)
                    .child("Computer Score").setValue(computerScoreDatabase);
        }

    }

    // Changes the board game from button[][] to String[][]
    private String[][] StringArrayOfButtons(){
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++){
            for (int j = 0; j< 3; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        return field;
    }

    // Check if someone has won the game
    private boolean checkForWin(){
        String[][] field = StringArrayOfButtons();

        for (int i = 0; i < 3; i++){
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }

        }

        for (int i = 0; i < 3; i++){
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }

        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    // Function that run when player has won
    private void playerwins(){
        Xpoints++;
        Toast.makeText(this, "Player  Wins", Toast.LENGTH_SHORT).show();
        updatePointsText();
        updateScoreOnDatabase(true);
        resetBoard();
    }

    // Function that run when computer has won
    private void computerwins(){
        Opoints++;
        Toast.makeText(this, "Computer Wins", Toast.LENGTH_SHORT).show();
        updatePointsText();
        updateScoreOnDatabase(false);
        resetBoard();
    }

    // Function that run when player and computer has drawn
    private void draw(){
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    // Function that updates score on screen
    private void updatePointsText(){
        textViewPlayer.setText("Player: " + Xpoints);
        textViewComputer.setText("Computer: " + Opoints);
    }

    // Function that resets the game state
    private void resetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
//                buttons[i][j].setEnabled(true);
            }
        }

        roundCount = 0;
        XTurn = !XTurn;
    }

    // Function that resets the game state and score shown on the screen
    private void resetGame(){
        Xpoints = 0;
        Opoints = 0;
        updatePointsText();
        resetBoard();
    }

    // Function that check if the player or computer has won
    // Also checks if the game state is a draw
    // Aslo changes the the person who plays next if none of the above possibilities occur
    private void WhoHasWon(){
        if (checkForWin()) {
            if (XTurn) {
                playerwins();
            }
            else {
                computerwins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            XTurn = !XTurn;
        }

    }

    // Creating the service connection
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ComputerMove.ComputerMoveBinder binder = (ComputerMove.ComputerMoveBinder) iBinder;
            computerMove = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };

    // binds the service once the intent is changed to this context
    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, ComputerMove.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    // unbinds the service when the intent is changed to different context
    @Override
    protected void onStop(){
        super.onStop();
        unbindService(connection);
        bound = false;
    }

    // Function to store the game state when the application is closed or the intent is moved
    @Override
    protected void onPause(){
        super.onPause();

        difficultyLevel = getIntent().getStringExtra("difficulty");
        username = getIntent().getStringExtra("username");
        SharedPreferences.Editor prefsEditor = prefs.edit();

        //storing the game state in a string
        String board = "";
        for(int i =0; i < buttons.length; i++){
            for(int j =0; j < buttons[i].length; j++){
                if (buttons[i][j].getText().equals("")){
                    board += 0 + ",";
                }else{
                    board += buttons[i][j].getText() + ",";
                }

            }
        }

        // Storing variables in sharedPreferences
        String dBoard = difficultyLevel+"board";
        String dPScore = difficultyLevel+"pScore";
        String dCScore = difficultyLevel+"cScore";
        prefsEditor.putString(dBoard, board);
        prefsEditor.putInt(dPScore, Xpoints);
        prefsEditor.putInt(dCScore, Opoints);
        prefsEditor.apply();
    }

    // Function to return the game state to how is was before
    @Override
    protected void onResume(){
        super.onResume();

        difficultyLevel = getIntent().getStringExtra("difficulty");
        username = getIntent().getStringExtra("username");

        // setting the board back to what is was
        String dBoard = difficultyLevel+"board";
        String dPScore = difficultyLevel+"pScore";
        String dCScore = difficultyLevel+"cScore";

        String board = prefs.getString(dBoard, "");
        int pScore = prefs.getInt(dPScore, Xpoints);
        int cScore = prefs.getInt(dCScore, Opoints);


        if(!(board.equals(""))) {
            String[] values = board.split(",");

            int index = 0;
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    if (!(values[index].equals("0"))) {
                        buttons[i][j].setText(values[index]);
                    }
                    index++;
                }
            }
        }

        // setting the score of the game
        textViewPlayer.setText("Player: " + pScore);
        textViewComputer.setText("Computer: " + cScore);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("playerpoints", Xpoints);
        outState.putInt("computerpoints", Opoints);
        outState.putBoolean("XTurn", XTurn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        Xpoints = savedInstanceState.getInt("playerpoints");
        Opoints = savedInstanceState.getInt("computerpoints");
        XTurn = savedInstanceState.getBoolean("XTurn");
    }

}