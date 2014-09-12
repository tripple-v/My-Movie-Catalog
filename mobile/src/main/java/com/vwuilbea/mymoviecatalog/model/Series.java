package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Series
        extends Video
{

    public static final Parcelable.Creator<Series> CREATOR = new Parcelable.Creator<Series>()
    {
        @Override
        public Series createFromParcel(Parcel source)
        {
            return new Series(source);
        }

        @Override
        public Series[] newArray(int size)
        {
            return new Series[size];
        }
    };
	
	private int season;

	public Series(int id){
		super(id);
	}

    public Series(Parcel in) {
        super(in);
        this.season = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeInt(season);
    }

    @Override
    public String toString() {
        return "Series{" +
                "season=" + season +
                ", " + super.toString() +
                '}';
    }
}

