package com.vwuilbea.mymoviecatalog.model;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Valentin on 31/08/2014.
 */
public class Role
    implements Parcelable
{

    private static final String LOG = Role.class.getSimpleName();

    public static final Parcelable.Creator<Role> CREATOR = new Parcelable.Creator<Role>()
    {
        @Override
        public Role createFromParcel(Parcel source)
        {
            return new Role(source);
        }

        @Override
        public Role[] newArray(int size)
        {
            return new Role[size];
        }
    };

    private String roleId;
    private Actor actor;
    private Video video;
    private String character;

    public Role(String roleId, Actor actor, Video video, String character) {
        this.roleId = roleId;
        this.actor = actor;
        this.video = video;
        this.character = character;
    }

    public Role(Parcel in) {
        this.roleId = in.readString();
        this.actor = in.readParcelable(Actor.class.getClassLoader());
        this.video = in.readParcelable(Video.class.getClassLoader());
        this.character = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roleId);
        dest.writeParcelable(actor, flags);
        dest.writeParcelable(video, flags);
        dest.writeString(character);
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void addInDB(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        Log.d(LOG,"Actor '"+actor.getLongName()+"' is in DB: "+actor.isInDb(dbR));
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId='" + roleId + '\'' +
                ", actor=" + actor +
                ", video=" + video +
                ", character='" + character + '\'' +
                '}';
    }
}
