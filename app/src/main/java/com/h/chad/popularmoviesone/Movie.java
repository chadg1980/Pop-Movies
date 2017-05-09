package com.h.chad.popularmoviesone;

import android.media.Image;

/**
 * Created by chad on 5/8/2017.
 */

public class Movie {

    private String title;
    private String releaseDate;
    private byte[] poster;
    String vote;
    String plot;
    public Movie(String title, String releaseDate, byte[] poster, String vote, String plot){
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.vote = vote;
        this.plot = plot;
    }
}
