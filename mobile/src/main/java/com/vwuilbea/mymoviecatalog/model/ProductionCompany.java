package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

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

    public ProductionCompany(int id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "ProductionCompany{" +
                super.toString() +
                '}';
    }

    @Override
    protected String getTableName() {
        return MovieCatalogContract.ProductionCompanyEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.ProductionCompanyEntry.ALL_COLUMNS;
    }
}

