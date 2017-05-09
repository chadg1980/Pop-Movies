package com.h.chad.popularmoviesone.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.h.chad.popularmoviesone.Movie;

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
            JSONArray moviesArray = null;
            Log.i(LOG_TAG, "base response " + baseResponse);

        }catch (JSONException e){
            e.printStackTrace();
            Log.e(LOG_TAG, "Error Parsing JSON");
        }
        return movieArrayList;
    }
}
