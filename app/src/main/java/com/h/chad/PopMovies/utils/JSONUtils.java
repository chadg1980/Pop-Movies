package com.h.chad.PopMovies.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.h.chad.PopMovies.Movie;
import com.h.chad.PopMovies.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


/**
 * Created by chad on 5/8/2017.
 */

public final class JSONUtils {

    private final static String LOG_TAG = JSONUtils.class.getName();

    public static ArrayList<Movie> getSimpleStringFromJson(Context context, String jsonString)
            throws JSONException{

        ArrayList<Movie> movieArrayList= new ArrayList<>();
        if(TextUtils.isEmpty(jsonString)){
            return movieArrayList;
        }
        try{
            JSONObject baseResponse = new JSONObject(jsonString);

            JSONArray resultsArray = null;
            if(baseResponse.has("results")){
                resultsArray = baseResponse.getJSONArray("results");
            }else{
                return movieArrayList;
            }
            /*
            * from the base API we get
            * int movieID         - Which is needed to receive posters, trailers and reviews
            * String title        - used in displayed in movie_detail_item
            * String release_date - used in displayed in movie_detail_item
            * String posterPath   - used to get the poster image
            * int voteCount       - currently unused
            * double voteAverage  - used in displayed in movie_detail_item
            * double popularity   - currently unused
            * String plot         - used in displayed in movie_detail_item
            *
            * */

            for(int i = 0; i < resultsArray.length(); i++){

                JSONObject movie = resultsArray.getJSONObject(i);
                int movieID = movie.getInt("id");
                String title = movie.getString("title");
                String releaseDate = movie.getString("release_date");
                String posterPath = movie.getString("poster_path");
                int voteCount = movie.getInt("vote_count");
                double voteAverage = movie.getDouble("vote_average");
                double popularity = movie.getDouble("popularity");
                String plot = movie.getString("overview");
                Movie newMovie = new Movie(movieID, title, releaseDate, posterPath,
                        voteCount, voteAverage, popularity, plot);
                movieArrayList.add(newMovie);

            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e(LOG_TAG, "Error Parsing JSON");
        }
        return movieArrayList;
    }

    public static ArrayList<String> youtubeKeyUrlString(String jsonString) throws JSONException {
        ArrayList<String> youtubeKeyUrlArray = new ArrayList<>();
        if (TextUtils.isEmpty(jsonString)) {
            return youtubeKeyUrlArray;
        }
        try {
            JSONObject baseResponse = new JSONObject(jsonString);
            JSONArray resultsArray = null;
            if (baseResponse.has("results")) {
                resultsArray = baseResponse.getJSONArray("results");
            } else {
                return youtubeKeyUrlArray;
            }
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject trailer = resultsArray.getJSONObject(i);
                String type = trailer.getString("type");
                if (type.equals("Trailer")) {
                    String youtubeKey = trailer.getString("key");
                    youtubeKeyUrlArray.add(youtubeKey);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error with the youtubeKey JSON");
        }
        return youtubeKeyUrlArray;
    }

    public static ArrayList<Review> getReviewFromJson(Context context, String jsonString)
            throws JSONException {
            ArrayList<Review> reviews = new ArrayList<>();

        return reviews;
    }
}
