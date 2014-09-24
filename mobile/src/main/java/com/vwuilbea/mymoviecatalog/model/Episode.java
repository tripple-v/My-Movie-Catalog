package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vwuilbea on 24/09/2014.
 */
public class Episode implements Parcelable {

    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>()
    {
        @Override
        public Episode createFromParcel(Parcel source)
        {
            return new Episode(source);
        }

        @Override
        public Episode[] newArray(int size)
        {
            return new Episode[size];
        }
    };

    private static final String LOG = Episode.class.getSimpleName();

    private int id;
    private Season season;
    private int number;
    private boolean possessed;
    private String date;
    private String title;
    private String overview;
    private String posterPath;
    private float voteAverage;
    private int voteCount;

    public Episode(int id) {
        this(id, null);
    }

    public Episode(int id, Season season) {
        this(id, season, season.isPossessed());
    }

    public Episode(int id, Season season, boolean possessed) {
        this.id = id;
        this.season = season;
        this.possessed = possessed;
    }

    public Episode(Parcel in) {
        id = in.readInt();
        season = in.readParcelable(Season.class.getClassLoader());
        number = in.readInt();
        possessed = Boolean.parseBoolean(in.readString());
        date = in.readString();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        voteAverage = in.readFloat();
        voteCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(season, flags);
        dest.writeInt(number);
        dest.writeString(String.valueOf(possessed));
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeFloat(voteAverage);
        dest.writeInt(voteCount);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isPossessed() {
        return possessed;
    }

    public void setPossessed(boolean possessed) {
        this.possessed = possessed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
