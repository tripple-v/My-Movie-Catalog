package com.vwuilbea.mymoviecatalog.tmdb.responses.search;

import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin on 04/09/2014.
 */
public class SearchSeriesResponse {

    private static final String LOG = SearchSeriesResponse.class.getSimpleName();

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_TOTAL_PAGE = "total_pages";
    private static final String PARAM_TOTAL_RESULTS = "total_results";
    private static final String PARAM_RESULTS = "results";

    private static final String PARAM_RESULTS_BACKDROP_PATH = "backdrop_path";
    private static final String PARAM_RESULTS_POSTER_PATH = "poster_path";
    private static final String PARAM_RESULTS_ID = "id";
    private static final String PARAM_RESULTS_TITLE = "name";
    private static final String PARAM_RESULTS_ORIGINAL_TITLE = "original_name";
    private static final String PARAM_RESULTS_RELEASE_DATE = "first_air_date";
    private static final String PARAM_RESULTS_ORIGIN_COUNTRY = "origin_country";
    private static final String PARAM_RESULTS_POPULARITY = "popularity";
    private static final String PARAM_RESULTS_VOTE_AVERAGE = "vote_average";
    private static final String PARAM_RESULTS_VOTE_COUNT = "vote_count";

    private int page;
    private int totalPages;
    private int totalResults;
    private List<Series> series = new ArrayList<Series>();

    public SearchSeriesResponse(String result) {
        try {
            JSONObject object = new JSONObject(result);
            this.page = object.getInt(PARAM_PAGE);
            this.totalPages = object.getInt(PARAM_TOTAL_PAGE);
            this.totalResults = object.getInt(PARAM_TOTAL_RESULTS);
            JSONArray array = object.getJSONArray(PARAM_RESULTS);
            for (int i = 0; i < array.length(); i++)
                series.add(getSeriesFromJSON(array.getJSONObject(i)));
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

    public List<Series> getSeries() {
        return series;
    }

    public static Series getSeriesFromJSON(JSONObject object) {
        try {
            Series series = new Series(object.getInt(PARAM_RESULTS_ID));
            series.setCoverPath(object.getString(PARAM_RESULTS_BACKDROP_PATH));
            series.setPosterPath(object.getString(PARAM_RESULTS_POSTER_PATH));
            series.setTitle(object.getString(PARAM_RESULTS_TITLE));
            series.setOriginalTitle(object.getString(PARAM_RESULTS_ORIGINAL_TITLE));
            series.setReleaseDate(object.getString(PARAM_RESULTS_RELEASE_DATE));
            series.setVoteAverage((float) object.getDouble(PARAM_RESULTS_VOTE_AVERAGE));
            series.setVoteCount(object.getInt(PARAM_RESULTS_VOTE_COUNT));
            return series;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
