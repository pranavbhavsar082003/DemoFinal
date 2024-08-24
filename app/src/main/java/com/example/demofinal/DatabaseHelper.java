package com.example.demofinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MoviesDB";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_FAVORITES = "favorites";

    // Table Columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_POSTER_URL = "posterUrl";
    public static final String COLUMN_IMDB_ID = "imdb_id";

    // Create Table SQL Query
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_RATING + " TEXT, " +
                    COLUMN_YEAR + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_POSTER_URL + " TEXT, " +
                    COLUMN_IMDB_ID + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
}
