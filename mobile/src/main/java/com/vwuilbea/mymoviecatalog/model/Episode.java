package com.vwuilbea.mymoviecatalog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;
import com.vwuilbea.mymoviecatalog.database.MyEntry;

/**
 * Created by vwuilbea on 24/09/2014.
 */
public class Episode
        extends MyEntry
        implements Parcelable {

    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>()
    {
        @Override
        public Episode createFromParcel(Parcel source)
        {
            return new Episode(source);
        }

        @Override
        public Episode[] newArray(int size)
        {
            return new Episode[size];
        }
    };

    private static final String LOG = Episode.class.getSimpleName();

    private int id;
    private Season season;
    private int number;
    private boolean possessed;
    private String date;
    private String title;
    private int quality;
    private String overview;
    private String posterPath;
    private float voteAverage;
    private int voteCount;

    public Episode(int id) {
        this(id, null);
    }

    public Episode(int id, Season season) {
        this(id, season, season != null && season.isPossessed());
    }

    public Episode(int id, Season season, boolean possessed) {
        this.id = id;
        this.season = season;
        this.possessed = possessed;
    }

    public Episode(Parcel in) {
        id = in.readInt();
        number = in.readInt();
        possessed = Boolean.parseBoolean(in.readString());
        date = in.readString();
        title = in.readString();
        quality = in.readInt();
        overview = in.readString();
        posterPath = in.readString();
        voteAverage = in.readFloat();
        voteCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(number);
        dest.writeString(String.valueOf(possessed));
        dest.writeString(date);
        dest.writeString(title);
        dest.writeInt(quality);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeFloat(voteAverage);
        dest.writeInt(voteCount);
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieCatalogContract.EpisodeEntry._ID,id);
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_SEASON_ID,season.getId());
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_NUMBER,number);
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_POSSESSED,possessed);
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_DATE,date);
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_TITLE,title);
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_QUALITY,quality);
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_OVERVIEW,overview);
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_POSTER_PATH,posterPath);
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_VOTE_AVERAGE,voteAverage);
        values.put(MovieCatalogContract.EpisodeEntry.COLUMN_VOTE_COUNT,voteCount);
        return values;
    }

    public void initFromCursor(Cursor cursor, SQLiteDatabase dbR) {
        cursor.moveToFirst();
        id = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry._ID));
        number = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry.COLUMN_NUMBER));
        possessed = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry.COLUMN_POSSESSED)) > 0;
        date = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry.COLUMN_DATE));
        title = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry.COLUMN_TITLE));
        quality = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry.COLUMN_QUALITY));
        overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry.COLUMN_OVERVIEW));
        posterPath = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry.COLUMN_POSTER_PATH));
        voteAverage = cursor.getFloat(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry.COLUMN_VOTE_AVERAGE));
        voteCount = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.EpisodeEntry.COLUMN_VOTE_COUNT));
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isPossessed() {
        return possessed;
    }

    public void setPossessed(boolean possessed) {
        this.possessed = possessed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Episode)) return false;

        Episode episode = (Episode) o;

        return id == episode.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", season=" + season +
                ", number=" + number +
                ", possessed=" + possessed +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    protected String getTableName() {
        return MovieCatalogContract.EpisodeEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.EpisodeEntry.ALL_COLUMNS;
    }

    @Override
    protected void addDependencies(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        //No dependencies here
    }

    @Override
    protected void removeDependencies(SQLiteDatabase dbW, String[] selectionArgs) {
        //No dependencies here
    }

}
