package com.vwuilbea.mymoviecatalog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.util.Log;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;
import com.vwuilbea.mymoviecatalog.database.MyEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vwuilbea on 24/09/2014.
 */
public class Season
        extends MyEntry
        implements Parcelable {

    public static final Parcelable.Creator<Season> CREATOR = new Parcelable.Creator<Season>()
    {
        @Override
        public Season createFromParcel(Parcel source)
        {
            return new Season(source);
        }

        @Override
        public Season[] newArray(int size)
        {
            return new Season[size];
        }
    };

    private static final String LOG = Season.class.getSimpleName();

    private int id;
    private Series series;
    private int number;
    private boolean possessed;
    private int quality;
    private List<Episode> episodes = new ArrayList<Episode>();
    private String overview;
    private String posterPath;
    private String firstDate;

    public Season(int id) {
        this(id, null);
    }

    public Season(int id, Series series) {
        this(id, series, 0);
    }

    public Season(int id, Series series, int number) {
        this(id, series, number, false, series != null ? series.getQuality() : Video.Quality.NORMAL.getId());
    }

    public Season(int id, Series series, int number, boolean possessed, int quality) {
        this.id = id;
        this.series = series;
        this.number = number;
        this.possessed = possessed;
        this.quality = quality;
    }

    public Season(Parcel in) {
        id = in.readInt();
        number = in.readInt();
        possessed = Boolean.parseBoolean(in.readString());
        quality = in.readInt();
        in.readList(episodes, Episode.class.getClassLoader());
        overview = in.readString();
        posterPath = in.readString();
        firstDate = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(number);
        dest.writeString(String.valueOf(possessed));
        dest.writeInt(quality);
        dest.writeList(episodes);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(firstDate);
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieCatalogContract.SeasonEntry._ID,id);
        values.put(MovieCatalogContract.SeasonEntry.COLUMN_SERIES_ID,series.getId());
        values.put(MovieCatalogContract.SeasonEntry.COLUMN_NUMBER,number);
        values.put(MovieCatalogContract.SeasonEntry.COLUMN_POSSESSED,possessed);
        values.put(MovieCatalogContract.SeasonEntry.COLUMN_QUALITY,quality);
        values.put(MovieCatalogContract.SeasonEntry.COLUMN_OVERVIEW,overview);
        values.put(MovieCatalogContract.SeasonEntry.COLUMN_POSTER_PATH,posterPath);
        values.put(MovieCatalogContract.SeasonEntry.COLUMN_FIRST_DATE,firstDate);
        return values;
    }

    public void initFromCursor(Cursor cursor, SQLiteDatabase dbR) {
        cursor.moveToFirst();
        id = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry._ID));
        number = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry.COLUMN_NUMBER));
        possessed = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry.COLUMN_POSSESSED)) > 0;
        quality = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry.COLUMN_QUALITY));
        overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry.COLUMN_OVERVIEW));
        posterPath = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry.COLUMN_POSTER_PATH));
        firstDate = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry.COLUMN_FIRST_DATE));
        addEpisodesFromDB(dbR);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
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

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public void addEpisode(Episode episode) {
        if(episode!=null && !episodes.contains(episode)) {
            episodes.add(episode);
            episode.setSeason(this);
        }
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
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

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

        Season season = (Season) o;
        return id == season.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Season{" +
                "id=" + id +
                ", number=" + number +
                ", possessed=" + possessed +
                ", series=" + series +
                '}';
    }

    @Override
    protected String getTableName() {
        return MovieCatalogContract.SeasonEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.SeasonEntry.ALL_COLUMNS;
    }

    @Override
    protected void addDependencies(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        for(Episode episode:episodes) episode.putInDB(dbW, dbR);
    }

    @Override
    protected void removeDependencies(SQLiteDatabase dbW, String[] selectionArgs) {
        String selection;
        //Episodes
        selection = MovieCatalogContract.EpisodeEntry.COLUMN_SEASON_ID+ " LIKE ?";
        dbW.delete(MovieCatalogContract.EpisodeEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected void addEpisodesFromDB(SQLiteDatabase dbR) {
        Log.d(LOG, "addEpisodesFromDB");
        String[] projection = MovieCatalogContract.EpisodeEntry.ALL_COLUMNS;
        String WHERE = MovieCatalogContract.EpisodeEntry.COLUMN_SEASON_ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};

        Cursor cursor = dbR.query(
                MovieCatalogContract.EpisodeEntry.TABLE_NAME,
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
                Episode episode = new Episode(cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry._ID)));
                episode.getFromDb(dbR, true);
                addEpisode(episode);
            }
            while (cursor.moveToNext());
        }
    }

}
