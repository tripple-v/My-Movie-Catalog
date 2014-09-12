package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Realisator extends Person
{

    public static final Parcelable.Creator<Realisator> CREATOR = new Parcelable.Creator<Realisator>()
    {
        @Override
        public Realisator createFromParcel(Parcel source)
        {
            return new Realisator(source);
        }

        @Override
        public Realisator[] newArray(int size)
        {
            return new Realisator[size];
        }
    };

	public Realisator(int id){
		super(id);
	}

    public Realisator(Parcel in) {
        super(in);
    }

    @Override
    public String toString() {
        return "Realisator{" +
                super.toString() +
                '}';
    }
}

