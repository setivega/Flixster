package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    Movie movie;

    ImageView posterImageView;
    TextView titleTextView;
    TextView releaseTextView;
    TextView overviewTextView;
    RatingBar movieRatingBar;
    ImageView backdropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        posterImageView = findViewById(R.id.posterImageView);
        titleTextView = findViewById(R.id.titleTextView);
        overviewTextView = findViewById(R.id.overviewTextView);
        movieRatingBar = findViewById(R.id.movieRatingBar);
        backdropImageView = findViewById(R.id.imageView2);
        releaseTextView = findViewById(R.id.releaseTextView);


        // unwrap movie serialized on creation of detail activity
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("DetailActivity", String.format("Showing details for '%s'", movie.getTitle()));

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
            context = backdropImageView.getContext();
        } else {
            imageUrl = movie.getPosterPath();
            placeholder = R.drawable.flicks_movie_placeholder;
            context = posterImageView.getContext();
        }

        Glide.with(context).load(imageUrl)
                .placeholder(placeholder)
                .into(posterImageView);

        Glide.with(backdropImageView.getContext()).load(movie.getBackdropPath())
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .into(backdropImageView);

    }
}