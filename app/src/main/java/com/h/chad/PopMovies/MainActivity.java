package com.h.chad.PopMovies;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.facebook.stetho.Stetho;
import com.facebook.stetho.inspector.protocol.module.Database;
import com.h.chad.PopMovies.utils.FetchMovieTask;
import com.h.chad.PopMovies.utils.ItemDecoration;
import com.h.chad.PopMovies.utils.NetworkUtils;
import com.h.chad.PopMovies.data.FavoritesContract.FavoritesEntry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.width;
import static android.R.attr.x;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getName();

    private RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_bar) ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;

    private Context mContext;
    private String mNetworkErrorMessage;
    private boolean mHasKey;
    private static final int URL_LOADER = 0;
    boolean mFavoritesIsActive;
    private String mListType;
    MovieAdapter mMovieAdapter;

    //Constance for savedInstanceStates Keys
    private String SAVED_FAVORITE_BOOL = "isFavoriteActive";
    private String SAVED_LIST_TYPE = "listType";

    //Key is redcted for security purposes.
    //A key can be aquired at https://www.themoviedb.org/documentation/api
    //private String mApiPrivateKey;
    private String mNoKeyErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mFavoritesIsActive = savedInstanceState.getBoolean(SAVED_FAVORITE_BOOL);
            mListType = savedInstanceState.getString(SAVED_LIST_TYPE);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_Movies);
        mHasKey = true;
        mNetworkErrorMessage = mContext.getString(R.string.network_error);
        mNoKeyErrorMessage = mContext.getString(R.string.no_key_error);

        //This value will be redacted
        //To use this app, you will need to find a key at
        //https://www.themoviedb.org/documentation/api
        //Stop everything and check for an API key
        //Without an API key the rest of the app doesn't work.
        String apiPrivateKey = mContext.getString(R.string.KEY1);
        if (apiPrivateKey.length() != 32) {
            mHasKey = false;
            showSpecificError(mNoKeyErrorMessage);
        } else {

            if (mListType == null) {
                //making popular by default so no null values get sent to the API call
                mListType = NetworkUtils.POPULAR;
            }

            //Stetho debug tools
        /*
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                        .build());
                        */
            //END Stetho debug tools

            //Check for a network connection
            //Favorite is a database call, so if favortites is active, then we don't need to show
            //a network error.
            if (mFavoritesIsActive) {
                setupRecyclerView();
                loadMoviesFavoriteMoviesFromCursor();
            } else {
                boolean isConnected = checkConnection();
                if (!isConnected && !mFavoritesIsActive) {
                    showSpecificError(mNetworkErrorMessage);

                } else {
                    //If connection is good, we set up the recycler view then load the movies
                    setupRecyclerView();
                    loadMovies();
                }
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
    /**
     * This method sets up the recycler view,
     * Can be called from the Menu when the type of list is changed
     * and called from the oncreate method
     * */
    private void setupRecyclerView(){

        //Check if the the screen is vertical or horizontal
        //Adjust the grid for each.
        int rotation = getResources().getConfiguration().orientation;

        //check if it is a tablet, if pixels are over 800
        RelativeLayout detail = (RelativeLayout) findViewById(R.id.rl_main);
        boolean istablet = false;
        String viewType = (String) detail.getTag();

        if (viewType.equals(getString(R.string.tablet)))
            istablet = true;

        if(rotation == getResources().getConfiguration().ORIENTATION_PORTRAIT
                && !istablet) {
            //handsets in Portrait
            GridLayoutManager layoutManager =
                    new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
            int spacingInPx = 5;
            mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPx));
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
        }else if (rotation == getResources().getConfiguration().ORIENTATION_LANDSCAPE
                && !istablet) {
            //handsets in Landscape
            GridLayoutManager layoutManager =
                    new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
            int spacingInPx = 5;
            mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPx));
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
        } else if(rotation == getResources().getConfiguration().ORIENTATION_PORTRAIT
                && istablet) {
            //Tablets in Portrait
            GridLayoutManager layoutManager =
                    new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
            int spacingInPx = 10;
            mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPx));
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

        }else{
            //tablets in Portrait
            GridLayoutManager layoutManager =
                    new GridLayoutManager(this, 8, GridLayoutManager.VERTICAL, false);
            int spacingInPx = 10;
            mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPx));
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

        }
    }
    private void loadMovies(){
        showData();
        new FetchMovieTask(mContext, mLoadingIndicator, mRecyclerView, mListType).execute();
    }
    private void loadMoviesFavoriteMoviesFromCursor(){
        showData();
        getSupportLoaderManager().initLoader(URL_LOADER, null, this);
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
                mFavoritesIsActive = false;
                break;
            case R.id.sortTopRated:
                mListType = NetworkUtils.TOP_RATED;
                mFavoritesIsActive = false;
                break;
            case R.id.sortByFavorite:
                mListType = NetworkUtils.FAVORITE;
                mFavoritesIsActive = true;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        boolean isConnected = checkConnection();

        if(mFavoritesIsActive){
          loadMoviesFavoriteMoviesFromCursor();
        }else {
            if (!mHasKey) {
                showSpecificError(mNoKeyErrorMessage);
            } else if (!isConnected) {
                showSpecificError(mNetworkErrorMessage);
            } else {
                loadMovies();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    //Shows the data, makes errormessage invisible
    public void showData(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    //shows a secific error message
    private void showSpecificError(String errorType){
        mLoadingIndicator.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessage.setText(errorType);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri favoriteUri = FavoritesEntry.CONTENT_URI;

        String []projection = {
                FavoritesEntry._ID,
                FavoritesEntry.MOVIE_ID,
                FavoritesEntry.TITLE,
                FavoritesEntry.RELEASE_DATE,
                FavoritesEntry.POSTER_PATH,
                FavoritesEntry.VOTE_COUNT,
                FavoritesEntry.VOTE_AVERAGE,
                FavoritesEntry.PLOT
        };
        return new CursorLoader(
                mContext,
                favoriteUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        if( (cursor == null) || (cursor.getCount() <=0) ){
            return;
        }
        ArrayList<Movie> moviesArrayList = new ArrayList<>();
        cursor.moveToFirst();

        if (cursor.moveToFirst()) do{
            int movieIdColumnIndex = cursor.getColumnIndex(FavoritesEntry.MOVIE_ID);
            int movieTitleColumnIndex = cursor.getColumnIndex(FavoritesEntry.TITLE);
            int releaseDateColumnIndex = cursor.getColumnIndex(FavoritesEntry.RELEASE_DATE);
            int posterPathColumnIndex = cursor.getColumnIndex(FavoritesEntry.POSTER_PATH);
            int voteCountColumnIndex = cursor.getColumnIndex(FavoritesEntry.VOTE_COUNT);
            int voteAverageColumnIndex = cursor.getColumnIndex(FavoritesEntry.VOTE_AVERAGE);
            int plotColumnIndex = cursor.getColumnIndex(FavoritesEntry.PLOT);

            int movieId = cursor.getInt(movieIdColumnIndex);
            String movieTitle = cursor.getString(movieTitleColumnIndex);
            String releaseDate = cursor.getString(releaseDateColumnIndex);
            String posterPath = cursor.getString(posterPathColumnIndex);
            int voteCount = cursor.getInt(voteCountColumnIndex);
            double voteAverage = cursor.getDouble(voteAverageColumnIndex);
            String plot = cursor.getString(plotColumnIndex);
            Movie movie = new Movie(movieId, movieTitle, releaseDate, posterPath,
                    voteCount, voteAverage, plot);
            moviesArrayList.add(movie);

        }while(cursor.moveToNext());

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieAdapter= new MovieAdapter(moviesArrayList, mContext);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);


    }
    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
        Log.i(LOG_TAG, "Loader Reset");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_FAVORITE_BOOL, mFavoritesIsActive);
        outState.putString(SAVED_LIST_TYPE, mListType);

    }


}
