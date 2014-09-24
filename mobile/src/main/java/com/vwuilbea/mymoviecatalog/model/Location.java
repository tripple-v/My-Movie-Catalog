package com.vwuilbea.mymoviecatalog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

public class Location extends Entity
{

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>()
    {
        @Override
        public Location createFromParcel(Parcel source)
        {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size)
        {
            return new Location[size];
        }
    };

	private int type;
	private String folder;

	public Location(int id){
		super(id);
	}

    public Location(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeInt(type);
        dest.writeString(folder);
    }

    @Override
    public String toString() {
        return "Location{" +
                "type=" + type +
                "folder=" + folder +
                ", " + super.toString() +
                '}';
    }

    @Override
    protected String getTableName() {
        return MovieCatalogContract.LocationEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.LocationEntry.ALL_COLUMNS;
    }

    @Override
    protected void initFromCursor(Cursor cursor) {
        super.initFromCursor(cursor);
        type = cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.LocationEntry.COLUMN_TYPE));
        folder = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.LocationEntry.COLUMN_FOLDER));
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues values = super.getContentValues();
        values.put(MovieCatalogContract.LocationEntry.COLUMN_TYPE,type);
        values.put(MovieCatalogContract.LocationEntry.COLUMN_FOLDER,folder);
        return values;
    }

}

