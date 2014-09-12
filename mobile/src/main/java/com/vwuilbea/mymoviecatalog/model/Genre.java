package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre extends Entity
{

    public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>()
    {
        @Override
        public Genre createFromParcel(Parcel source)
        {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size)
        {
            return new Genre[size];
        }
    };

	public Genre(int id ){
		super(id);
	}

    public Genre(Parcel in) {
        super (in);
    }

    @Override
    public String toString() {
        return "Genre{" +
                super.toString() +
                '}';
    }

}

