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
import java.util.List;

public class Series
        extends Video
{

    public static final Parcelable.Creator<Series> CREATOR = new Parcelable.Creator<Series>()
    {
        @Override
        public Series createFromParcel(Parcel source)
        {
            return new Series(source);
        }

        @Override
        public Series[] newArray(int size)
        {
            return new Series[size];
        }
    };

    private static final String LOG = Series.class.getSimpleName();

    private List<Season> seasons = new ArrayList<Season>();
    private String lastDate;
    private boolean inProduction;

	public Series(int id){
		super(id);
	}

    public Series(Parcel in) {
        super(in);
        in.readList(seasons, Season.class.getClassLoader());
        lastDate = in.readString();
        inProduction = Boolean.parseBoolean(in.readString());
    }

    public Series(Cursor cursor, SQLiteDatabase dbR) {
        super(cursor, dbR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeList(seasons);
        dest.writeString(lastDate);
        dest.writeString(String.valueOf(inProduction));
    }

    public void addSeason(Season season) {
        if(season!=null && !seasons.contains(season)) {
            seasons.add(season);
            season.setSeries(this);
        }
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public boolean isInProduction() {
        return inProduction;
    }

    public void setInProduction(boolean inProduction) {
        this.inProduction = inProduction;
    }

    @Override
    public String toString() {
        return "Series{" +
                super.toString() +
                '}';
    }

    @Override
    protected String getTableName() {
        return MovieCatalogContract.SeriesEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.SeriesEntry.ALL_COLUMNS;
    }

    protected static List<Series> getSeriesFromDb(SQLiteDatabase dbR) {
        ArrayList<Series> series = new ArrayList<Series>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = MovieCatalogContract.SeriesEntry.ALL_COLUMNS;
        Cursor cursor = dbR.query(MovieCatalogContract.SeriesEntry.TABLE_NAME,projection,null,null,null,null,null);
        while(cursor.moveToNext()) series.add(new Series(cursor, dbR));
        return series;
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues values = super.getContentValues();

        return values;
    }

    @Override
    protected void initFromCursor(Cursor cursor, SQLiteDatabase dbR) {
        super.initFromCursor(cursor, dbR);

    }


    protected void addSeasonsFromDB(SQLiteDatabase dbR) {
        Log.d(LOG, "addSeasonsFromDB");
        String[] projection = MovieCatalogContract.SeasonEntry.ALL_COLUMNS;
        String WHERE = MovieCatalogContract.SeasonEntry.COLUMN_SERIES_ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};

        Cursor cursor = dbR.query(
                MovieCatalogContract.SeasonEntry.TABLE_NAME,
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
                Season season = new Season(cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry._ID)));
                season.getFromDb(dbR, true);
                addSeason(season);
            }
            while (cursor.moveToNext());
        }
    }


}

