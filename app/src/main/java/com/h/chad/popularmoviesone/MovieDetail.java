package com.h.chad.popularmoviesone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.h.chad.popularmoviesone.MovieAdapter.MOVIE_TITLE;
import static com.h.chad.popularmoviesone.MovieAdapter.MOVIE_RELEASE_DATE;
import static com.h.chad.popularmoviesone.MovieAdapter.MOVIE_VOTE_AVERAGE;
import static com.h.chad.popularmoviesone.MovieAdapter.MOVIE_POSTER_PATH;
import static com.h.chad.popularmoviesone.MovieAdapter.MOVIE_PLOT;


/**
 * Created by chad on 5/11/2017.
 */

public class MovieDetail extends AppCompatActivity{

    TextView mTV_movieTitle;
    ImageView mIV_moviePoster;
    TextView mTV_movieReleaseDate;
    RatingBar mRB_movieVoteAverage;
    TextView mTV_movieVoteAverage;
    TextView mTV_moviePlot;
    private static final String DETAIL_IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_item);
        //Get the Intent that started the activity
        Intent intent = getIntent();
        Context context = this;
        String movieTitle = intent.getStringExtra(MOVIE_TITLE);
        String movieReleaseDate = intent.getStringExtra(MOVIE_RELEASE_DATE);
        String moviePosterPath = intent.getStringExtra(MOVIE_POSTER_PATH);
        String moviePlot = intent.getStringExtra(MOVIE_PLOT);
        Double movieVoteAverage = intent.getDoubleExtra(MOVIE_VOTE_AVERAGE, 0.0);

        String outOfTen = this.getString(R.string.average_out_of_ten);
        String averageVotes = Double.toString(movieVoteAverage) + outOfTen;

        mTV_movieTitle = (TextView)findViewById(R.id.tv_movie_title);
        mIV_moviePoster = (ImageView)findViewById(R.id.detail_poster);
        mTV_movieReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRB_movieVoteAverage = (RatingBar) findViewById(R.id.rb_vote_average);
        mTV_moviePlot = (TextView) findViewById(R.id.tv_plot);
        mTV_movieVoteAverage = (TextView) findViewById(R.id.tv_average);

        mTV_movieVoteAverage.setText(averageVotes);
        mTV_movieTitle.setText(movieTitle);
        mTV_movieReleaseDate.setText(parseYear(movieReleaseDate));
        mRB_movieVoteAverage.setRating( movieVoteAverage.floatValue() );
        mTV_moviePlot.setText(moviePlot);

        String totalUrl = MovieAdapter.IMAGE_URL + moviePosterPath;
        Picasso.with(context).load(totalUrl).into(mIV_moviePoster);


    }

    private String parseYear(String inDate){
        String outdate[] = inDate.split("-");
        return outdate[0];
    }
}
