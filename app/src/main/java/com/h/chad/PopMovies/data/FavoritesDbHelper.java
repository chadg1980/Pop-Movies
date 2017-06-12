package com.h.chad.PopMovies.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.h.chad.PopMovies.data.FavoritesContract.FavoritesEntry;

/**
 * Created by chad on 5/24/2017.
 */
//todo 503 design the favorites database
public class FavoritesDbHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = FavoritesDbHelper.class.getName();
    //Name of database name for file
    private static final String DATABASE_NAME = "favoriteMovies.db";
    Context mContext;
    //Version 1 for now
    private static final int VERSION = 2;
    /**
     * Constructor
     * @param context is the context of the app
     * */
    FavoritesDbHelper(Context context) {

        super(context, DATABASE_NAME, null, VERSION);
        this.mContext = context;
    }
    /**
     * onCreate should create the entire database
     * ID                   INTEGER -This is our database id...
     * MOVIE_ID             INTEGER -This is tthemoviedb API database id, needed for posters, comments, and trailers
     * TITLE                STRING
     * RELEASE_DATE         STRING
     * POSTER_PATH          STRING
     * VOTE_COUNT           INTEGER
     * VOTE_AVERAGE         DOUBLE
     * POPULARITY           DOUBLE - removed from db
     * PLOT                 STRING
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FAVORITES_TABLE =

                "CREATE TABLE " + FavoritesEntry.TABLE_NAME + "( " +
                FavoritesEntry._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                FavoritesEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                FavoritesEntry.TITLE + " TEXT, " +
                FavoritesEntry.RELEASE_DATE + " TEXT, " +
                FavoritesEntry.POSTER_PATH + " TEXT, " +
                FavoritesEntry.VOTE_COUNT + " INTEGER, " +
                FavoritesEntry.VOTE_AVERAGE + " REAL, " +
                FavoritesEntry.PLOT + " TEXT ); ";

        db.execSQL(SQL_CREATE_FAVORITES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
            onCreate(db);
        }

    }

    public boolean exists(int id ){
        ContentResolver mContentResolver = mContext.getContentResolver();
        String selection = FavoritesEntry.MOVIE_ID + " = " + id;
        Cursor c = mContentResolver.query(FavoritesEntry.CONTENT_URI,
                new String[]{FavoritesEntry.MOVIE_ID}, selection, null, null);
        if(c.getCount() < 0){
            return true;
        }
        return false;
    }
}
