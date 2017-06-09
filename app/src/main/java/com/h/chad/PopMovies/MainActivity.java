package com.h.chad.PopMovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.h.chad.PopMovies.MovieAdapter;
import com.h.chad.PopMovies.utils.FetchMovieTask;
import com.h.chad.PopMovies.utils.ItemDecoration;
import com.h.chad.PopMovies.utils.JSONUtils;
import com.h.chad.PopMovies.utils.NetworkUtils;
import com.h.chad.PopMovies.R;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private String mListType;
    private Context mContext;
    private String mNetworkErrorMessage;
    private boolean mHasKey;

    //Key is redcted for security purposes.
    // A key can be aquired at https://www.themoviedb.org/documentation/api
    private String mApiPrivateKey;
    private String mNoKeyErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();
        mContext = context;
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_bar);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Movies);
        mHasKey = true;
        mNetworkErrorMessage = mContext.getString(R.string.network_error);
        mNoKeyErrorMessage = mContext.getString(R.string.no_key_error);

        //This value will be redacted
        //To use this app, you will need to find a key at
        //https://www.themoviedb.org/documentation/api
        //Stop everything and check for an API key
        //Without an API key the rest of the app doesn't work.
        mApiPrivateKey = mContext.getString(R.string.KEY1);
        if (mApiPrivateKey.length() != 32) {
            mHasKey = false;
            showSpecificError(mNoKeyErrorMessage);
        } else {


            //making popular by default so no null values get sent to the API call
            if (mListType == null) {
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
            if (!isConnected) {
                showSpecificError(mNetworkErrorMessage);

            } else {
                //If connection is good, we set up the recycler view then load the movies
                setupRecyclerView();
                loadMovies();
            }
        }
    }
    //Checks for a connection, used in onCreate and when the settings are switched
    boolean checkConnection(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return(activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
    private void setupRecyclerView(){
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        int spacingInPx = 5;
        mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPx));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }
    private void loadMovies(){
        showData();

        new FetchMovieTask(mContext, mLoadingIndicator, mRecyclerView, mListType).execute();
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

        if(isConnected && mHasKey) {
            setupRecyclerView();
            loadMovies();
        }
        else {
            if(!isConnected)
                showSpecificError(mNetworkErrorMessage);
            if(!mHasKey)
                showSpecificError(mNoKeyErrorMessage);
        }
        return super.onOptionsItemSelected(item);
    }
    //Shows the data, makes errormessage invisible
    public void showData(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    //Show the error message
    public void showErrorMessage(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
    //shows a secific error message
    private void showSpecificError(String errorType){
        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessage.setText(errorType);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

}
