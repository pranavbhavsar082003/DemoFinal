package com.example.demofinal.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {
    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("imdbRating")
    private String imdbRating;

    @SerializedName("Poster")
    private String poster;

    @SerializedName("imdbID")
    private String imdbId;

    @SerializedName("Plot")
    private String description;

    public Movie(String title, String year, String imdbRating, String imdbId, String poster, String description) {
        this.title = title;
        this.year = year;
        this.imdbRating = imdbRating;
        this.imdbId = imdbId;
        this.poster = poster;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
