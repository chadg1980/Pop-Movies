package com.h.chad.PopMovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.h.chad.PopMovies.R;
import com.h.chad.PopMovies.data.FavoritesContentProvider;
import com.h.chad.PopMovies.data.FavoritesContract;
import com.h.chad.PopMovies.data.FavoritesDbHelper;
import com.h.chad.PopMovies.utils.FetchReviewTask;
import com.h.chad.PopMovies.utils.FetchYoutubeKeys;
import com.h.chad.PopMovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.duration;
import static android.R.attr.id;
import static android.R.attr.layout;
import static android.R.attr.rotation;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.h.chad.PopMovies.MovieAdapter.API_KEY;
import static com.h.chad.PopMovies.MovieAdapter.MOVIE_ID;
import static com.h.chad.PopMovies.MovieAdapter.MOVIE_PLOT;
import static com.h.chad.PopMovies.MovieAdapter.MOVIE_POSTER_PATH;
import static com.h.chad.PopMovies.MovieAdapter.MOVIE_RELEASE_DATE;
import static com.h.chad.PopMovies.MovieAdapter.MOVIE_TITLE;
import static com.h.chad.PopMovies.MovieAdapter.MOVIE_VOTE_AVERAGE;
import static com.h.chad.PopMovies.MovieAdapter.MOVIE_VOTE_COUNT;
import static com.h.chad.PopMovies.R.layout.trailer_item;
import static com.h.chad.PopMovies.data.FavoritesContract.FavoritesEntry;


/**
 * Created by chad on 5/11/2017.
 */

public class MovieDetailActivity extends AppCompatActivity {

    /*butterknife binding */

    @BindView(R.id.tv_movie_title) TextView mTV_movieTitle;
    @BindView(R.id.detail_poster)ImageView mIV_moviePoster;
    @BindView (R.id.tv_release_date)TextView mTV_movieReleaseDate;
    @BindView(R.id.rb_vote_average)RatingBar mRB_movieVoteAverage;
    @BindView(R.id.tv_average) TextView mTV_movieVoteAverage;
    @BindView(R.id.tv_plot) TextView mTV_moviePlot;
    @BindView(R.id.trailer_label) TextView mTrailerLabel;
    @BindView(R.id.trailer_line) View mTrailerLine;
    @BindView(R.id.tv_no_review) TextView mNoReview;
    @BindView(R.id.tb_add_favorite) ToggleButton mAddFavorite;
    @BindView(R.id.tv_no_trailer) TextView mNoTrailer;

    RecyclerView mRecyclerTrailer;
    RecyclerView getmRecyclerReview;

    private Context mContext;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

    private Uri mFavoriteUri;
    private boolean movieAlreadyFavorite;

    //values for db calls
    private int mMovieId;
    private String mMovieTitle;
    private String mReleaseDate;
    private String mPosterPath;
    private int mVoteCount;
    private Double mVoteAverage;
    private String mPlot;


    private final static String LOG_TAG = MovieDetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_item);
        ButterKnife.bind(this);
        //Get the Intent that started the activity
        Intent intent = getIntent();
        mContext = this;

        mRecyclerTrailer = (RecyclerView) findViewById(R.id.rv_trailers);
        mRecyclerTrailer.setLayoutManager(new LinearLayoutManager(this));

        getmRecyclerReview = (RecyclerView) findViewById(R.id.rv_reviews);
        getmRecyclerReview.setLayoutManager(new LinearLayoutManager(this));
        mMovieId = intent.getIntExtra(MOVIE_ID, -1);
        String apiKey = intent.getStringExtra(API_KEY);

        //Check if the movie is already in the databse if the item came from a non database area
        movieAlreadyFavorite = false;
        checkIfFavorite();



        mMovieTitle = intent.getStringExtra(MOVIE_TITLE);
        mReleaseDate = intent.getStringExtra(MOVIE_RELEASE_DATE);
        mPosterPath = intent.getStringExtra(MOVIE_POSTER_PATH);
        mVoteCount = intent.getIntExtra(MOVIE_VOTE_COUNT, 0);
        mVoteAverage = intent.getDoubleExtra(MOVIE_VOTE_AVERAGE, 0.0);
        mPlot = intent.getStringExtra(MOVIE_PLOT);

        String outOfTen = mContext.getString(R.string.average_out_of_ten);
        String averageVotes = Double.toString(mVoteAverage) + outOfTen;

        mTV_movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        mIV_moviePoster = (ImageView) findViewById(R.id.detail_poster);
        mTV_movieReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRB_movieVoteAverage = (RatingBar) findViewById(R.id.rb_vote_average);
        mTV_moviePlot = (TextView) findViewById(R.id.tv_plot);
        mTV_movieVoteAverage = (TextView) findViewById(R.id.tv_average);

        String totalUrl = com.h.chad.PopMovies.MovieAdapter.IMAGE_URL + mPosterPath;
        Picasso.with(mContext).load(totalUrl).into(mIV_moviePoster);
        mTV_movieVoteAverage.setText(averageVotes);
        mTV_movieTitle.setText(mMovieTitle);
        mTV_movieReleaseDate.setText(parseYear(mReleaseDate));
        mRB_movieVoteAverage.setRating(mVoteAverage.floatValue());
        mTV_moviePlot.setText(mPlot);
        getReviews(apiKey, mMovieId);
        getYoutubeKeys(apiKey, mMovieId);
        moveLineIfPlotIsLongInLandscape();

    }

    private void moveLineIfPlotIsLongInLandscape() {
        int rotation = getResources().getConfiguration().orientation;
        if(rotation == getResources().getConfiguration().ORIENTATION_LANDSCAPE ) {
            mTV_moviePlot.post(new Runnable() {
                @Override
                public void run() {
                    ConstraintLayout constrainLayout = (ConstraintLayout) findViewById(R.id.cl_movie_detail);
                    int linecount = mTV_moviePlot.getLineCount();
                    if (linecount <= 3) {
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                                mTrailerLine.getLayoutParams();

                        ConstraintSet set = new ConstraintSet();
                        set.clone(constrainLayout);
                        set.connect(mTrailerLine.getId(), ConstraintSet.TOP, mIV_moviePoster.getId(), ConstraintSet.BOTTOM, 40);
                        set.applyTo(constrainLayout);

                    }
                }
            });
        }

    }

    /**
     * Set up the toggle switch, if the movie_id is in the database, it will be set to on.
     * Also the _ID from the database table will be put in a variable so the proper
     * cursor will be deleted.
     * if the movie_id is not in the database, the toggle will be set to false
     * */
    private void checkIfFavorite() {

        ContentResolver mContentResolver = mContext.getContentResolver();
        String selection = FavoritesEntry.MOVIE_ID + " =?";
        String [] projection = {
                FavoritesEntry._ID,
                FavoritesEntry.MOVIE_ID
        };
        String[] args={Integer.toString(mMovieId)};
        Cursor c = mContentResolver.query(FavoritesEntry.CONTENT_URI,
                projection, selection,args, null);

        if(c.getCount() > 0){

            movieAlreadyFavorite = true;
            c.moveToFirst();
            long currentIndex = c.getLong(c.getColumnIndex(FavoritesEntry._ID));
            mFavoriteUri = ContentUris.withAppendedId(FavoritesEntry.CONTENT_URI, currentIndex);
            Log.e(LOG_TAG, "current item is " + mFavoriteUri);
            mAddFavorite.setTextOn("Favorite");

        }else {
            movieAlreadyFavorite = false;
            mFavoriteUri = null;
            mAddFavorite.setTextOff("Not favorite");
        }
        mAddFavorite.setChecked(movieAlreadyFavorite);
    }

    private void getYoutubeKeys(String apiKey, int movieId) {
        URL linkToVideos = NetworkUtils.getVideoUrl(apiKey, movieId);
        new FetchYoutubeKeys(){
            @Override
            protected void onPostExecute(ArrayList<String> results) {
                if(results.size() > 0) {
                    mNoTrailer.setVisibility(View.GONE);
                    mRecyclerTrailer.setVisibility(View.VISIBLE);
                    mTrailerAdapter = new TrailerAdapter(results, mContext);
                    mRecyclerTrailer.setItemViewCacheSize(20);
                    mRecyclerTrailer.setDrawingCacheEnabled(true);
                    mRecyclerTrailer.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    mRecyclerTrailer.setAdapter(mTrailerAdapter);
                }
            }
        }.execute(linkToVideos);
    }
    private void getReviews(String apiKey, int movieID){
        URL linkToReviews  = NetworkUtils.getReviewsUrl(apiKey, movieID, 1);
        new FetchReviewTask(mContext){
            @Override
            protected void onPostExecute(ArrayList<Review> reviews){
                if(reviews == null){
                    Log.e(LOG_TAG, "no reviews");
                }
                if(reviews.size() > 0) {
                    mNoReview.setVisibility(View.GONE);
                    getmRecyclerReview.setVisibility(View.VISIBLE);
                    mReviewAdapter = new ReviewAdapter(reviews, mContext);
                    getmRecyclerReview.setAdapter(mReviewAdapter);
                }
            }
        }.execute(linkToReviews);
    }
    private String parseYear(String inDate) {
        String outdate[] = inDate.split("-");

        return getString(R.string.release_year) + " " + outdate[0];
    }

    private void saveMovieToFavorite(ToggleButton tb_favorite){
            ContentValues values = new ContentValues();
            mAddFavorite.setTextOn("Favorite");
            values.put(FavoritesEntry.MOVIE_ID, mMovieId);
            values.put(FavoritesEntry.TITLE, mMovieTitle);
            values.put(FavoritesEntry.RELEASE_DATE, mReleaseDate);
            values.put(FavoritesEntry.POSTER_PATH, mPosterPath);
            values.put(FavoritesEntry.VOTE_COUNT, mVoteCount);
            values.put(FavoritesEntry.VOTE_AVERAGE, mVoteAverage);
            values.put(FavoritesEntry.PLOT, mPlot);
            Uri newFavoriteUri = getContentResolver().insert(FavoritesEntry.CONTENT_URI, values);
            if (newFavoriteUri == null) {
                Log.e(LOG_TAG, "URI " + newFavoriteUri + " Not added ");
            } else {
                Log.e(LOG_TAG, "URI " + newFavoriteUri + " added ");
            }
    }
    private void removeMovieFromFavorite(ToggleButton mAddFavorite) {
        mAddFavorite.setTextOff("add to favorite");
        int rowsDeleted = getContentResolver().delete(mFavoriteUri, null, null);

        if (rowsDeleted == 0) {
            Log.e(LOG_TAG, "ROWS DELETED " +rowsDeleted + " Nothing was removed");
        } else {
            Log.e(LOG_TAG, "ROWS DELETED  = " + rowsDeleted + ", movie removed from favorite ");
        }
    }
}

