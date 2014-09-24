package com.vwuilbea.mymoviecatalog.tmdb.responses.details;

import com.vwuilbea.mymoviecatalog.model.Country;
import com.vwuilbea.mymoviecatalog.model.Genre;
import com.vwuilbea.mymoviecatalog.model.ProductionCompany;
import com.vwuilbea.mymoviecatalog.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin on 04/09/2014.
 */
public class DetailsMovieResponse {

    private static final String LOG = DetailsMovieResponse.class.getSimpleName();

    private static final String PARAM_ADULT = "adult";
    private static final String PARAM_BACKDROP_PATH = "backdrop_path";
    private static final String PARAM_BELONGS_TO_COLLECTION = "belongs_to_collection";
    private static final String PARAM_BUDGET = "budget";
    private static final String PARAM_GENRES = "genres";
    private static final String PARAM_HOMEPAGE = "homepage";
    private static final String PARAM_ID = "id";
    private static final String PARAM_IMDB_ID = "imdb_id";
    private static final String PARAM_ORIGINAL_TITLE = "original_title";
    private static final String PARAM_OVERVIEW = "overview";
    private static final String PARAM_POPULARITY = "popularity";
    private static final String PARAM_POSTER_PATH = "poster_path";
    private static final String PARAM_PRODUCTION_COMPANIES = "production_companies";
    private static final String PARAM_PRODUCTION_COUNTRIES = "production_countries";
    private static final String PARAM_RELEASE_DATE = "release_date";
    private static final String PARAM_REVENUE = "revenue";
    private static final String PARAM_RUNTIME = "runtime";
    private static final String PARAM_LANGUAGES = "spoken_languages";
    private static final String PARAM_STATUS= "status";
    private static final String PARAM_TAGLINE = "tagline";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_VOTE_AVERAGE = "vote_average";
    private static final String PARAM_VOTE_COUNT = "vote_count";

    private static String belongsToCollection;
    private static String homePage;
    private static String imdbId;
    private static Double popularity;
    private static Integer revenue;
    private static List<Language> languages = new ArrayList<Language>();
    private static String status;

    public static void parse(String result, Video video) {
        try {
            JSONObject object = new JSONObject(result);
            if(!object.isNull(PARAM_ADULT)) video.setAdult(object.getBoolean(PARAM_ADULT));
            video.setCoverPath(object.getString(PARAM_BACKDROP_PATH));
            if(!object.isNull(PARAM_BELONGS_TO_COLLECTION)) belongsToCollection = object.getString(PARAM_BELONGS_TO_COLLECTION);
            if(!object.isNull(PARAM_BUDGET)) video.setBudget(object.getInt(PARAM_BUDGET));
            JSONArray array = object.getJSONArray(PARAM_GENRES);
            for (int i = 0; i < array.length(); i++)
                video.addGenre(new GenreResult(array.getJSONObject(i)).getGenre());
            if(!object.isNull(PARAM_HOMEPAGE)) homePage = object.getString(PARAM_HOMEPAGE);
            if(!object.isNull(PARAM_IMDB_ID)) imdbId = object.getString(PARAM_IMDB_ID);
            video.setOriginalTitle(object.getString(PARAM_ORIGINAL_TITLE));
            video.setOverview(object.getString(PARAM_OVERVIEW));
            if(!object.isNull(PARAM_POPULARITY)) popularity = object.getDouble(PARAM_POPULARITY);
            video.setPosterPath(object.getString(PARAM_POSTER_PATH));
            array = object.getJSONArray(PARAM_PRODUCTION_COMPANIES);
            for (int i = 0; i < array.length(); i++)
                video.addProductionCompany(new ProductionCompanyResult(array.getJSONObject(i)).getProductionCompany());
            array = object.getJSONArray(PARAM_PRODUCTION_COUNTRIES);
            for (int i = 0; i < array.length(); i++)
                video.addCountry(new ProductionCountryResult(array.getJSONObject(i)).getCountry());
            video.setReleaseDate(object.getString(PARAM_RELEASE_DATE));
            if(!object.isNull(PARAM_REVENUE)) revenue = object.getInt(PARAM_REVENUE);
            if(!object.isNull(PARAM_RUNTIME)) video.setRuntime(object.getInt(PARAM_RUNTIME));
            array = object.getJSONArray(PARAM_LANGUAGES);
            for (int i = 0; i < array.length(); i++)
                languages.add(new Language(array.getJSONObject(i)));
            if(languages.size()>0) video.setLanguage(languages.get(0).getIso());
            status = object.getString(PARAM_STATUS);
            video.setTagline(object.getString(PARAM_TAGLINE));
            video.setTitle(object.getString(PARAM_TITLE));
            if(!object.isNull(PARAM_VOTE_AVERAGE)) video.setVoteAverage((float)object.getDouble(PARAM_VOTE_AVERAGE));
            if(!object.isNull(PARAM_VOTE_COUNT)) video.setVoteCount(object.getInt(PARAM_VOTE_COUNT));
        } catch (JSONException e) {
            e.printStackTrace();
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

    public static class ProductionCountryResult {

        private static final String PARAM_COUNTRY_ISO = "iso_3166_1";
        private static final String PARAM_COUNTRY_NAME = "name";

        private String iso;
        private String name;

        ProductionCountryResult(JSONObject object) {
            try {
                this.iso = object.getString(PARAM_COUNTRY_ISO);
                this.name = object.getString(PARAM_COUNTRY_NAME);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public Country getCountry() {
            return new Country(iso, name);
        }

        public String getIso() {
            return iso;
        }

        public String getName() {
            return name;
        }
    }

    public static class Language {

        private static final String PARAM_LANGUAGE_ISO = "iso_639_1";
        private static final String PARAM_LANGUAGE_NAME = "name";

        private String iso;
        private String name;

        Language(JSONObject object) {
            try {
                this.iso = object.getString(PARAM_LANGUAGE_ISO);
                this.name = object.getString(PARAM_LANGUAGE_NAME);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getIso() {
            return iso;
        }

        public String getName() {
            return name;
        }
    }

}
