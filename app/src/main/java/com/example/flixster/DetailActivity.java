package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import android.view.View.OnClickListener;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_ID = "movieId";

    Movie movie;

    ImageView posterImageView;
    TextView titleTextView;
    TextView releaseTextView;
    TextView overviewTextView;
    RatingBar movieRatingBar;
    ImageView trailerImageView;
    ImageButton playImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        posterImageView = findViewById(R.id.posterImageView);
        titleTextView = findViewById(R.id.titleTextView);
        overviewTextView = findViewById(R.id.overviewTextView);
        movieRatingBar = findViewById(R.id.movieRatingBar);
        trailerImageView = findViewById(R.id.trailerImageView);
        releaseTextView = findViewById(R.id.releaseTextView);
        playImageButton = findViewById(R.id.playImageButton);


        // unwrap movie serialized on creation of detail activity
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("DetailActivity", String.format("Showing details for '%s'", movie.getTitle()));

        playImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the new activity
                Intent trailer = new Intent(DetailActivity.this, TrailerActivity.class);
                // pass the data being edited
                trailer.putExtra(MOVIE_ID, movie.getMovieId());
                Log.d("DetailActivity", "Movie ID: " + movie.getMovieId());
                // display the edit activity
                startActivity(trailer);
            }
        });

        titleTextView.setText(movie.getTitle());
        overviewTextView.setText(movie.getOverview());
        releaseTextView.setText(movie.getReleaseDate());

        float voteAverage = movie.getVoteAverage().floatValue();
        movieRatingBar.setRating(voteAverage / 2.0f);

        String imageUrl;
        int placeholder;
        Context context;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imageUrl = movie.getBackdropPath();
            placeholder = R.drawable.flicks_backdrop_placeholder;
            context = trailerImageView.getContext();
        } else {
            imageUrl = movie.getPosterPath();
            placeholder = R.drawable.flicks_movie_placeholder;
            context = posterImageView.getContext();
        }

        Glide.with(context).load(imageUrl)
                .placeholder(placeholder)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(posterImageView);

        Glide.with(trailerImageView.getContext()).load(movie.getBackdropPath())
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(trailerImageView);

    }

}