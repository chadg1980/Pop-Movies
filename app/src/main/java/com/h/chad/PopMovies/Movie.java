package com.h.chad.PopMovies;

import android.media.Image;

/**
 * Created by chad on 5/8/2017.
 */

// todo 101 look into using a parcelabel
    //https://guides.codepath.com/android/using-parcelable
    //https://www.101apps.co.za/index.php/articles/using-android-s-parcelable-class-a-tutorial.html
    //blog.prolificinteractive.com/2014/07/18/why-we-love-parcelable/

public class Movie {

    private int movieID;
    private String title;
    private String releaseDate;
    private String posterPath;
    private int voteCount;
    private double voteAverage;
    private double popularity;
    private String plot;
    //All the data from the API that we may or may not need
    //I did not find the running time like the example
    public Movie(int movieID, String title, String releaseDate,
                 String posterPath, int voteCount, double voteAverage,
                 double popularity, String plot){
        this.movieID = movieID;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.plot = plot;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPlot() {
        return plot;
    }
}
