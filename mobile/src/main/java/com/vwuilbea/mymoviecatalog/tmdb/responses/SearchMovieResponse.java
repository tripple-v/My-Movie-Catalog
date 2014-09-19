package com.vwuilbea.mymoviecatalog.tmdb.responses;

import com.vwuilbea.mymoviecatalog.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin on 04/09/2014.
 */
public class SearchMovieResponse {

    private static final String LOG = SearchMovieResponse.class.getSimpleName();

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_TOTAL_PAGE = "total_pages";
    private static final String PARAM_TOTAL_RESULTS = "total_results";
    private static final String PARAM_RESULTS = "results";

    private static final String PARAM_RESULTS_ADULT = "adult";
    private static final String PARAM_RESULTS_BACKDROP_PATH = "backdrop_path";
    private static final String PARAM_RESULTS_POSTER_PATH = "poster_path";
    private static final String PARAM_RESULTS_ID = "id";
    private static final String PARAM_RESULTS_TITLE = "title";
    private static final String PARAM_RESULTS_ORIGINAL_TITLE = "original_title";
    private static final String PARAM_RESULTS_RELEASE_DATE = "release_date";
    private static final String PARAM_RESULTS_POPULARITY = "popularity";
    private static final String PARAM_RESULTS_VOTE_AVERAGE = "vote_average";
    private static final String PARAM_RESULTS_VOTE_COUNT = "vote_count";

    private int page;
    private int totalPages;
    private int totalResults;
    private List<Movie> movies = new ArrayList<Movie>();

    public SearchMovieResponse(String result) {
        try {
            JSONObject object = new JSONObject(result);
            this.page = object.getInt(PARAM_PAGE);
            this.totalPages = object.getInt(PARAM_TOTAL_PAGE);
            this.totalResults = object.getInt(PARAM_TOTAL_RESULTS);
            JSONArray array = object.getJSONArray(PARAM_RESULTS);
            for (int i = 0; i < array.length(); i++)
                movies.add(getMovieFromJSON(array.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public static Movie getMovieFromJSON(JSONObject object) {
        try {
            Movie movie = new Movie(object.getInt(PARAM_RESULTS_ID));
            movie.setAdult(object.getBoolean(PARAM_RESULTS_ADULT));
            movie.setCoverPath(object.getString(PARAM_RESULTS_BACKDROP_PATH));
            movie.setPosterPath(object.getString(PARAM_RESULTS_POSTER_PATH));
            movie.setTitle(object.getString(PARAM_RESULTS_TITLE));
            movie.setOriginalTitle(object.getString(PARAM_RESULTS_ORIGINAL_TITLE));
            movie.setReleaseDate(object.getString(PARAM_RESULTS_RELEASE_DATE));
            movie.setVoteAverage(object.getDouble(PARAM_RESULTS_VOTE_AVERAGE));
            movie.setVoteCount(object.getInt(PARAM_RESULTS_VOTE_COUNT));
            return movie;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
