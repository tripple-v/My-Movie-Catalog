package com.vwuilbea.mymoviecatalog.tmdb.responses;

import com.vwuilbea.mymoviecatalog.model.Country;
import com.vwuilbea.mymoviecatalog.model.Genre;
import com.vwuilbea.mymoviecatalog.model.Movie;
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
public class DetailsResponse {

    private static final String LOG = DetailsResponse.class.getSimpleName();

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

    private Boolean adult;
    private String backdropPath;
    private String belongsToCollection;
    private Integer budget;
    private List<Genre> genres = new ArrayList<Genre>();
    private String homePage;
    private Integer id;
    private String imdbId;
    private String originalTitle;
    private String overview;
    private Double popularity;
    private String posterPath;
    private List<ProductionCompany> productionCompanies = new ArrayList<ProductionCompany>();
    private List<Country> countries = new ArrayList<Country>();
    private String releaseDate;
    private Integer revenue;
    private Integer runtime;
    private List<Language> languages = new ArrayList<Language>();
    private String status;
    private String tagline;
    private String title;
    private Double voteAverage;
    private Integer voteCount;

    public DetailsResponse(String result, Video video) {
        try {
            JSONObject object = new JSONObject(result);
            if(!object.isNull(PARAM_ADULT)) this.adult = object.getBoolean(PARAM_ADULT);
            this.backdropPath = object.getString(PARAM_BACKDROP_PATH);
            this.belongsToCollection = object.getString(PARAM_BELONGS_TO_COLLECTION);
            if(!object.isNull(PARAM_BUDGET)) this.budget = object.getInt(PARAM_BUDGET);
            JSONArray array = object.getJSONArray(PARAM_GENRES);
            for (int i = 0; i < array.length(); i++)
                genres.add(new GenreResult(array.getJSONObject(i)).getGenre());
            this.homePage = object.getString(PARAM_HOMEPAGE);
            if(!object.isNull(PARAM_ID)) this.id = object.getInt(PARAM_ID);
            this.imdbId = object.getString(PARAM_IMDB_ID);
            this.originalTitle = object.getString(PARAM_ORIGINAL_TITLE);
            this.overview = object.getString(PARAM_OVERVIEW);
            if(!object.isNull(PARAM_POPULARITY)) this.popularity = object.getDouble(PARAM_POPULARITY);
            this.posterPath = object.getString(PARAM_POSTER_PATH);
            array = object.getJSONArray(PARAM_PRODUCTION_COMPANIES);
            for (int i = 0; i < array.length(); i++)
                productionCompanies.add(new ProductionCompanyResult(array.getJSONObject(i)).getProductionCompany());
            array = object.getJSONArray(PARAM_PRODUCTION_COUNTRIES);
            for (int i = 0; i < array.length(); i++)
                countries.add(new ProductionCountryResult(array.getJSONObject(i)).getCountry());
            this.releaseDate = object.getString(PARAM_RELEASE_DATE);
            if(!object.isNull(PARAM_REVENUE)) this.revenue = object.getInt(PARAM_REVENUE);
            if(!object.isNull(PARAM_RUNTIME)) this.runtime = object.getInt(PARAM_RUNTIME);
            array = object.getJSONArray(PARAM_LANGUAGES);
            for (int i = 0; i < array.length(); i++)
                languages.add(new Language(array.getJSONObject(i)));
            this.status = object.getString(PARAM_STATUS);
            this.tagline = object.getString(PARAM_TAGLINE);
            this.title = object.getString(PARAM_TITLE);
            if(!object.isNull(PARAM_VOTE_AVERAGE)) this.voteAverage = object.getDouble(PARAM_VOTE_AVERAGE);
            if(!object.isNull(PARAM_VOTE_COUNT)) this.voteCount = object.getInt(PARAM_VOTE_COUNT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fillVideo(video);
    }

    public void fillVideo(Video movie) {
        movie.setAdult(adult);
        movie.setBudget(budget);
        movie.setOriginalTitle(originalTitle);
        movie.setOverview(overview);
        movie.setPosterPath(posterPath);
        movie.setGenres(genres);
        movie.setProductionCompanies(productionCompanies);
        movie.setCountries(countries);
        movie.setReleaseDate(releaseDate);
        movie.setRuntime(runtime);
        if(languages.size()>0) movie.setLanguage(languages.get(0).getIso());
        movie.setTagline(tagline);
        movie.setTitle(title);
        movie.setVoteAverage(voteAverage);
        movie.setVoteCount(voteCount);
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getBelongsToCollection() {
        return belongsToCollection;
    }

    public Integer getBudget() {
        return budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomePage() {
        return homePage;
    }

    public Integer getId() {
        return id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }



    public class GenreResult {

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

    public class ProductionCompanyResult {

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

    public class ProductionCountryResult {

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

    public class Language {

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
