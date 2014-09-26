package com.vwuilbea.mymoviecatalog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

import java.util.List;

public class Genre extends Entity
{

    public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>()
    {
        @Override
        public Genre createFromParcel(Parcel source)
        {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size)
        {
            return new Genre[size];
        }
    };

    private static final String LOG = Genre.class.getSimpleName();

    public Genre() {
        super();
    }

    public Genre(int id ){
		super(id);
	}

    public Genre(Parcel in) {
        super (in);
    }

    public Genre(int id, String name) {
        super(id, name);
    }

    public Genre(int id, String name, Video video) {
        super(id, name, video);
    }

    @Override
    public String toString() {
        return "Genre{" +
                super.toString() +
                '}';
    }

    @Override
    protected String getVideoEntityTableName() {
        return MovieCatalogContract.VideoGenreEntry.TABLE_NAME;
    }

    @Override
    protected String getTableName() {
        return MovieCatalogContract.GenreEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.GenreEntry.ALL_COLUMNS;
    }

}

