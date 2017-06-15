Pop Movies part 2 for Android, 
A learning Experience through Udacity's Android Developers NanoDegree

Pop Movies 
About:
Pop-Movies pulls data from themviedb API and returns with either Popular Movies or Highly Rated Movies, or movies that are added by the user to a database of favorite movies. The Movie poster can be clicked to show more information about the movie such as PLOT, Movie Title, Trailers, and reviews. 

What I learned in this project. 
1) making network calls, 
	Making API requests and getting JSON back is something I have done before, but this project I needed to make multiple request.
	One request for Popular Movies. A different request for Highly Rated movies. A seperate network request to get the keys for trailers so the trailers can be taken from Youtube. Another network call to Youtube to get thumbnail pictures. And finally a network call to get the reviews for the movie. 
	
2) UI and Design
	This was my first project making seperate layouts for Landscape vs Portrait. And another layout for tablet. One of my biggest challenges was the length of the Textview for Plot. Making a call to themoviedb, I did not know how long the plot was going to be. In Landscape and Tablet views I moved the plot from below the movie poster to the right of the movie poster so there wasn't as much white space. But if the plot was short the trailers got moved under the movie poster. I solved this by getting the about of lines of text in the plot, which can be different per each device. If there are not enough lines of text in the plot then the trailer view is attached to the bottom of the poster Imageview instead of the Plot Textview. Also this is my first project with ConstraintLayout which provided its own set of challenges.
	I built a function like this: 
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
3) Seperation of Concern
	This project I moved my Async Tasks to their own class, which I had to learn how to send layout UI object to the Fetch classes, because I needed to have the Asyc task update the UI. Sending Context to other classes was the most difficult part for me to wrap my head around, but after several trail and errors I figured it out. Context can be sent through the constructor.

4) Mixing API calls and Database Cursor Calls
	This part took me several hours of planning before I even wrote a line of code. There were several design options. I could have put all the movies on first load into the database, then only call the database for the adapter. This would make my database larger than my current design.  I could have written two apters, one for API calls using JSON and one for the Cursor from the database. I did not choose this design because it breaks the DRY rule. 
	The method I chose was to add cursor data to a ArrayList. I had already written and ArrayList<Movie> for the API call when the JSON data is returned, I already had an adapter that took the ArrayList<movie> as input, so I just added the cursor data to an ArrayList and sent it to the Adapter that was already built to accept an arraylist. 
	