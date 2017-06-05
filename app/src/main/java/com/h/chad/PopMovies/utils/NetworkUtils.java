package com.h.chad.PopMovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.R.attr.id;


/**
 * Created by chad on 5/8/2017.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String LANGUAGE = "en-US";
    private static final String VIDEO_TRAILER = "/videos";

    /*The two list types are public so other classes can use them*/
    public final static  String TOP_RATED = "top_rated";
    public static final  String POPULAR = "popular";

    /*The URL for getting the thumbnail
    * https://img.youtube.com/vi/<insert-youtube-video-id-here>/0.jpg
    * 0.jpg is player background thumbnail (480x360), I can see what size works best
    * https://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
    * */
    public static final String youtubeThumbnailUrlBase = "https://img.youtube.com/vi/";
    public static final String youtubeThumbnailEnd = "/0.jpg";

    /*
    * URL for watch youtube videos
    * */
    public static final String youtubeWatchUrlBase = "https://www.youtube.com/watch?v=";


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
            Log.e(LOG_TAG, "buildPopularUrl has an error");
        }
        return url;
    }

    public static URL getVideoUrl (String apiKeyValue, int id){
        String fullUrl = BASE_URL + id + VIDEO_TRAILER;
        Uri getMovies =  Uri.parse(fullUrl).buildUpon()
                .appendQueryParameter(API_PARAM, apiKeyValue)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .build();
        URL url = null;
        try {
            url = new URL( getMovies.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.e(LOG_TAG, "buildPopularUrl has an error");
        }
        return url;
    }

    public static String buildWatchUrl(String youtubeID) {
        return youtubeWatchUrlBase + youtubeID;
    }
    public static String buildThumbUrl(String youtubeID) {
        return youtubeThumbnailUrlBase +  youtubeID + youtubeThumbnailEnd;

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
