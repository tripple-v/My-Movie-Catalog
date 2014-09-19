package com.vwuilbea.mymoviecatalog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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

    private static final String LOG = Video.class.getSimpleName();

    protected int id;
    protected String title;
    protected String originalTitle;
    protected String tagline;
    protected String overview;
    protected int runtime;
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
    protected String coverPath;
    protected int budget;
    protected double voteAverage;
    protected int voteCount;

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
        coverPath = in.readString();
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
        dest.writeString(coverPath);
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

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
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

    public void addCountry(Country country) {
        if(country != null) {
            country.addVideo(this);
            if(!countries.contains(country))
                countries.add(country);
        }
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        if(countries!=null) {
            this.countries = countries;
            for(Country country:countries) country.addVideo(this);
        }
    }

    public void addProductionCompany(ProductionCompany company) {
        if(company!=null) {
            company.addVideo(this);
            if(!productionCompanies.contains(company))
                productionCompanies.add(company);
        }
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        if(productionCompanies!=null) {
            this.productionCompanies = productionCompanies;
            for(ProductionCompany company:productionCompanies) company.addVideo(this);
        }
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

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
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
        Log.d(LOG, "addGenreFromDB");
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
        Log.d(LOG, "cursor count:"+cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Genre genre = new Genre(cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoGenreEntry.COLUMN_GENRE_ID)));
                genre.getFromDb(dbR, true);
                addGenre(genre);
            }
            while (cursor.moveToNext());
        }
    }

    protected void addRoleFromDB(SQLiteDatabase dbR) {
        Log.d(LOG, "addRoleFromDB");
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
        Log.d(LOG, "cursor count:"+cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Role role = new Role(cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.RoleEntry._ID)));
                role.getFromDb(dbR, true);
                addRole(role);
            } while (cursor.moveToNext());
        }
    }
}

