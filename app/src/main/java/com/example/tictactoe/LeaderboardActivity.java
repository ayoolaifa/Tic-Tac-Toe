package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LeaderboardActivity extends AppCompatActivity {
    Spinner Difficulty, Filter;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        TableLayout leaderboardTable = findViewById(R.id.leaderboardTable);
        username = getIntent().getStringExtra("username");
        Difficulty = findViewById(R.id.difficultyDropdown);
        Filter = findViewById(R.id.filterDropdown);

        // Setting dropdown list to the difficulty spinner
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.
                createFromResource(this, R.array.difficultyDropdown, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Difficulty.setAdapter(difficultyAdapter);

        // Setting dropdown list to the filter spinner
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.
                createFromResource(this, R.array.filterDropdown, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Filter.setAdapter(filterAdapter);

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getAllRecords("Leaderboard");

        // Getting arraylist for leaderboard data stored in local database
        ArrayList<Score> leaderboardData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String difficulty = cursor.getString(cursor.getColumnIndexOrThrow("difficulty"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            int pscore = cursor.getInt(cursor.getColumnIndexOrThrow("pscore"));
            int cscore = cursor.getInt(cursor.getColumnIndexOrThrow("cscore"));

            Score newInput = new Score(difficulty, username, pscore, cscore);
            Log.d("Debugging", newInput.toString());
            leaderboardData.add(newInput);
        }

        cursor.close();


        // Spinner listener for when an item is selected for difficulty
        Difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String difficultyLevel = Difficulty.getSelectedItem().toString();
                String filter = Filter.getSelectedItem().toString();
                // checks which filter is selected at the moment
                if (filter.equals("Number Of Wins")) {
                    leaderboardTable.removeAllViews();
                    ArrayList<Score> tempArray = new ArrayList<>();
                    for (Score i : leaderboardData) {
                        if (i.getDifficulty().equals(difficultyLevel)) {
                            tempArray.add(i);
                        }
                    }

                    ArrayListComparator sorter = new ArrayListComparator();
                    // sorting arraylist in reversed order by the player score for the difficulty
                    Collections.sort(tempArray, sorter.reversed());

                    CreateTable(leaderboardTable, tempArray, filter);

                } else if (filter.equals("Win Percentage")) {
                    leaderboardTable.removeAllViews();
                    ArrayList<Score> tempArray = new ArrayList<>();
                    for (Score i : leaderboardData) {
                        if (i.getDifficulty().equals(difficultyLevel)) {
                            tempArray.add(i);
                        }
                    }

                    ArrayListWinPercentageComparator sorter = new ArrayListWinPercentageComparator();
                    // sorting arraylist in reversed order by the win percentage for the difficulty
                    Collections.sort(tempArray, sorter.reversed());

                    CreateTable(leaderboardTable, tempArray, filter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner listener for when an item is selected for filter spinner
        Filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String difficultyLevel = Difficulty.getSelectedItem().toString();
                String filter = Filter.getSelectedItem().toString();
                if (filter.equals("Number Of Wins")) {
                    leaderboardTable.removeAllViews();
                    ArrayList<Score> tempArray = new ArrayList<>();
                    for (Score i : leaderboardData) {
                        if (i.getDifficulty().equals(difficultyLevel)) {
                            tempArray.add(i);
                        }
                    }

                    ArrayListComparator sorter = new ArrayListComparator();
                    Collections.sort(tempArray, sorter.reversed());

                    CreateTable(leaderboardTable, tempArray, filter);

                } else if (filter.equals("Win Percentage")) {
                    leaderboardTable.removeAllViews();
                    ArrayList<Score> tempArray = new ArrayList<>();
                    for (Score i : leaderboardData) {
                        if (i.getDifficulty().equals(difficultyLevel)) {
                            tempArray.add(i);
                        }
                    }

                    ArrayListWinPercentageComparator sorter = new ArrayListWinPercentageComparator();
                    Collections.sort(tempArray, sorter.reversed());

                    CreateTable(leaderboardTable, tempArray, filter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView myImageView = (ImageView) findViewById(R.id.imageViewLeaderboard);
        myImageView.setImageResource(R.drawable.tictactoetitle);
    }

    // Function that set the data in a table row and adds it to the view
    public void CreateTable (TableLayout leaderboardTable, ArrayList<Score> tempArray, String Filter){
        TableRow headerRow = new TableRow(LeaderboardActivity.this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT

        ));

        TextView usernameHeader = new TextView(LeaderboardActivity.this);
        TextView scoreHeader = new TextView(LeaderboardActivity.this);

        usernameHeader.setText("Username");
        scoreHeader.setText(Filter);


        TableRow.LayoutParams usernameHeaderLayout = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
        );

        TableRow.LayoutParams scoreHeaderLayout = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
        );

        usernameHeaderLayout.weight=1;
        scoreHeaderLayout.weight=1;

        usernameHeader.setLayoutParams(usernameHeaderLayout);
        scoreHeader.setLayoutParams(scoreHeaderLayout);

        usernameHeader.setGravity(Gravity.CENTER);
        scoreHeader.setGravity(Gravity.CENTER);

        usernameHeader.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        headerRow.addView(usernameHeader);
        headerRow.addView(scoreHeader);

        leaderboardTable.addView(headerRow);

        for (Score i : tempArray) {
            TableRow tableRow = new TableRow(LeaderboardActivity.this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT

            ));

            TextView username = new TextView(LeaderboardActivity.this);
            TextView score = new TextView(LeaderboardActivity.this);

            username.setText(i.getUsername());
            if (Filter.equals("Number Of Wins")){
                score.setText(String.valueOf(i.getPscore()));
            }else{
                score.setText(String.valueOf(i.getWinPercentage()));
            }



            TableRow.LayoutParams usernameLayout = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
            );

            TableRow.LayoutParams scoreLayout = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
            );

            usernameLayout.weight=1;
            scoreLayout.weight=1;

            username.setLayoutParams(usernameLayout);
            score.setLayoutParams(scoreLayout);

            username.setGravity(Gravity.CENTER);
            score.setGravity(Gravity.CENTER);

            tableRow.addView(username);
            tableRow.addView(score);

            leaderboardTable.addView(tableRow);
        }
    }
}