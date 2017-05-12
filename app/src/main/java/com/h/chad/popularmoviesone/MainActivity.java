package com.h.chad.popularmoviesone;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.h.chad.popularmoviesone.utils.ItemDecoration;
import com.h.chad.popularmoviesone.utils.JSONUtils;
import com.h.chad.popularmoviesone.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private String mListType;


    //Key is redcted for security purposes.
    // A key can be aquired at https://www.themoviedb.org/documentation/api
    private  String mApiPrivateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_bar);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Movies);
        //This value will be redacted
        //To use this app, you will need to find a key at
        //https://www.themoviedb.org/documentation/api
        mApiPrivateKey = context.getString(R.string.KEY1);
        //making popular by default so no null values get sent to the API call
        if(mListType == null){
            mListType = NetworkUtils.POPULAR;
        }


        //Stetho debug tools
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                .build());
        //END Stetho debug tools

        //Check for a network connection
        boolean isConnected = checkConnection();
        if(!isConnected){
            showNetworkError();

        }else {
            //END of connection check

            GridLayoutManager layoutManager =
                    new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
            int spacingInPx = 5;
            mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPx));
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            loadMovies();
        }
    }
    //Checks for a connection, used in onCreate and when the settings are switched
    boolean checkConnection(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return(activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
    private void loadMovies(){
        showData();
        new FetchMovieTask().execute();
    }

    //Create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
    //When a menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //The api can call popular or top rated
        switch (id){
            case R.id.sortPopular:
                mListType = NetworkUtils.POPULAR;
                break;
            case R.id.sortTopRated:
                mListType = NetworkUtils.TOP_RATED;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        boolean isConnected = checkConnection();
        if(isConnected)
            loadMovies();
        else
            showNetworkError();
        return super.onOptionsItemSelected(item);
    }
    //Shows the data, makes errormessage invisible
    private void showData(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    //Show the error message
    private void showErrorMessage(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
    //shows a network error message
    private void showNetworkError(){
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessage.setText(R.string.network_error);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
    //Async task call in a background thread
    private class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {
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
            for(int page = 1; page<5;page++){
                movieRequestUrl = NetworkUtils.getMoviesUrl(page, mApiPrivateKey, mListType);
                try{
                    String jsonMovieResponse = NetworkUtils.getResponse(movieRequestUrl);
                    if(page == 1) {
                        simpleJsonMovieData =
                                JSONUtils.getSimpleStringFromJson(
                                        MainActivity.this, jsonMovieResponse);
                    }else{
                        simpleJsonMovieData.addAll(
                                JSONUtils.getSimpleStringFromJson(
                                        MainActivity.this, jsonMovieResponse));
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
                showErrorMessage();
            }else {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mMovieAdapter = new MovieAdapter(movies, getApplicationContext());
                mRecyclerView.setAdapter(mMovieAdapter);
            }
        }
    }
}
