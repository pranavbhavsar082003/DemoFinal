package com.example.demofinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import com.google.android.material.appbar.MaterialToolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demofinal.adapters.MovieAdapter;
import com.example.demofinal.models.Movie;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> favoriteMovies;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        dbHelper = new DatabaseHelper(this);
        favoriteMovies = new ArrayList<>();
        loadFavoriteMovies();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(favoriteMovies, this::showMovieDetails, this::removeFavorite);
        recyclerView.setAdapter(adapter);

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(v -> onBackPressed());
    }



    private void loadFavoriteMovies() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(DatabaseHelper.TABLE_FAVORITES, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                String title = getColumnValue(cursor, DatabaseHelper.COLUMN_TITLE);
                String rating = getColumnValue(cursor, DatabaseHelper.COLUMN_RATING);
                String year = getColumnValue(cursor, DatabaseHelper.COLUMN_YEAR);
                String description = getColumnValue(cursor, DatabaseHelper.COLUMN_DESCRIPTION);
                String posterUrl = getColumnValue(cursor, DatabaseHelper.COLUMN_POSTER_URL);
                String imdbId = getColumnValue(cursor, DatabaseHelper.COLUMN_IMDB_ID);

                favoriteMovies.add(new Movie(title, year, rating, imdbId, posterUrl, description));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getColumnValue(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex != -1 ? cursor.getString(columnIndex) : "";
    }

    private void showMovieDetails(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("MOVIE", movie);
        startActivity(intent);
    }

    private void removeFavorite(Movie movie) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_FAVORITES, DatabaseHelper.COLUMN_IMDB_ID + "=?", new String[]{movie.getImdbId()});
        db.close();
        favoriteMovies.remove(movie);
        adapter.notifyDataSetChanged();
    }
}
