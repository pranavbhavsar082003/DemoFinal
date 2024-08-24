package com.example.demofinal;

import android.os.AsyncTask;

import com.example.demofinal.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OMDBApi {

    private static final String API_KEY = "cbf6184c";

    public static void searchMovies(String query, OMDBCallback<List<Movie>> callback, OMDBCallback<Exception> errorCallback) {
        new AsyncTask<Void, Void, List<Movie>>() {
            private Exception error;

            @Override
            protected List<Movie> doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://www.omdbapi.com/?s=" + query + "&apikey=" + API_KEY);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    Scanner scanner = new Scanner(connection.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.optJSONArray("Search");

                    List<Movie> movies = new ArrayList<>();
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject movieObj = jsonArray.getJSONObject(i);
                            String title = movieObj.optString("Title", "Unknown Title");
                            String year = movieObj.optString("Year", "Unknown Year");
                            String imdbId = movieObj.optString("imdbID");
                            String posterUrl = movieObj.optString("Poster", "");

                            Movie movie = fetchMovieDetails(imdbId, title, year, posterUrl);
                            movies.add(movie);
                        }
                    }
                    return movies;
                } catch (Exception e) {
                    error = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                if (movies != null) {
                    callback.onResponse(movies);
                } else {
                    errorCallback.onResponse(error);
                }
            }

            private Movie fetchMovieDetails(String imdbId, String title, String year, String posterUrl) {
                try {
                    URL detailUrl = new URL("https://www.omdbapi.com/?i=" + imdbId + "&apikey=" + API_KEY);
                    HttpURLConnection connection = (HttpURLConnection) detailUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    Scanner scanner = new Scanner(connection.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();

                    JSONObject movieObj = new JSONObject(response.toString());
                    String imdbRating = movieObj.optString("imdbRating", "N/A");
                    String description = movieObj.optString("Plot", "No Description");

                    return new Movie(title, year, imdbRating, imdbId, posterUrl, description);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Movie(title, year, "N/A", imdbId, posterUrl, "N/A");
                }
            }
        }.execute();
    }

    public interface OMDBCallback<T> {
        void onResponse(T response);
    }
}
