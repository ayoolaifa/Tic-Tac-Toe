package com.example.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];

    private boolean XTurn = true;

    private int roundCount;

    private int Xpoints;
    private int Opoints;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    String username;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("username");

        // On Click Listener to change the starting page
        Button BackButton = findViewById(R.id.button_back_multi);
        Intent intent = new Intent(getApplicationContext(), StartingPage.class);
        intent.putExtra("username", username);
        BackButton.setOnClickListener(v -> startActivity(intent));

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        // On Click Listener for reset button. Resets the current board
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(v -> resetGame());

        GridLayout gridLayout = findViewById(R.id.multiPlayerBoard);

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
    }

    // Function that determines what happens when button on game board is pressed
    @Override
    public void onClick(View v) {
        // if a player has already played in the position (button) nothing will happen
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        // if it this the X players turn, button text will be X
        if (XTurn){
            ((Button) v).setText("X");
        }else {         // if it this the O players turn, button text will be O
            ((Button) v).setText("O");
        }

        // increasing the round count
        roundCount++;

        if (checkForWin()) {// checking if any player has won

            if (XTurn) {
                Xwins();
            } else {
                Owins();
            }
        } else if (roundCount ==9) { // checking is there is no position on board left
            draw();
        } else { // changing which player can make their move next
            XTurn = !XTurn;
        }

    }

    // Function to check if a player has won
    private boolean checkForWin(){
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++){
            for (int j = 0; j< 3; j++){
                field[i][j] = (String) buttons[i][j].getText().toString();
            }
        }

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

    // Function occurs when X player has won
    private void Xwins(){
        Xpoints++;
        Toast.makeText(this, "Player 1 Wins", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    // Function occurs when O player has won
    private void Owins(){
        Opoints++;
        Toast.makeText(this, "Player 2 Wins", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    // Function occurs when player has drawn
    private void draw(){
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    // Function to update the player scores displayed on screen
    private void updatePointsText(){
        textViewPlayer1.setText("Player 1: " + Xpoints);
        textViewPlayer2.setText("Player 2: " + Opoints);
    }

    // Function to reset the current board game
    private void resetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        XTurn = !XTurn;
    }

    // Function to reset the whole game (board and scores)
    private void resetGame(){
        Xpoints = 0;
        Opoints = 0;
        updatePointsText();
        resetBoard();
    }


    // Function to store the game state when the application is closed or the intent is moved
    @Override
    protected void onPause() {
        super.onPause();

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
        prefsEditor.putString("multiplayerBoard", board);
        prefsEditor.putInt("multiplayerP1Score", Xpoints);
        prefsEditor.putInt("multiplayerP2core", Opoints);
        prefsEditor.apply();
    }

    // Function to return the game state to how is was before
    @Override
    protected void onResume(){
        super.onResume();

        username = getIntent().getStringExtra("username");

        String board = prefs.getString("multiplayerBoard", "");
        int p1Score = prefs.getInt("multiplayerP1Score", Xpoints);
        int p2Score = prefs.getInt("multiplayerP2core", Opoints);


        // setting the board back to what is was
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
        textViewPlayer1.setText("Player 1: " + p1Score);
        textViewPlayer2.setText("Player 2: " + p2Score);
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("Xpoints", Xpoints);
        outState.putInt("Opoints", Opoints);
        outState.putBoolean("XTurn", XTurn);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        Xpoints = savedInstanceState.getInt("Xpoints");
        Opoints = savedInstanceState.getInt("Opoints");
        XTurn = savedInstanceState.getBoolean("XTurn");
    }
}