package com.vwuilbea.mymoviecatalog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


public abstract class Video
        implements Parcelable {

    protected int id;
    protected String title;
    protected String originalTitle;
    protected String tagline;
    protected String overview;
    protected Integer runtime;
    protected String releaseDate;
    protected List<Genre> genres = new ArrayList<Genre>();
    protected List<Role> roles = new ArrayList<Role>();
    protected List<Realisator> realisators = new ArrayList<Realisator>();
    protected List<Country> countries = new ArrayList<Country>();
    protected List<ProductionCompany> productionCompanies = new ArrayList<ProductionCompany>();
    protected String language;
    protected String subtitle;
    protected Location location;
    protected boolean adult;
    protected String posterPath;
    protected Integer budget;
    protected double voteAverage;
    protected Integer voteCount;

    public Video(int id) {
        super();
        this.id = id;
    }

    public Video(Parcel in) {
        id = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        tagline = in.readString();
        overview = in.readString();
        runtime = in.readInt();
        releaseDate = in.readString();
        in.readList(genres, Genre.class.getClassLoader());
        in.readList(roles, Role.class.getClassLoader());
        in.readList(realisators, Realisator.class.getClassLoader());
        in.readList(countries, Country.class.getClassLoader());
        in.readList(productionCompanies, ProductionCompany.class.getClassLoader());
        language = in.readString();
        subtitle = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        adult = Boolean.parseBoolean(in.readString());
        posterPath = in.readString();
        budget = in.readInt();
        voteAverage = in.readDouble();
        voteCount = in.readInt();

        for (Genre genre : genres) genre.addVideo(this);
        for (Role role : roles) role.setVideo(this);
        for (Realisator realisator : realisators) realisator.addVideo(this);
        for (Country country : countries) country.addVideo(this);
        for (ProductionCompany prod : productionCompanies) prod.addVideo(this);
        if (location != null) location.addVideo(this);
    }

    public Video(int id, String title, String originalTitle, String tagline, String overview, int runtime, String releaseDate, List<Genre> genres,
                 List<Role> roles, List<Realisator> realisators, List<Country> countries, List<ProductionCompany> productionCompanies,
                 String language, String subtitle, Location location, boolean adult, String posterPath, int budget, double voteAverage, int voteCount) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.tagline = tagline;
        this.overview = overview;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.roles = roles;
        this.realisators = realisators;
        this.countries = countries;
        this.productionCompanies = productionCompanies;
        this.language = language;
        this.subtitle = subtitle;
        this.location = location;
        this.adult = adult;
        this.posterPath = posterPath;
        this.budget = budget;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(tagline);
        dest.writeString(overview);
        dest.writeInt(runtime);
        dest.writeString(releaseDate);
        dest.writeList(genres);
        dest.writeList(roles);
        dest.writeList(realisators);
        dest.writeList(countries);
        dest.writeList(productionCompanies);
        dest.writeString(language);
        dest.writeString(subtitle);
        dest.writeParcelable(location, flags);
        dest.writeString(String.valueOf(adult));
        dest.writeString(posterPath);
        dest.writeInt(budget);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void addGenre(Genre genre) {
        if (genre != null) {
            genre.addVideo(this);
            if (!genres.contains(genre))
                genres.add(genre);
        }
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        if (genres != null) {
            this.genres = genres;
            for (Genre genre : genres) genre.addVideo(this);
        }
    }

    public void addRole(Role role) {
        if (role != null && !roles.contains(role))
            roles.add(role);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        if (roles != null) {
            this.roles = roles;
        }
    }

    public List<Realisator> getRealisators() {
        return realisators;
    }

    public void setRealisators(List<Realisator> realisators) {
        this.realisators = realisators;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", tagline='" + tagline + '\'' +
                ", overview='" + overview + '\'' +
                ", genres=" + genres +
                ", runtime=" + runtime +
                ", releaseDate=" + releaseDate +
                ", roles=" + roles +
                ", realisators=" + realisators +
                ", countries=" + countries +
                ", productionCompanies=" + productionCompanies +
                ", language='" + language + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", location=" + location +
                ", adult=" + adult +
                ", posterPath='" + posterPath + '\'' +
                ", budget=" + budget +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

        Video video = (Video) o;
        return id == video.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public static final Comparator<Video> COMPARATOR_DATE = new Comparator<Video>() {
        @Override
        public int compare(Video video1, Video video2) {
            return video2.getReleaseDate().compareTo(video1.getReleaseDate());
        }

        public void aa() {
        }
    };

    protected void addGenreFromDB(SQLiteDatabase dbR) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {MovieCatalogContract.VideoGenreEntry.COLUMN_GENRE_ID};
        String WHERE = MovieCatalogContract.VideoGenreEntry.COLUMN_VIDEO_ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};

        Cursor cursor = dbR.query(
                MovieCatalogContract.VideoGenreEntry.TABLE_NAME,
                projection,
                WHERE,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                Genre genre = new Genre(cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoGenreEntry.COLUMN_GENRE_ID)));
                genre.getFromDb(dbR, true);
                addGenre(genre);
            }
        }
    }

    protected void addRoleFromDB(SQLiteDatabase dbR) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {MovieCatalogContract.RoleEntry._ID};
        String WHERE = MovieCatalogContract.RoleEntry.COLUMN_VIDEO_ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};

        Cursor cursor = dbR.query(
                MovieCatalogContract.RoleEntry.TABLE_NAME,
                projection,
                WHERE,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                Role role = new Role(cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoGenreEntry.COLUMN_GENRE_ID)));
                role.getFromDb(dbR, true);
                addRole(role);
            }
        }
    }
}

