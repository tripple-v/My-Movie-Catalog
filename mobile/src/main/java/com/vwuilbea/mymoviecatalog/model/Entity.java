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
import com.vwuilbea.mymoviecatalog.database.MyEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public abstract class Entity
        extends MyEntry
        implements Parcelable {

    private static final String LOG = Entity.class.getSimpleName();

    protected int id;
    protected String name;
    protected List<Video> videos = new ArrayList<Video>();

    public Entity() {}

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

    @Override
    protected void initFromCursor(Cursor cursor, SQLiteDatabase dbR) {
        cursor.moveToFirst();
        name = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.EntityEntry.COLUMN_NAME));
    }

    @Override
    protected ContentValues getContentValues() {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(MovieCatalogContract.EntityEntry._ID, id);
            values.put(MovieCatalogContract.EntityEntry.COLUMN_NAME, name);
            return values;
    }

    protected abstract String getVideoEntityTableName();

    @Override
    public int putInDB(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        int res = super.putInDB(dbW,dbR);
        if(res != DatabaseHelper.OK) {
            return res;
        }
        else {
            if(getVideoEntityTableName()!=null) puttAllVideoEntityInDB(dbW, dbR);
            return DatabaseHelper.OK;
        }
    }

    private void puttAllVideoEntityInDB(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        for(Video video:videos) putVideoEntityInDB(dbW, dbR, video);
    }

    private void putVideoEntityInDB(SQLiteDatabase dbW, SQLiteDatabase dbR, Video video) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = MovieCatalogContract.VideoEntityEntry.ALL_COLUMNS;
        String WHERE = MovieCatalogContract.VideoEntityEntry.COLUMN_ENTITY_ID + " = ? AND " +
                MovieCatalogContract.VideoEntityEntry.COLUMN_VIDEO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(getId()), String.valueOf(video.getId())};

        Cursor cursor = dbR.query(
                getVideoEntityTableName(),
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
            values.put(MovieCatalogContract.VideoEntityEntry.COLUMN_ENTITY_ID, id);
            values.put(MovieCatalogContract.VideoEntityEntry.COLUMN_VIDEO_ID, video.getId());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = dbW.insert(
                    getVideoEntityTableName(),
                    DatabaseHelper.NULL,
                    values);
            Log.d(LOG, "new "+getVideoEntityTableName()+" row added: '"+newRowId+"'");
        }
    }


    @Override
    protected void addDependencies(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        //No dependencies here
    }

    @Override
    protected void removeDependencies(SQLiteDatabase dbW, String[] selectionArgs) {
        //No dependencies here
    }

}

