package com.vwuilbea.mymoviecatalog.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

public class Actor
        extends Person {

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

    public boolean isInDb(SQLiteDatabase dbR) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {MovieCatalogContract.ActorEntry._ID};
        String WHERE = MovieCatalogContract.ActorEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};

        Cursor c = dbR.query(
                MovieCatalogContract.ActorEntry.TABLE_NAME,
                projection,
                WHERE,
                selectionArgs,
                null,
                null,
                null
        );
        return c.getCount()!=0;
    }

    @Override
    public String toString() {
        return "Actor {" + super.toString() + "}";
    }
}

