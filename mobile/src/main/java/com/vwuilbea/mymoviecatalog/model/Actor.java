package com.vwuilbea.mymoviecatalog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

public class Actor
        extends Person {

    private static final String LOG = Actor.class.getSimpleName();

    public static final Parcelable.Creator<Actor> CREATOR = new Parcelable.Creator<Actor>() {
        @Override
        public Actor createFromParcel(Parcel source) {
            return new Actor(source);
        }

        @Override
        public Actor[] newArray(int size) {
            return new Actor[size];
        }
    };

    public Actor(int id) {
        super(id);
    }

    public Actor(Parcel in) {
        super(in);
    }

    public int getFromDb(SQLiteDatabase dbR, boolean init) {
        Log.d(LOG, "getFromDb, actor : " + getLongName());
        String[] projection = MovieCatalogContract.ActorEntry.ALL_COLUMNS;
        String WHERE = MovieCatalogContract.ActorEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};

        Cursor cursor = dbR.query(
                MovieCatalogContract.ActorEntry.TABLE_NAME,
                projection,
                WHERE,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor.getCount()>1) return DatabaseHelper.MULTIPLE_RESULTS;
        if(init) initFromCursor(cursor);
        else return cursor.getCount();
        return DatabaseHelper.OK;
    }

    private void initFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        name = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.ActorEntry.COLUMN_NAME));
        firstname = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.ActorEntry.COLUMN_FIRSTNAME));
        birthday = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.ActorEntry.COLUMN_BIRTHDAY));
        profilePath = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.ActorEntry.COLUMN_PROFILE_PATH));
        country = new Country(cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.ActorEntry.COLUMN_COUNTRY_ID)));
        Log.d(LOG, "initFromCursor, actor : " + this);
    }

    public boolean isInDb(SQLiteDatabase dbR) {
        return getFromDb(dbR, false) != 0;
    }

    public int putInDB(SQLiteDatabase dbR, SQLiteDatabase dbW) {
        if (!isInDb(dbR)) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(MovieCatalogContract.ActorEntry._ID, id);
            values.put(MovieCatalogContract.ActorEntry.COLUMN_NAME, name);
            values.put(MovieCatalogContract.ActorEntry.COLUMN_FIRSTNAME, firstname);
            values.put(MovieCatalogContract.ActorEntry.COLUMN_BIRTHDAY, birthday);
            values.put(MovieCatalogContract.ActorEntry.COLUMN_PROFILE_PATH, profilePath);
            values.put(MovieCatalogContract.ActorEntry.COLUMN_COUNTRY_ID, country == null ? DatabaseHelper.ALREADY_IN_DB : country.getId());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = dbW.insert(
                    MovieCatalogContract.ActorEntry.TABLE_NAME,
                    DatabaseHelper.NULL,
                    values);
            if(newRowId!=getId()) return DatabaseHelper.ERROR;
        }
        return DatabaseHelper.OK;
    }

    @Override
    public String toString() {
        return "Actor {" + super.toString() + "}";
    }
}

