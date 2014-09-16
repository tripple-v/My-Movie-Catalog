package com.vwuilbea.mymoviecatalog.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
}

