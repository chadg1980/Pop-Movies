package com.h.chad.popularmoviesone.utils;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.h.chad.popularmoviesone.MainActivity;
import com.h.chad.popularmoviesone.R;
import com.squareup.picasso.Request;
import com.squareup.picasso.Request.Builder;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.R.id.list;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by chad on 5/8/2017.
 */

public class NetworkUtils {
    //Key1 and Key2 can be aquired from api.themoviedb.org
    //API Key (v3 auth)

    //API Redeacted Access Token (v4 auth)

    //example api request
    //https://api.themoviedb.org/3/movie/550?api_key=KEY1

    //example api for popular
    // https://api.themoviedb.org/3/movie/popular?api_key=KEY1&language=en-US&page=1

    //example api for top_rated
    // https://api.themoviedb.org/3/movie/top_rated?api_key=KEY1&language=en-US&page=1

    //example of movie poster
    // https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg

    private static final String LOG_TAG = NetworkUtils.class.getName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";



    private static final String LANGUAGE = "en-US";

    //private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    //private static final String POPULAR_URL = BASE_URL + POPULAR;
    //private static final String TOP_RATED_URL = BASE_URL + TOP_RATED;


    //Params for the API call
    private static final String API_PARAM = "api_key";
    private static final String LANGUAGE_PARAM = "language";
    private static final String PAGE_PARAM = "page";

    //Empty Constructor
    public NetworkUtils(){

    }

    public static URL getMoviesUrl (int page, String apiKeyValue, String listType){
        String fullUrl = BASE_URL + listType;
        if(page <= 0){
            page = 1;
        }
         Uri getMovies =  Uri.parse(fullUrl).buildUpon()
                .appendQueryParameter(API_PARAM, apiKeyValue)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, Integer.toString(page))
                .build();
        URL url = null;
        try {
            url = new URL( getMovies.toString());

        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.e(LOG_TAG, "buildPopularUrl has an error: " + url);
        }
        return url;
    }

    public static String getResponse(URL url) throws IOException{

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream is = urlConnection.getInputStream();

            Scanner scan = new Scanner(is);
            scan.useDelimiter("\\A");

            boolean hasInput = scan.hasNext();
            if(hasInput){

                return scan.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}
