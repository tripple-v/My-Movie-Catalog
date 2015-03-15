package com.vwuilbea.mymoviecatalog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;
import com.vwuilbea.mymoviecatalog.database.MyEntry;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public abstract class Video
        extends MyEntry
        implements Parcelable {

    private static final String LOG = Video.class.getSimpleName();

    public enum Quality {
        LOW(0, "Screener"),
        NORMAL(1, "DVDRip"),
        HD720(2, "720p"),
        HD1080(3, "1080p"),
        ULTRAHD(4, "4K");

        private int id;
        private String name;

        Quality(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public enum Format {
        NUMERIC(0, "Numeric"),
        DVD(1, "DVD"),
        BLURAY(2, "Blu-Ray");

        private int id;
        private String name;

        Format(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    protected int id;
    protected String title;
    protected String originalTitle;
    protected String tagline;
    protected String overview;
    protected String comment;
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
    protected int quality = Quality.NORMAL.getId();
    protected int format = Format.NUMERIC.getId();
    protected boolean threeD = false;
    protected float voteAverage;
    protected int voteCount;
    protected float votePrivate;

    public Video(int id) {
        super();
        this.id = id;
    }

    public Video(Cursor cursor, SQLiteDatabase dbR) {
        super();
        if(cursor.getCount()>0) {
            this.id = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry._ID));
            initFromCursor(cursor, dbR);
        }
    }

    /*
     To update when add/remove fields
     */

    public Video(Parcel in) {
        id = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        tagline = in.readString();
        overview = in.readString();
        comment = in.readString();
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
        quality = in.readInt();
        format = in.readInt();
        threeD = Boolean.parseBoolean(in.readString());
        voteAverage = in.readFloat();
        voteCount = in.readInt();
        votePrivate = in.readFloat();

        for (Genre genre : genres) genre.addVideo(this);
        for (Role role : roles) role.setVideo(this);
        for (Realisator realisator : realisators) realisator.addVideo(this);
        for (Country country : countries) country.addVideo(this);
        for (ProductionCompany prod : productionCompanies) prod.addVideo(this);
        if (location != null) location.addVideo(this);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(tagline);
        dest.writeString(overview);
        dest.writeString(comment);
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
        dest.writeInt(quality);
        dest.writeInt(format);
        dest.writeString(String.valueOf(threeD));
        dest.writeFloat(voteAverage);
        dest.writeInt(voteCount);
        dest.writeFloat(votePrivate);
    }

    protected ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieCatalogContract.VideoEntry._ID, id);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_TITLE, title);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_TAG_LINE, tagline);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_OVERVIEW, overview);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_COMMENT, comment);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_RUNTIME, runtime);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_DATE, releaseDate);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_LANGUAGE, language);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_SUBTITLE, subtitle);
        if (location != null) {
            values.put(MovieCatalogContract.VideoEntry.COLUMN_LOCATION_ID, location.getId());
        }
        values.put(MovieCatalogContract.VideoEntry.COLUMN_ADULT, adult);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_POSTER_PATH, posterPath);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_COVER_PATH, coverPath);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_BUDGET, budget);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_QUALITY, quality);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_FORMAT, format);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_THREE_D, threeD);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_VOTE_AVERAGE, voteAverage);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_VOTE_COUNT, voteCount);
        values.put(MovieCatalogContract.VideoEntry.COLUMN_VOTE_PRIVATE, votePrivate);
        return values;
    }

    protected void initFromCursor(Cursor cursor, SQLiteDatabase dbR) {
        Log.d(LOG, "initFromCursor, video : "+getId());
        title = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_TITLE));
        originalTitle = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_ORIGINAL_TITLE));
        tagline = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_TAG_LINE));
        overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_OVERVIEW));
        comment = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_COMMENT));
        runtime = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_RUNTIME));
        releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_DATE));
        language = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_LANGUAGE));
        subtitle = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_SUBTITLE));
        adult = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_ADULT))>0;
        posterPath = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_POSTER_PATH));
        coverPath = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_COVER_PATH));
        budget = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_BUDGET));
        quality = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_QUALITY));
        format = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_FORMAT));
        threeD = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_THREE_D))>0;
        voteAverage = cursor.getFloat(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_VOTE_AVERAGE));
        voteCount = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_VOTE_COUNT));
        votePrivate = cursor.getFloat(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntry.COLUMN_VOTE_PRIVATE));
        addGenreFromDB(dbR);
        addRoleFromDB(dbR);
    }

    /**********/

    @Override
    public int describeContents() {
        return 0;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public boolean isThreeD() {
        return threeD;
    }

    public void setThreeD(boolean threeD) {
        this.threeD = threeD;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public float getVotePrivate() {
        return votePrivate;
    }

    public void setVotePrivate(float votePrivate) {
        this.votePrivate = votePrivate;
    }

    @Override
    public String toString() {
        return  getTableName()+"{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", runtime=" + runtime +
                ", releaseDate='" + releaseDate + '\'' +
                ", language='" + language + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", adult=" + adult +
                ", budget=" + budget +
                ", quality=" + quality +
                ", format=" + format +
                ", threeD=" + threeD +
                ", votePrivate=" + votePrivate +
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
            if(video1==null || video1.getReleaseDate()==null) return 1;
            if(video2==null || video2.getReleaseDate()==null) return -1;
            return video2.getReleaseDate().compareTo(video1.getReleaseDate());
        }

        public void aa() {
        }
    };

    public static final Comparator<Video> COMPARATOR_NAME = new Comparator<Video>() {
        @Override
        public int compare(Video video1, Video video2) {
            if(video1==null || video1.getTitle()==null) return 1;
            if(video2==null || video2.getTitle()==null) return -1;
           return video1.getTitle().compareTo(video2.getTitle());
        }

        public void aa() {
        }
    };

    public static final Comparator<Video> COMPARATOR_RATING= new Comparator<Video>() {
        @Override
        public int compare(Video video1, Video video2) {
            if(video1==null || (Float)video1.getVoteAverage()==null) return 1;
            if(video2==null || (Float)video2.getVoteAverage()==null) return -1;
            return ((Float)video2.getVoteAverage()).compareTo(video1.getVoteAverage());
        }

        public void aa() {
        }
    };

    protected <T extends Entity> List<T> addEntityFromDB(SQLiteDatabase dbR, String tableName, Class<T> clazz) {
        String[] projection = {MovieCatalogContract.VideoEntityEntry.COLUMN_ENTITY_ID};
        String WHERE = MovieCatalogContract.VideoEntityEntry.COLUMN_VIDEO_ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};
        Cursor cursor = dbR.query(tableName,projection,WHERE,selectionArgs,null,null,null);
        List<T> entities = new ArrayList<T>();
        while(cursor.moveToNext()) {
            try {
                T entity = clazz.newInstance();
                entity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.VideoEntityEntry.COLUMN_ENTITY_ID)));
                if (entity.getFromDb(dbR, true) == DatabaseHelper.OK)
                    entities.add(entity);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return entities;
    }

    protected void addGenreFromDB(SQLiteDatabase dbR) {
        List<Genre> entities = addEntityFromDB(dbR, MovieCatalogContract.VideoGenreEntry.TABLE_NAME, Genre.class);
        for(Genre entity:entities) addGenre(entity);
    }

    protected void addRoleFromDB(SQLiteDatabase dbR) {
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
            do {
                Role role = new Role(cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.RoleEntry._ID)));
                role.getFromDb(dbR, true);
                addRole(role);
            } while (cursor.moveToNext());
        }
    }

    @Override
    protected void removeDependencies(SQLiteDatabase dbW, String[] selectionArgs) {
        String selection;
        //VideoGenres
        selection = MovieCatalogContract.VideoEntityEntry.COLUMN_VIDEO_ID+ " LIKE ?";
        dbW.delete(MovieCatalogContract.VideoGenreEntry.TABLE_NAME, selection, selectionArgs);
        //Roles
        selection = MovieCatalogContract.RoleEntry.COLUMN_VIDEO_ID+ " LIKE ?";
        dbW.delete(MovieCatalogContract.RoleEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    protected void addDependencies(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        for(Genre genre:genres) genre.putInDB(dbW, dbR);
        for(Role role:roles) role.putInDB(dbW, dbR);
    }

}

