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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public abstract class Entity
        implements Parcelable {

    private static final String LOG = Entity.class.getSimpleName();

    protected int id;
    protected String name;
    protected List<Video> videos = new ArrayList<Video>();

    public Entity(int id) {
        this(id, null);
    }

    public Entity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    protected Entity(int id, String name) {
        this(id, name, null);
    }

    protected Entity(int id, String name, Video video) {
        this.id = id;
        this.name = name;
        addVideo(video);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public void addVideo(Video video) {
        if(video!=null && !this.videos.contains(video)) this.videos.add(video);
    }

    public void removeVideo(Video video) {
        this.videos.remove(video);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object) this).getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        if (id != entity.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    protected abstract String getTableName();
    protected abstract String[] getAllColumns();

    public int getFromDb(SQLiteDatabase dbR, boolean init) {
        String[] projection = getAllColumns();
        String WHERE = BaseColumns._ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};


        Cursor cursor = dbR.query(
                getTableName(),
                projection,
                WHERE,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor.getCount()>1) return DatabaseHelper.MULTIPLE_RESULTS;
        if(init && cursor.getCount()>0) initFromCursor(cursor);
        else return cursor.getCount();
        return DatabaseHelper.OK;
    }

    public int putInDB(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        if (!isInDb(dbR)) {
            // Insert the new row, returning the primary key value of the new row
            long newRowId = dbW.insert(
                    getTableName(),
                    DatabaseHelper.NULL,
                    getContentValues());
            Log.d(LOG, "new '"+getTableName()+"' row added: '" + newRowId + "'");

            if (newRowId != id)
                return DatabaseHelper.ERROR;
        }
        return DatabaseHelper.OK;
    }

    protected void initFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        name = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.EntityEntry.COLUMN_NAME));
    }

    public boolean isInDb(SQLiteDatabase dbR) {
        return getFromDb(dbR, false) != 0;
    }

    protected ContentValues getContentValues() {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(MovieCatalogContract.EntityEntry._ID, id);
            values.put(MovieCatalogContract.EntityEntry.COLUMN_NAME, name);
            return values;
    }
}

