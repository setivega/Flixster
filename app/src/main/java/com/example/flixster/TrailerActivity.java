package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d(TAG, "Movie ID: " + movieID);

        MOVIE_URL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=0ec117989bb0ab0cca419bdc1adad681", movieID);

        Log.d(TAG, MOVIE_URL);

        client.get(MOVIE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject trailer = results.getJSONObject(0);
                    trailerKey = trailer.getString("key");
                    Log.d(TAG, "Inside Key: " + trailerKey);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON Exception ", e);
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (trailerKey != null) {
                    Log.d(TAG, "Trailer Key: " + trailerKey);
                    youTubePlayer.cueVideo(trailerKey);
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