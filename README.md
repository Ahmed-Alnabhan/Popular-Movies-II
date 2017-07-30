# Popular-Movies-II
This app is the stage 2 of the PopularMovies project in Nanodegree Android Developer program. To use this app, you need to add your own API_KEY to the file "gradle.properties" as a value of api_key entry.
Note: the provided api_key is just an example.

## App Description
An Android app, optimized for tablets and all screen sizes, to help users discover popular and top rated movies on the web. It displays a scrolling grid of movie posters, launches a details screen whenever a particular movie is selected, allows users to save favorites, play trailers, and read user reviews. This app uses RESTful APIs to retrieve movie data from themoviedb.org.

## Technologies/Libraries used 
The app uses the following components and libraries:

  * <strong>SQLite</strong> and <strong>ContentProvider</strong> to save and retrieve the favorite movies.
  * <strong>Retrofit</strong> to connect, fetch and retrieve movies data from the backend service.
  * <strong>Picasso</strong> to handle images.
  * <strong>RecyclerView</strong> to improve the UI performance.
  * <strong>SharedPreference</strong> to save and restore the app settings.
  
## App Usage
Following Youtube video and GIF images show how to use the app:
  * Please watch the use of the the app. </br>
[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/d4vm04JTOPo/0.jpg)](http://www.youtube.com/watch?v=d4vm04JTOPo)

  * Selecting and displaying the top-rated movies list from the Settings menu. </br>
![](https://image.ibb.co/dRPCh5/20170728_234619.gif)

  * Adding a movie to the Favorite list. </br>
![](https://image.ibb.co/mrXXh5/20170728_234323.gif)

  * Selecting and displaying the fovarite movies from the Settings menu. </br>
![](https://image.ibb.co/hDFJN5/20170728_234738.gif)
