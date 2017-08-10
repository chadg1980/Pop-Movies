## Pop Movies part 2 for Android, 
Pop Movies is project for learning Experience through Udacity's Android Developers NanoDegree
Pop-Movies pulls data from themviedb API and returns with either Popular Movies or Highly Rated Movies, or movies that are added by the user to a database of favorite movies. The Movie poster can be clicked to show more information about the movie such as PLOT, Movie Title, Trailers, and reviews.

## usage
To get the code run
`$ git clone https://github.com/chadg1980/Pop-Movies.git`
Then open the project in Android Studio
Using this project requires an API key that you can get from  https://www.themoviedb.org/documentation/api
Once you have the API key it needs to be placed in app>res>values>strings.xml Place the key in  <string name="KEY1>


## What I learned in this project. 
1) 	Making multiple requests API request.  
  
	* One request for Popular Movies. 
	* A different request for Highly Rated movies. 
	* A separate network request to get the keys for trailers, so the trailers can be taken from Youtube. 
	* Another network call to Youtube to get thumbnail pictures. 
	* A network call to get the reviews for the movie. 
	
2) UI and Design
    * Making separate layouts for Landscape vs Portrait. 
    * And another layout for tablet. 
    * One of my biggest challenges was the length of the Textview for Plot. 
    
Making a call to themoviedb database, there was no way of knowing how much text was going to
 be passed from the database ahead of time. 
In Landscape and Tablet views the plot was moved from below the movie poster to the right of the 
movie poster so there wasn't as much white space. 
But if the plot was short the trailers got moved under the movie poster.  
The issue was solved by getting the about of lines of text in the plot, 
which can be different per each device.
 If there are not enough lines of text in the plot then the trailer view 
 is attached to the bottom of the poster Imageview instead of the Plot Textview. 
 	 
	 ```
	PlotTextViewt.post(new Runnable() {
                @Override
                public void run() {
                    int minLineCount = 4;
                    ConstraintLayout constrainLayout = (ConstraintLayout) findViewById(R.id.cl_movie_detail);
                    int linecount = PlotTextView.getLineCount();
                    //Change the constraints if there aren't enough lines of text to push the Trailers below the movie poster. 
					if (linecount < minLineCount) {
                        ConstraintSet set = new ConstraintSet();	//Get the constrainst in ConstraintLayout.
                        set.clone(constrainLayout);					//copy the constraints, From much StackOverflow reading
                        set.connect(mTrailerLine.getId(), ConstraintSet.TOP, mIV_moviePoster.getId(), ConstraintSet.BOTTOM, 40);
                        set.applyTo(constrainLayout);
                    }
                }
            });
            ```
3) Separation of Concern
    * Moving Async Tasks to their own class

	