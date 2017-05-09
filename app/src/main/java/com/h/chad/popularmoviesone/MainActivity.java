package com.h.chad.popularmoviesone;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.h.chad.popularmoviesone.utils.JSONUtils;
import com.h.chad.popularmoviesone.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;


//notes for picasso
//Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mRecyclerView = (RecyclerView) findViewById(R.id.rv_Movies);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setHasFixedSize(true);

        //mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_bar);

        loadMovies();

    }

    private void loadMovies(){
        //showData();
        //TODO 1 switch between POPULAR and TOP_RATED

        new FetchMovieTask().execute("1");
    }

    private void showData(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        //mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage(){
        //mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if(params.length == 0){
                params[0] = "1";
            }

            String pageString = params[0];

            URL movieRequestUrl = NetworkUtils.popularUrl(Integer.valueOf(pageString));

            try {
                String jsonMovieResponse = NetworkUtils.getResponse(movieRequestUrl);
                ArrayList<Movie> simpleJsonMovieData =
                        JSONUtils.getSimpleStringFromJson(MainActivity.this, jsonMovieResponse);
                return simpleJsonMovieData;

            }catch (Exception e){
                e.printStackTrace();
                Log.e(LOG_TAG, "Fetch Error ");
                return null;
            }
        }
    }
}
