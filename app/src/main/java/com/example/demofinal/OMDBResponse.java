package com.example.demofinal;


import com.example.demofinal.models.Movie;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OMDBResponse {
    @SerializedName("Search")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}