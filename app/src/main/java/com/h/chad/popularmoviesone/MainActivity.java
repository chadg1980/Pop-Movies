package com.h.chad.popularmoviesone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.h.chad.popularmoviesone.utils.ItemDecoration;
import com.h.chad.popularmoviesone.utils.JSONUtils;
import com.h.chad.popularmoviesone.utils.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import static com.h.chad.popularmoviesone.utils.NetworkUtils.getResponse;


//notes for picasso
//Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_bar);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Movies);

        //Stetho debug tools
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                .build());
        //END Stetho debug tools

        //Check for a network connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected =    activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            showNetworkError();

        }else {
            //END of connection check

            /*LinearLayoutManager layoutManager =
                    new GridLayoutManager(mRecyclerView.getContext(), 2);
            */
            GridLayoutManager layoutManager =
                    new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
            int spacingInPx = 5;
            mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPx));
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            loadMovies();
        }
    }
    private void loadMovies(){
        showData();
        //TODO 1 switch between POPULAR and TOP_RATED

        new FetchMovieTask().execute();
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    */

    private void showData(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
    private void showNetworkError(){
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessage.setText(R.string.network_error);
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
            URL movieRequestUrl;

            StringBuilder jsonMovieMultiPages = new StringBuilder();
            ArrayList<Movie> simpleJsonMovieData = new ArrayList<>();
            for(int page = 1; page<5;page++){
                movieRequestUrl = NetworkUtils.popularUrl(page);

                try{

                    String jsonMovieResponse = NetworkUtils.getResponse(movieRequestUrl);
                    if(page == 1) {
                        simpleJsonMovieData =
                                JSONUtils.getSimpleStringFromJson(MainActivity.this, jsonMovieResponse);
                    }else{
                        simpleJsonMovieData.addAll(JSONUtils.getSimpleStringFromJson(MainActivity.this, jsonMovieResponse));
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
                mMovieAdapter = new MovieAdapter(movies);
                mRecyclerView.setAdapter(mMovieAdapter);
            }
        }
    }
}
