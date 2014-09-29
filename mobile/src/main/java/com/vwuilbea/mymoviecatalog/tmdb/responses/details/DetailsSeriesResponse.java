package com.vwuilbea.mymoviecatalog.tmdb.responses.details;

import com.vwuilbea.mymoviecatalog.model.Country;
import com.vwuilbea.mymoviecatalog.model.Genre;
import com.vwuilbea.mymoviecatalog.model.ProductionCompany;
import com.vwuilbea.mymoviecatalog.model.Season;
import com.vwuilbea.mymoviecatalog.model.Series;
import com.vwuilbea.mymoviecatalog.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by Valentin on 04/09/2014.
 */
public class DetailsSeriesResponse {

    private static final String LOG = DetailsSeriesResponse.class.getSimpleName();

    private static final String PARAM_BACKDROP_PATH = "backdrop_path";
    private static final String PARAM_CREATED_BY = "created_by";
    private static final String PARAM_RUNTIME = "episode_run_time";
    private static final String PARAM_FIRST_DATE = "first_air_date";
    private static final String PARAM_GENRES = "genres";
    private static final String PARAM_HOMEPAGE = "homepage";
    private static final String PARAM_ID = "id";
    private static final String PARAM_IN_PRODUCTION = "in_production";
    private static final String PARAM_LANGUAGES = "languages";
    private static final String PARAM_LAST_DATE = "last_air_date";
    private static final String PARAM_TITLE = "name";
    private static final String PARAM_NETWORKS = "networks";
    private static final String PARAM_NB_EPISODES = "number_of_episodes";
    private static final String PARAM_NB_SEASONS = "number_of_seasons";
    private static final String PARAM_ORIGINAL_TITLE = "original_name";
    private static final String PARAM_COUNTRY = "origin_country";
    private static final String PARAM_OVERVIEW = "overview";
    private static final String PARAM_POSTER_PATH = "poster_path";
    private static final String PARAM_PRODUCTION_COMPANIES = "production_companies";
    private static final String PARAM_SEASONS = "seasons";
    private static final String PARAM_STATUS= "status";
    private static final String PARAM_VOTE_AVERAGE = "vote_average";
    private static final String PARAM_VOTE_COUNT = "vote_count";

    private static String homePage;
    private static String status;
    private static List<String> languages = new ArrayList<String>();

    public static void parse(String result, Series series) {
        try {
            JSONObject object = new JSONObject(result);
            JSONArray array;
            series.setCoverPath(object.getString(PARAM_BACKDROP_PATH));

            if(!object.isNull(PARAM_GENRES)) {
                array = object.getJSONArray(PARAM_GENRES);
                for (int i = 0; i < array.length(); i++)
                    series.addGenre(new GenreResult(array.getJSONObject(i)).getGenre());
            }

            if(!object.isNull(PARAM_HOMEPAGE)) homePage = object.getString(PARAM_HOMEPAGE);
            series.setOriginalTitle(object.getString(PARAM_ORIGINAL_TITLE));
            if(!object.isNull(PARAM_OVERVIEW)) series.setOverview(object.getString(PARAM_OVERVIEW).replace("\n\n", ""));

            if(!object.isNull(PARAM_PRODUCTION_COMPANIES)) {
                array = object.getJSONArray(PARAM_PRODUCTION_COMPANIES);
                for (int i = 0; i < array.length(); i++)
                    series.addProductionCompany(new ProductionCompanyResult(array.getJSONObject(i)).getProductionCompany());
            }

            if(!object.isNull(PARAM_RUNTIME)) {
                array = object.getJSONArray(PARAM_RUNTIME);
                List<Integer> runtimes = new ArrayList<Integer>();
                for(int i=0; i<array.length(); i++) {
                    runtimes.add(array.getInt(i));
                }
                int runtimeAverage=0;
                for(int runtime:runtimes) {
                    runtimeAverage+=runtime;
                }
                runtimeAverage/=runtimes.size();
                series.setRuntime(runtimeAverage);
            }

            array = object.getJSONArray(PARAM_LANGUAGES);
            for (int i = 0; i < array.length(); i++)
                languages.add(array.getString(i));
            if(languages.size()>0) series.setLanguage(languages.get(0));

            if(!object.isNull(PARAM_LAST_DATE)) series.setLastDate(object.getString(PARAM_LAST_DATE));
            status = object.getString(PARAM_STATUS);
            if(!object.isNull(PARAM_TITLE)) series.setTitle(object.getString(PARAM_TITLE));
            if(!object.isNull(PARAM_VOTE_AVERAGE)) series.setVoteAverage((float) object.getDouble(PARAM_VOTE_AVERAGE));
            if(!object.isNull(PARAM_VOTE_COUNT)) series.setVoteCount(object.getInt(PARAM_VOTE_COUNT));
            if(!object.isNull(PARAM_SEASONS)) {
                array = object.getJSONArray(PARAM_SEASONS);
                for(int i = 0; i < array.length(); i++) {
                    series.addSeason(SeasonResult.parse(array.getJSONObject(i), series));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class SeasonResult {

        private static final String PARAM_SEASON_AIR_DATE = "air_date";
        private static final String PARAM_SEASON_ID = "id";
        private static final String PARAM_SEASON_POSTER_PATH = "poster_path";
        private static final String PARAM_SEASON_NUMBER = "season_number";

        static Season parse(JSONObject object, Series series) {
            try {
                if(!object.isNull(PARAM_SEASON_ID)) {
                    int number=-1;
                    if(!object.isNull(PARAM_SEASON_NUMBER)) number = object.getInt(PARAM_SEASON_NUMBER);
                    if(number>0) {
                        Season season = new Season(object.getInt(PARAM_SEASON_ID), series);
                        season.setNumber(number);
                        if (!object.isNull(PARAM_SEASON_AIR_DATE))
                            season.setFirstDate(object.getString(PARAM_SEASON_AIR_DATE));
                        if (!object.isNull(PARAM_SEASON_POSTER_PATH))
                            season.setPosterPath(object.getString(PARAM_SEASON_POSTER_PATH));
                        return season;
                    }
                    else return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class GenreResult {

        private static final String PARAM_GENRE_ID = "id";
        private static final String PARAM_GENRE_NAME = "name";

        private int id;
        private String name;

        GenreResult(JSONObject object) {
            try {
                this.id = object.getInt(PARAM_GENRE_ID);
                this.name = object.getString(PARAM_GENRE_NAME);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public Genre getGenre() {
            return new Genre(id,name);
        }

        public Genre getGenre(Video video) {
            return new Genre(id,name,video);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class ProductionCompanyResult {

        private static final String PARAM_PROD_ID = "id";
        private static final String PARAM_PROD_NAME = "name";

        private int id;
        private String name;

        ProductionCompanyResult(JSONObject object) {
            try {
                this.id = object.getInt(PARAM_PROD_ID);
                this.name = object.getString(PARAM_PROD_NAME);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public ProductionCompany getProductionCompany() {
            return new ProductionCompany(id, name);
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}
