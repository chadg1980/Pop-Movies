package com.h.chad.PopMovies.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.h.chad.PopMovies.Review;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by chad on 6/5/2017.
 */

public class FetchReviewTask extends AsyncTask<URL, Void, ArrayList<Review>>{
    private final static String LOG_TAG = FetchReviewTask.class.getName();
    Context mContext;
    public FetchReviewTask(Context context){
        this.mContext = context;
    }

    @Override
    protected ArrayList<Review> doInBackground(URL... params) {
        if(params.length < 0){
            Log.e(LOG_TAG, "no URL passed to fetch");
            return null;
        }
        URL reviewUrl = params[0];

        ArrayList<Review> reviewsJsonData = new ArrayList<>();
        try{
            String jsonReviewResponse = NetworkUtils.getResponse(reviewUrl);
            reviewsJsonData = JSONUtils.getReviewFromJson(mContext, jsonReviewResponse);

        }catch (Exception e){
            Log.e(LOG_TAG, "review Url error", e);
        }
        return reviewsJsonData;
    }
}
