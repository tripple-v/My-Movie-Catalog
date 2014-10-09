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
        for(Season season:seasons) season.setSeries(this);
    }

    public Series(Cursor cursor, SQLiteDatabase dbR) {
        super(cursor, dbR);
        addSeasonsFromDB(dbR);
        Log.d(LOG,"New Series: "+ toString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(seasons);
        dest.writeString(lastDate);
        dest.writeString(String.valueOf(inProduction));
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues values = super.getContentValues();
        values.put(MovieCatalogContract.SeriesEntry.COLUMN_LAST_DATE,lastDate);
        values.put(MovieCatalogContract.SeriesEntry.COLUMN_IN_PRODUCTION, inProduction);
        return values;
    }

    @Override
    protected void initFromCursor(Cursor cursor, SQLiteDatabase dbR) {
        super.initFromCursor(cursor, dbR);
        lastDate = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeriesEntry.COLUMN_LAST_DATE));
        inProduction = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeriesEntry.COLUMN_IN_PRODUCTION)) > 0;
    }


    public void addSeason(Season season) {
        if(season!=null && !getSeasons().contains(season)) {
            seasons.add(season);
            season.setSeries(this);
        }
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        if(seasons!=null) this.seasons = seasons;
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
                super.toString() + ", " +
                seasons.size() + " seasons" +
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
        if (cursor.getCount() > 0) {
            Log.d(LOG, cursor.getCount()+" seasons found in series "+getTitle());
            cursor.moveToFirst();
            do {
                Season season = new Season(cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.SeasonEntry._ID)));
                if( season.getFromDb(dbR, true) == DatabaseHelper.OK) addSeason(season);
                else Log.w(LOG, "multiple seasons found for season "+season.getNumber()+" of series "+getTitle());
            }
            while (cursor.moveToNext());
            Log.d(LOG, seasons.size()+" seasons added in series "+getTitle());
        }
        else {
            Log.w(LOG,"No season found in series " + getTitle());
        }
    }

    @Override
    protected void removeDependencies(SQLiteDatabase dbW, String[] selectionArgs) {
        super.removeDependencies(dbW, selectionArgs);
        String selection;
        //Seasons
        selection = MovieCatalogContract.SeasonEntry.COLUMN_SERIES_ID+ " LIKE ?";
        dbW.delete(MovieCatalogContract.SeasonEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    protected void addDependencies(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        super.addDependencies(dbW, dbR);
        Log.d(LOG,"addDependencies, "+seasons.size()+" seasons");
        for(Season season:seasons) season.putInDB(dbW, dbR);
    }


}

