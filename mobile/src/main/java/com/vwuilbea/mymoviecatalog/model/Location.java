package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

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

}

