package com.example.demofinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demofinal.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView imdbRatingTextView;
    private TextView yearTextView;
    private TextView descriptionTextView;
    private ImageView posterImageView;
    private Button favoriteButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

        titleTextView = findViewById(R.id.textViewTitle);
        imdbRatingTextView = findViewById(R.id.textViewRating);
        yearTextView = findViewById(R.id.textViewYear);
        descriptionTextView = findViewById(R.id.textViewDescription);
        posterImageView = findViewById(R.id.imageViewPoster);
        favoriteButton = findViewById(R.id.buttonFavorite);

        Movie movie = (Movie) getIntent().getSerializableExtra("MOVIE");

        if (movie != null) {
            titleTextView.setText(movie.getTitle());
            imdbRatingTextView.setText("IMDB: " + movie.getImdbRating());
            yearTextView.setText(movie.getYear());
            descriptionTextView.setText(movie.getDescription());

            if (movie.getPoster() != null && !movie.getPoster().isEmpty()) {
                Picasso.get()
                        .load(movie.getPoster())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(posterImageView);
            } else {
                posterImageView.setImageResource(R.drawable.placeholder);
            }

            favoriteButton.setOnClickListener(v -> addToFavorites(movie));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorites(Movie movie) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query(DatabaseHelper.TABLE_FAVORITES, null, DatabaseHelper.COLUMN_IMDB_ID + "=?", new String[]{movie.getImdbId()}, null, null, null);
                boolean exists = cursor.getCount() > 0;
                cursor.close();
                db.close();
                return exists;
            }

            @Override
            protected void onPostExecute(Boolean exists) {
                if (!exists) {
                    new AddFavoriteTask().execute(movie);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Movie is already in favorites", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private class AddFavoriteTask extends AsyncTask<Movie, Void, Void> {
        @Override
        protected Void doInBackground(Movie... movies) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TITLE, movies[0].getTitle());
            values.put(DatabaseHelper.COLUMN_RATING, movies[0].getImdbRating());
            values.put(DatabaseHelper.COLUMN_YEAR, movies[0].getYear());
            values.put(DatabaseHelper.COLUMN_DESCRIPTION, movies[0].getDescription());
            values.put(DatabaseHelper.COLUMN_POSTER_URL, movies[0].getPoster());
            values.put(DatabaseHelper.COLUMN_IMDB_ID, movies[0].getImdbId());

            db.insert(DatabaseHelper.TABLE_FAVORITES, null, values);
            db.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MovieDetailsActivity.this, "Movie added to favorites", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MovieDetailsActivity.this, FavoritesActivity.class);
            startActivity(intent);
        }
    }
}
