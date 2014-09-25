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

    @Override
    public String toString() {
        return "Actor {" + super.toString() + "}";
    }

    @Override
    protected String getTableName() {
        return MovieCatalogContract.ActorEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.ActorEntry.ALL_COLUMNS;
    }

    @Override
    protected void initFromCursor(Cursor cursor, SQLiteDatabase dbR) {
        super.initFromCursor(cursor, dbR);
        Log.d(LOG, "initFromCursor, actor : " + this);
    }

    @Override
    protected String getVideoEntityTableName() {
        return null;
    }
}

