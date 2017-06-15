package com.h.chad.PopMovies.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.h.chad.PopMovies.MainActivity;
import com.h.chad.PopMovies.Movie;
import com.h.chad.PopMovies.MovieAdapter;
import com.h.chad.PopMovies.R;

import com.h.chad.PopMovies.MovieAdapter;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by chad on 5/24/2017.
 */



//Async task call in a background thread
//https://stackoverflow.com/questions/9963691/android-asynctask-sending-callbacks-to-ui
public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private ProgressBar mLoadingIndicator;
    private Context mMainContext;
    private String mApiPrivateKey;
    private String mListType;
    private RecyclerView mRecyclerView;
    private static final String LOG_TAG = FetchMovieTask.class.getName();
    private MovieAdapter mMovieAdapter;


    //Constructor
    public FetchMovieTask(Context context,ProgressBar progressBar, RecyclerView rv, String listType){

        //https://stackoverflow.com/questions/7836415/call-a-public-method-in-the-activity-class-from-another-class
        this.mLoadingIndicator = progressBar;
        this.mRecyclerView = rv;
        this.mMainContext = context;
        mApiPrivateKey = mMainContext.getString(R.string.KEY1);
        if(listType == null){
            mListType = NetworkUtils.POPULAR;
        }
        else{
            mListType = listType;
        }
    }
        @Override
    protected void onPreExecute(){
        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        URL movieRequestUrl;
        ArrayList<Movie> simpleJsonMovieData = new ArrayList<>();
        //We get the first 5 pages from the API
        //The more we get the lower quality poster we need to use
        //for(int page = 1; page<5;page++){
        //Changing to one page for testing
        for(int page = 1; page<2;page++){
            movieRequestUrl = NetworkUtils.getMoviesUrl(page, mApiPrivateKey, mListType);
            try{
                String jsonMovieResponse = NetworkUtils.getResponse(movieRequestUrl);
                if(page == 1) {
                    simpleJsonMovieData =
                            JSONUtils.getSimpleStringFromJson(
                                    mMainContext, jsonMovieResponse);
                }else{
                    simpleJsonMovieData.addAll(
                            JSONUtils.getSimpleStringFromJson(
                                    mMainContext, jsonMovieResponse));
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e(LOG_TAG, "Fetch Error ");
                return null;
            }
        }
        return simpleJsonMovieData;
    }

    protected void onPostExecute(ArrayList<Movie> movies){
        if(movies == null){
            //MainActivity.showErrorMessage();
        }else {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieAdapter = new MovieAdapter(movies, mMainContext.getApplicationContext());
            mRecyclerView.setItemViewCacheSize(20);
            mRecyclerView.setDrawingCacheEnabled(true);
            mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            mRecyclerView.setAdapter(mMovieAdapter);
        }
    }
}
