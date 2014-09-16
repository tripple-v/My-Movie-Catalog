package com.vwuilbea.mymoviecatalog.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

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

    public Role(String roleId) {
        this(roleId, null, null, null);
    }

    public Role(String roleId, Actor actor, Video video, String character) {
        setRoleId(roleId);
        setActor(actor);
        setVideo(video);
        setCharacter(character);
    }

    public Role(Parcel in) {
        this.roleId = in.readString();
        this.actor = in.readParcelable(Actor.class.getClassLoader());
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
        if(video!=null) {
            this.video = video;
            video.addRole(this);
        }
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }



    public int getFromDb(SQLiteDatabase dbR, boolean init) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = MovieCatalogContract.RoleEntry.ALL_COLUMNS;
        String WHERE = MovieCatalogContract.RoleEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(getRoleId())};

        Cursor cursor = dbR.query(
                MovieCatalogContract.RoleEntry.TABLE_NAME,
                projection,
                WHERE,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor.getCount()>1) return DatabaseHelper.MULTIPLE_RESULTS;
        if(init && cursor.getCount()>0) initFromCursor(dbR, cursor);
        else return cursor.getCount();
        return DatabaseHelper.OK;
    }

    private void initFromCursor(SQLiteDatabase dbR, Cursor cursor) {
        cursor.moveToFirst();
        setCharacter( cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.RoleEntry.COLUMN_CHARACTER)) );
        addActorFromDB(dbR, cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.RoleEntry.COLUMN_ACTOR_ID)));
    }

    protected void addActorFromDB(SQLiteDatabase dbR, int actorId) {
        Actor actor = new Actor(actorId);
        actor.getFromDb(dbR, true);
        setActor(actor);
    }

    public boolean isInDb(SQLiteDatabase dbR) {
        return getFromDb(dbR, false) != 0;
    }

    public void putInDB(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        int actorRes = actor.putInDB(dbR, dbW);
        if(actorRes == DatabaseHelper.OK) {

        }
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId='" + roleId + '\'' +
                ", actor=" + actor +
                ", character='" + character + '\'' +
                '}';
    }
}
