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

    public Movie(Cursor cursor, SQLiteDatabase dbR) {
        this(cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry._ID)));
        initFromCursor(cursor, dbR);
    }

    @Override
    public String toString() {
        return "Location{" +
                super.toString() +
                '}';
    }

    public static List<Movie> getMoviesFromDb(SQLiteDatabase dbR) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = MovieCatalogContract.MovieEntry.ALL_COLUMNS;
        Cursor cursor = dbR.query(MovieCatalogContract.MovieEntry.TABLE_NAME,projection,null,null,null,null,null);
        while(cursor.moveToNext()) movies.add(new Movie(cursor, dbR));
        return movies;
    }

    public int getFromDb(SQLiteDatabase dbR, boolean init) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {MovieCatalogContract.MovieEntry._ID};
        String WHERE = MovieCatalogContract.MovieEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};

        Cursor cursor = dbR.query(
                MovieCatalogContract.MovieEntry.TABLE_NAME,
                projection,
                WHERE,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor.getCount()>1) return DatabaseHelper.MULTIPLE_RESULTS;
        if(init) initFromCursor(cursor, dbR);
        else return cursor.getCount();
        return DatabaseHelper.OK;
    }

    private void initFromCursor(Cursor cursor, SQLiteDatabase dbR) {
        Log.d(LOG, "initFromCursor, movie : "+getId());
        title = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_TITLE));
        originalTitle = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
        tagline = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_TAG_LINE));
        overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_OVERVIEW));
        runtime = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_RUNTIME));
        releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_DATE));
        language = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_LANGUAGE));
        subtitle = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_SUBTITLE));
        adult = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_ADULT)));
        posterPath = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_POSTER_PATH));
        coverPath = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_COVER_PATH));
        budget = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_BUDGET));
        voteAverage = cursor.getDouble(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_VOTE_AVERAGE));
        voteCount = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.MovieEntry.COLUMN_VOTE_COUNT));
        addGenreFromDB(dbR);
        addRoleFromDB(dbR);
    }

    public boolean isInDb(SQLiteDatabase dbR) {
        return getFromDb(dbR, false) != 0;
    }

    public int putInDB(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        if (!isInDb(dbR)) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(MovieCatalogContract.MovieEntry._ID, id);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_TITLE, title);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_TAG_LINE, tagline);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_OVERVIEW, overview);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_RUNTIME, runtime);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_DATE, releaseDate);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_LANGUAGE, language);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_SUBTITLE, subtitle);
            if (location != null) {
                values.put(MovieCatalogContract.MovieEntry.COLUMN_LOCATION_ID, location.getId());
            }
            values.put(MovieCatalogContract.MovieEntry.COLUMN_ADULT, adult);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_COVER_PATH, coverPath);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_BUDGET, budget);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
            values.put(MovieCatalogContract.MovieEntry.COLUMN_VOTE_COUNT, voteCount);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = dbW.insert(
                    MovieCatalogContract.MovieEntry.TABLE_NAME,
                    DatabaseHelper.NULL,
                    values);

            if (newRowId == id) {
                //We can add other tables rows
                for(Genre genre:genres) genre.putInDB(dbW, dbR);
                for(Role role:roles) role.putInDB(dbW, dbR);
            }
            else return DatabaseHelper.ERROR;
        }
        return DatabaseHelper.OK;
    }

    public int removeFromDB(SQLiteDatabase dbW) {
        String selection = MovieCatalogContract.MovieEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        removeDependencies(dbW, selectionArgs);
        return dbW.delete(MovieCatalogContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void removeDependencies(SQLiteDatabase dbW, String[] selectionArgs) {
        String selection;
        //VideoGenres
        selection = MovieCatalogContract.VideoGenreEntry.COLUMN_VIDEO_ID+ " LIKE ?";
        dbW.delete(MovieCatalogContract.VideoGenreEntry.TABLE_NAME, selection, selectionArgs);
        //Roles
        selection = MovieCatalogContract.RoleEntry.COLUMN_VIDEO_ID+ " LIKE ?";
        dbW.delete(MovieCatalogContract.RoleEntry.TABLE_NAME, selection, selectionArgs);
    }
}

