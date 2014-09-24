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
    protected String getTableName() {
        return MovieCatalogContract.GenreEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.GenreEntry.ALL_COLUMNS;
    }

    @Override
    public int putInDB(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        int res = super.putInDB(dbW,dbR);
        if(res != DatabaseHelper.OK) {
            return res;
        }
        else {
            puttAllVideoGenreInDB(dbW, dbR);
            return DatabaseHelper.OK;
        }
    }

    private void puttAllVideoGenreInDB(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        for(Video video:videos) putVideoGenreInDB(dbW, dbR, video);
    }

    private void putVideoGenreInDB(SQLiteDatabase dbW, SQLiteDatabase dbR, Video video) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = MovieCatalogContract.VideoGenreEntry.ALL_COLUMNS;
        String WHERE = MovieCatalogContract.VideoGenreEntry.COLUMN_GENRE_ID + " = ? AND " +
                MovieCatalogContract.VideoGenreEntry.COLUMN_VIDEO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(getId()), String.valueOf(video.getId())};

        Cursor cursor = dbR.query(
                MovieCatalogContract.VideoGenreEntry.TABLE_NAME,
                projection,
                WHERE,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor.getCount() == 0) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(MovieCatalogContract.VideoGenreEntry.COLUMN_GENRE_ID, id);
            values.put(MovieCatalogContract.VideoGenreEntry.COLUMN_VIDEO_ID, video.getId());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = dbW.insert(
                    MovieCatalogContract.VideoGenreEntry.TABLE_NAME,
                    DatabaseHelper.NULL,
                    values);
            Log.d(LOG, "new VideoGenre row added: '"+newRowId+"'");
        }
    }

}

