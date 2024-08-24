package com.example.demofinal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demofinal.R;
import com.example.demofinal.models.Movie;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movies;
    private final OnItemClickListener clickListener;
    private final OnRemoveClickListener removeClickListener;
    private final boolean isRemovable;

    // Constructor for items with removal functionality
    public MovieAdapter(List<Movie> movies, OnItemClickListener clickListener, OnRemoveClickListener removeClickListener) {
        this.movies = movies;
        this.clickListener = clickListener;
        this.removeClickListener = removeClickListener;
        this.isRemovable = true;
    }

    // Constructor for items without removal functionality
    public MovieAdapter(List<Movie> movies, OnItemClickListener clickListener) {
        this.movies = movies;
        this.clickListener = clickListener;
        this.removeClickListener = null; // No removal functionality
        this.isRemovable = false;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie, clickListener, isRemovable ? removeClickListener : null);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(Movie movie);
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView ratingTextView;
        private final TextView yearTextView;
        private final ImageView posterImageView;
        private final Button removeButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            ratingTextView = itemView.findViewById(R.id.textViewRating);
            yearTextView = itemView.findViewById(R.id.textViewYear);
            posterImageView = itemView.findViewById(R.id.imageViewPoster);
            removeButton = itemView.findViewById(R.id.buttonRemove);
        }

        public void bind(Movie movie, OnItemClickListener clickListener, OnRemoveClickListener removeClickListener) {
            titleTextView.setText(movie.getTitle());
            ratingTextView.setText("IMDB: " + movie.getImdbRating());
            yearTextView.setText(movie.getYear());

            if (posterImageView != null && movie.getPoster() != null) {
                Picasso.get()
                        .load(movie.getPoster())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(posterImageView);
            }

            itemView.setOnClickListener(v -> clickListener.onItemClick(movie));

            if (removeClickListener != null) {
                removeButton.setVisibility(View.VISIBLE);
                removeButton.setOnClickListener(v -> removeClickListener.onRemoveClick(movie));
            } else {
                removeButton.setVisibility(View.GONE);
            }
        }
    }
}
