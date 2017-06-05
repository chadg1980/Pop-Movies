package com.h.chad.PopMovies;

/**
 * Created by chad on 6/5/2017.
 */

public class Review {

    private String mAuthor;
    private String mContent;

    public Review(String author, String content){
        this.mAuthor = author;
        this.mContent = content;

    }

    public String getAuthor(){
        return mAuthor;
    }
    public String getContent(){
        return mContent;
    }
}
