package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductionCompany extends Entity
{

    public static final Parcelable.Creator<ProductionCompany> CREATOR = new Parcelable.Creator<ProductionCompany>()
    {
        @Override
        public ProductionCompany createFromParcel(Parcel source)
        {
            return new ProductionCompany(source);
        }

        @Override
        public ProductionCompany[] newArray(int size)
        {
            return new ProductionCompany[size];
        }
    };

	public ProductionCompany(int id){
		super(id);
	}

    public ProductionCompany(Parcel in) {
        super(in);
    }

    @Override
    public String toString() {
        return "ProductionCompany{" +
                super.toString() +
                '}';
    }
}

