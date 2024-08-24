package com.example.demofinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demofinal.adapters.MovieAdapter;
import com.example.demofinal.models.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();
    private EditText searchField;
    private Button searchButton;
    private ProgressBar progressBar;
    private TextView noResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);

        recyclerView = findViewById(R.id.recyclerViewMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MovieAdapter(movieList, movie -> {
            Intent intent = new Intent(MovieSearchActivity.this, MovieDetailsActivity.class);
            intent.putExtra("MOVIE", movie);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        searchField = findViewById(R.id.searchField);
        searchButton = findViewById(R.id.searchButton);
        progressBar = findViewById(R.id.progressBar);
        noResultsTextView = findViewById(R.id.noResultsTextView);
        noResultsTextView.setVisibility(View.GONE);

        searchButton.setOnClickListener(v -> {
            String query = searchField.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                fetchMovies(query);
            }
        });
    }

    private void fetchMovies(String query) {
        progressBar.setVisibility(View.VISIBLE);
        noResultsTextView.setVisibility(View.GONE);

        OMDBApi.searchMovies(query, movies -> {
            progressBar.setVisibility(View.GONE);
            if (movies != null && !movies.isEmpty()) {
                movieList.clear();
                movieList.addAll(movies);
                adapter.notifyDataSetChanged();
            } else {
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText("No results found. Please try a different search.");
            }
        }, error -> {
            progressBar.setVisibility(View.GONE);
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText("An error occurred. Please try again.");
        });
    }
}
