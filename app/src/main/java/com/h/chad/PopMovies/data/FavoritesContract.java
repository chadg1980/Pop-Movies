package com.h.chad.PopMovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chad on 5/24/2017.
 */

public class FavoritesContract {

    //Prevent someone from accidentially creating the class
    private FavoritesContract(){}

    private static final String LOG_TAG = FavoritesContract.class.getName();
    public static final String CONTENT_AUTHORITY = "com.h.chad.PopMovies.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    /**
     * Table of favorites
     * ID                   INTEGER -This is our database id...
     * MOVIE_ID             INTEGER -This is tthemoviedb API database id, needed for posters, comments, and trailers
     * TITLE                STRING
     * RELEASE_DATE         STRING
     * POSTER_PATH          STRING
     * VOTE_COUNT           INTEGER
     * VOTE_AVERAGE         REAL
     * POPULARITY           REAL
     * PLOT                 STRING
     * */
    public static final class FavoritesEntry implements BaseColumns{

        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVORITES);

        //Name of table
        public final static String TABLE_NAME = "favorites";
        //ID for each entry
        public final static String _ID = BaseColumns._ID;
        //ID from the movie database
        public final static String MOVIE_ID = "movie_id";
        //Tite of movie
        public final static String TITLE = "title";
        //Release date of the movie
        public final static String RELEASE_DATE = "release_date";
        //poster path is the URL for the poster
        public final static String POSTER_PATH = "poster_path";
        //Vote Count for the rating
        public final static String VOTE_COUNT = "vote_count";
        //Vote Average for the rating
        public final static String VOTE_AVERAGE = "vote_average";
        //Popularity of the movie
        public final static String POPULARITY = "popularity";
        //Plot of the movie
        public final static String PLOT = "plot";
        //movie poster
        public final static String POSTER = "poster";

        //Return values for getType in FavoriteProvider
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

    }
}
