package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie
        extends Video
{

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel source)
        {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

	public Movie(int id){
		super(id);
	}

    public Movie(Parcel in) {
        super(in);
    }

    @Override
    public String toString() {
        return "Location{" +
                super.toString() +
                '}';
    }
}

