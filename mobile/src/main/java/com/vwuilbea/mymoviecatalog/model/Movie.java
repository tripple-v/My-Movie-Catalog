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

public class Movie
        extends Video
{

    private static final String LOG = Movie.class.getSimpleName();

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel source)
        {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

    public Movie(int id){
		super(id);
	}

    public Movie(Parcel in) {
        super(in);
    }

    @Override
    protected String getTableName() {
        return MovieCatalogContract.MovieEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.MovieEntry.ALL_COLUMNS;
    }

    public Movie(Cursor cursor, SQLiteDatabase dbR) {
        super(cursor, dbR);
    }

    protected static List<Movie> getMoviesFromDb(SQLiteDatabase dbR) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = MovieCatalogContract.MovieEntry.ALL_COLUMNS;
        Cursor cursor = dbR.query(MovieCatalogContract.MovieEntry.TABLE_NAME,projection,null,null,null,null,null);
        while(cursor.moveToNext()) movies.add(new Movie(cursor, dbR));
        return movies;
    }

}

