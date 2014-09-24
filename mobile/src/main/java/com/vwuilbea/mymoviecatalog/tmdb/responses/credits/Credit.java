package com.vwuilbea.mymoviecatalog.tmdb.responses.credits;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vwuilbea on 11/09/2014.
 */


public class Credit
        implements Comparable
{

    private static final String PARAM_ID = "id";
    private static final String PARAM_CAST_ID = "cast_id";
    private static final String PARAM_CAST_CHARACTER = "character";
    private static final String PARAM_CAST_CREDIT_ID = "credit_id";
    private static final String PARAM_CAST_NAME = "name";
    private static final String PARAM_CAST_ORDER = "order";
    private static final String PARAM_CAST_PROFILE_PATH = "profile_path";

    public static final Parcelable.Creator<Credit> CREATOR = new Parcelable.Creator<Credit>()
    {
        @Override
        public Credit createFromParcel(Parcel source)
        {
            return new Credit(source);
        }

        @Override
        public Credit[] newArray(int size)
        {
            return new Credit[size];
        }
    };

    private int videoId;
    private int castId;
    private String character;
    private String creditId;
    private int id;
    private String name;
    private int order;
    private String profilePath;

    public Credit(JSONObject object, int videoId) {
        try {
            this.videoId = videoId;
            this.character = object.getString(PARAM_CAST_CHARACTER);
            this.creditId = object.getString(PARAM_CAST_CREDIT_ID);
            this.id = object.getInt(PARAM_ID);
            this.name = object.getString(PARAM_CAST_NAME);
            this.order = object.getInt(PARAM_CAST_ORDER);
            this.profilePath = object.getString(PARAM_CAST_PROFILE_PATH);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Credit(Parcel in) {
        this.videoId = in.readInt();
        this.character = in.readString();
        this.creditId = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.order = in.readInt();
        this.profilePath = in.readString();
    }

    public int getVideoId() {
        return videoId;
    }

    public String getCharacter() {
        return character;
    }

    public String getCreditId() {
        return creditId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    @Override
    public int compareTo(Object another) {
        if(another==null || creditId==null) return -1;
        if(another instanceof Credit)
            return creditId.compareTo(((Credit)another).creditId);
        else return -1;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "castId=" + castId +
                ", character='" + character + '\'' +
                ", creditId='" + creditId + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", profilePath='" + profilePath + '\'' +
                '}';
    }
}