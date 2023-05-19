package com.example.tictactoe;
// This class contains all the string values for database name, tabls and columns
// Also contains database queries
public class MyDatabaseScheme {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDB.db";

    public static final String ACCOUNT_TABLE = "Accounts";
    public static final String ACCOUNT_COLUMN_USERNAME = "username";
    public static final String ACCOUNT_COLUMN_PASSWORD = "password";

    public static final String LEADERBOARD_TABLE = "Leaderboard";
    public static final String LEADERBOARD_COLUMN_DIFFICULTY = "difficulty";
    public static final String LEADERBOARD_COLUMN_USERNAME = "username";
    public static final String LEADERBOARD_COLUMN_PSCORE = "pscore";
    public static final String LEADERBOARD_COLUMN_CSCORE = "cscore";

    public static final String SQL_CREATE_ACCOUNTS =
            "CREATE TABLE " + ACCOUNT_TABLE + " (" +
            ACCOUNT_COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
            ACCOUNT_COLUMN_PASSWORD+ " TEXT NOT NULL, " +
            "PRIMARY KEY("+ACCOUNT_COLUMN_USERNAME+"));";

    public static final String SQL_DELETE_ACCOUNT =
            "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;

    public static final String SQL_CREATE_LEADERBOARD =
            "CREATE TABLE " + LEADERBOARD_TABLE + " (" +
            LEADERBOARD_COLUMN_DIFFICULTY + " TEXT NOT NULL, " +
            LEADERBOARD_COLUMN_USERNAME	+ " TEXT NOT NULL, " +
            LEADERBOARD_COLUMN_PSCORE	+ " INTEGER NOT NULL, " +
            LEADERBOARD_COLUMN_CSCORE	+ " INTEGER NOT NULL, " +
            "PRIMARY KEY("+LEADERBOARD_COLUMN_DIFFICULTY+","+LEADERBOARD_COLUMN_USERNAME+"));";

    public static final String SQL_DELETE_LEADERBOARD =
            "DROP TABLE IF EXISTS " + LEADERBOARD_TABLE;
}
