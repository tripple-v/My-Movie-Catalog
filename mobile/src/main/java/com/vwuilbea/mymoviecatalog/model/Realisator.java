package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

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

    @Override
    protected String getTableName() {
        return MovieCatalogContract.RealisatorEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.RealisatorEntry.ALL_COLUMNS;
    }
}

