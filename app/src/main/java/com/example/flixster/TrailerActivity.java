package com.example.flixster;


import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import okhttp3.Headers;

public class TrailerActivity extends YouTubeBaseActivity {

    public static final String TAG = "TrailerActivity";

    String MOVIE_URL;
    String trailerKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        Integer movieID = getIntent().getExtras().getInt(DetailActivity.MOVIE_ID);
        Log.d(TAG, "Movie ID: " + movieID);

        AsyncHttpClient client = new AsyncHttpClient();
        // Creating the movie url using the movieID we pass from the detail activity and the api key
        MOVIE_URL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s", movieID, getString(R.string.movie_api_key));
        Log.d(TAG, MOVIE_URL);

        // Setting up the JSON repsonse handler to parse our information from the api url
        client.get(MOVIE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    // Taking the results and setting them to an array of trailer json objects
                    JSONArray results = jsonObject.getJSONArray("results");
                    // Setting the first trailer in the array to a JsonObject variable
                    JSONObject trailer = results.getJSONObject(0);
                    // Taking the specific trailer key and setting it our trailerKey variable
                    trailerKey = trailer.getString("key");
                    Log.d(TAG, "Inside Function Key: " + trailerKey);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON Exception ", e);
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        // Creating the youtube player object
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // Initializing object using our api key
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (trailerKey != null) {
                    Log.d(TAG, "Outside Function Trailer Key: " + trailerKey);
                    // Starts the trailer video
                    youTubePlayer.cueVideo(trailerKey);
                    // Puts the trailer in full screen on activity launch
                    youTubePlayer.setFullscreen(true);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("TrailerActivity", "Error initializing YouTube player");
            }
        });
    }


}