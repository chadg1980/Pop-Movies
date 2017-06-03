package com.h.chad.PopMovies.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by chad on 6/1/2017.
 */

public class FetchYoutubeKeys extends AsyncTask<URL, Void, ArrayList<String>> {
    private static final String LOG_TAG = FetchYoutubeKeys.class.getName();

    @Override
    protected ArrayList<String> doInBackground(URL... params) {
        if(params.length == 0){
            Log.e(LOG_TAG, "No URL passed in");
            return null;
        }
        URL VideoUrl = params[0];
        String thumbnailJsonResponse;
        ArrayList<String> youtubeKeysStringUrl = new ArrayList<>();
        try {
            thumbnailJsonResponse = NetworkUtils.getResponse(VideoUrl);
            youtubeKeysStringUrl = JSONUtils.youtubeKeyUrlString(thumbnailJsonResponse);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(LOG_TAG, "error fetching json data for youtubeKey");
        }
        return youtubeKeysStringUrl;
    }
}
