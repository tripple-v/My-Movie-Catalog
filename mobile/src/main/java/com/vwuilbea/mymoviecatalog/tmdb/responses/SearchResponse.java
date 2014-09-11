package com.vwuilbea.mymoviecatalog.tmdb.responses;

import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Valentin on 04/09/2014.
 */
public class SearchResponse {

    private static final String LOG = SearchResponse.class.getSimpleName();

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
    private List<SearchResult> results;

    public SearchResponse(String result) {
        try {
            JSONObject object = new JSONObject(result);
            this.page = object.getInt(PARAM_PAGE);
            this.totalPages = object.getInt(PARAM_TOTAL_PAGE);
            this.totalResults = object.getInt(PARAM_TOTAL_RESULTS);
            this.results = new ArrayList<SearchResult>();
            JSONArray array = object.getJSONArray(PARAM_RESULTS);
            for (int i = 0; i < array.length(); i++)
                results.add(new SearchResult(array.getJSONObject(i)));
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

    public List<SearchResult> getResults() {
        return results;
    }

    public class SearchResult  implements Comparable{

        private boolean adult;
        private String backdropPath;
        private String posterPath;
        private int id;
        private String title;
        private String originalTitle;
        private String releaseDate;
        private Double popularity;
        private Double voteAverage;
        private int voteCount;

        public SearchResult(JSONObject object) {
            try {
                this.adult = object.getBoolean(PARAM_RESULTS_ADULT);
                this.backdropPath = object.getString(PARAM_RESULTS_BACKDROP_PATH);
                this.posterPath = object.getString(PARAM_RESULTS_POSTER_PATH);
                this.id = object.getInt(PARAM_RESULTS_ID);
                this.title = object.getString(PARAM_RESULTS_TITLE);
                this.originalTitle = object.getString(PARAM_RESULTS_ORIGINAL_TITLE);
                this.releaseDate = object.getString(PARAM_RESULTS_RELEASE_DATE);
                this.popularity = object.getDouble(PARAM_RESULTS_POPULARITY);
                this.voteAverage = object.getDouble(PARAM_RESULTS_VOTE_AVERAGE);
                this.voteCount = object.getInt(PARAM_RESULTS_VOTE_COUNT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public boolean isAdult() {
            return adult;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public Double getPopularity() {
            return popularity;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public int getVoteCount() {
            return voteCount;
        }

        @Override
        public int compareTo(Object o) {
            return 0;
        }

        @Override
        public String toString() {
            return "SearchResult{" +
                    "adult=" + adult +
                    ", backdropPath='" + backdropPath + '\'' +
                    ", posterPath='" + posterPath + '\'' +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", originalTitle='" + originalTitle + '\'' +
                    ", releaseDate='" + releaseDate + '\'' +
                    ", popularity=" + popularity +
                    ", voteAverage=" + voteAverage +
                    ", voteCount=" + voteCount +
                    '}';
        }
    }

}
