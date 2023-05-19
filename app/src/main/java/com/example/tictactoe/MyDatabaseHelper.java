package com.example.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, MyDatabaseScheme.DATABASE_NAME, null, MyDatabaseScheme.DATABASE_VERSION);
    }

    // Function to create the tables in the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyDatabaseScheme.SQL_CREATE_ACCOUNTS);
        db.execSQL(MyDatabaseScheme.SQL_CREATE_LEADERBOARD);
    }

    // Function that first deletes tables in database and then creates then again
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyDatabaseScheme.SQL_DELETE_ACCOUNT);
        db.execSQL(MyDatabaseScheme.SQL_DELETE_LEADERBOARD);
        onCreate(db);

    }

    // Function that inserts records into the accounts table
    public long insertRecordAccounts(String username, String password){
        ContentValues values = new ContentValues();
        values.put(MyDatabaseScheme.ACCOUNT_COLUMN_USERNAME, username);
        values.put(MyDatabaseScheme.ACCOUNT_COLUMN_PASSWORD, password);

        SQLiteDatabase db =  getWritableDatabase();
        long newRowId = db.insert(MyDatabaseScheme.ACCOUNT_TABLE, null, values);
        db.close();

        return newRowId;
    }

    // Function that inserts records into the leaderboard table
    public long insertRecordLeadboard(String username, String difficulty, int pscore, int cscore){
        ContentValues values = new ContentValues();
        values.put(MyDatabaseScheme.LEADERBOARD_COLUMN_USERNAME, username);
        values.put(MyDatabaseScheme.LEADERBOARD_COLUMN_DIFFICULTY, difficulty);
        values.put(MyDatabaseScheme.LEADERBOARD_COLUMN_PSCORE, pscore);
        values.put(MyDatabaseScheme.LEADERBOARD_COLUMN_CSCORE, cscore);

        SQLiteDatabase db =  getWritableDatabase();
        long newRowId = db.insert(MyDatabaseScheme.LEADERBOARD_TABLE, null, values);
        db.close();

        return newRowId;
    }

    // Function that gets all the records from a table
    public Cursor getAllRecords(String TABLE){
        SQLiteDatabase db = getReadableDatabase();
        String[] project;
        Cursor cursor = null;
        if (TABLE.equals(MyDatabaseScheme.ACCOUNT_TABLE)){
             project = new String[]{
                     MyDatabaseScheme.ACCOUNT_COLUMN_USERNAME,
                     MyDatabaseScheme.ACCOUNT_COLUMN_PASSWORD
             };

             cursor = db.query(
                     MyDatabaseScheme.ACCOUNT_TABLE,
                     project,
                     null,
                     null,
                     null,
                     null,
                     null,
                     null
             );
        }
        if (TABLE.equals(MyDatabaseScheme.LEADERBOARD_TABLE)){
            project = new String[]{
                    MyDatabaseScheme.LEADERBOARD_COLUMN_DIFFICULTY,
                    MyDatabaseScheme.LEADERBOARD_COLUMN_USERNAME,
                    MyDatabaseScheme.LEADERBOARD_COLUMN_PSCORE,
                    MyDatabaseScheme.LEADERBOARD_COLUMN_CSCORE
            };

            cursor = db.query(
                MyDatabaseScheme.LEADERBOARD_TABLE,
                project,
                null,
                null,
                null,
                null,
                null,
                null
            );
        }

        return cursor;
    }
}
