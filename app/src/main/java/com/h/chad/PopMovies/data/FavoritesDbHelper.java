package com.h.chad.PopMovies.data;

import android.content.ContentProvider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chad on 5/24/2017.
 */
//todo 503 design the favorites database
public class FavoritesDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favoriteMovies.db";
    private static final int VERSION = 1;


    FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public void onCreate(){

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
