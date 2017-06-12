package com.h.chad.PopMovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import static com.h.chad.PopMovies.data.FavoritesContract.CONTENT_AUTHORITY;
import static com.h.chad.PopMovies.data.FavoritesContract.FavoritesEntry.MOVIE_ID;
import static com.h.chad.PopMovies.data.FavoritesContract.FavoritesEntry.TABLE_NAME;
import static com.h.chad.PopMovies.data.FavoritesContract.PATH_FAVORITES;
import static com.h.chad.PopMovies.data.FavoritesContract.FavoritesEntry.CONTENT_LIST_TYPE;
import static com.h.chad.PopMovies.data.FavoritesContract.FavoritesEntry.CONTENT_ITEM_TYPE;
import static com.h.chad.PopMovies.data.FavoritesContract.FavoritesEntry;

/**
 * Created by chad on 5/24/2017.
 */

//todo 504 implement the content provider methods
    //CRUD methods create update delete and query all
    //no need to query for a single favorite movie

public class FavoritesContentProvider extends ContentProvider{
    private final static String LOG_TAG = FavoritesContentProvider.class.getName();
    //database helper
    private FavoritesDbHelper mDbHelper;
    private static final int FAVORITE_MOVIES = 100;
    private static final int FAVORITE_MOVIE_ID = 200;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_FAVORITES, FAVORITE_MOVIES);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_FAVORITES +"/#", FAVORITE_MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new FavoritesDbHelper(getContext());
        return true;
    }


    /**
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * */

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        //Get the database in read mode!
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //Create the cursor for the return value
        Cursor cursor;
        //match the URI
        int match = sUriMatcher.match(uri);
        //determine the path to take
        switch (match){
            case FAVORITE_MOVIES:
                cursor = db.query(TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FAVORITE_MOVIE_ID:
                selection = FavoritesContract.FavoritesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(LOG_TAG + " Can't query uri " + uri);
        }
        //Set notification URI on the cursor so we know what content URI the cursor was created for
        //IF the data at the URi changes we need to update the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FAVORITE_MOVIES:
                return CONTENT_LIST_TYPE;
            case FAVORITE_MOVIE_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + " with match " + match);
        }
    }
    /**
     * @param uri
     * @param values
     * @return uri
     * */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case FAVORITE_MOVIES:
                Uri returnUri = insertFavorite(uri, values);
                return returnUri;
            default:
                throw new IllegalArgumentException(LOG_TAG + " can't match URI " + uri );
        }
    }

    private Uri insertFavorite(Uri uri, ContentValues values){
        String movieId = values.getAsString(FavoritesEntry.MOVIE_ID);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id;
        if(movieId == null){
            throw new IllegalArgumentException(LOG_TAG + " movieid is null ");
        }
        String title = values.getAsString(FavoritesEntry.TITLE);
        String releaseDate = values.getAsString(FavoritesEntry.RELEASE_DATE);
        String posterPath = values.getAsString(FavoritesEntry.POSTER_PATH);
        int voteCount = values.getAsInteger(FavoritesEntry.VOTE_COUNT);
        double voteAverage = values.getAsDouble(FavoritesEntry.VOTE_AVERAGE);
        String plotString = values.getAsString(FavoritesEntry.PLOT);


        id = db.insert(TABLE_NAME, null, values);
        if(id == -1){
            Log.e(LOG_TAG, "insertion failed for uri " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int rowsDeleted = 0;
        final int match = sUriMatcher.match(uri);
        switch(match){
            case FAVORITE_MOVIES:
                rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_MOVIE_ID:
                //delete a single movie from the database
                selection = FavoritesEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(LOG_TAG + " Deletion is not available for uri " + uri);
        }
        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsAffected = 0;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
        return rowsAffected;
    }

}
