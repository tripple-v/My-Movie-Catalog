package com.vwuilbea.mymoviecatalog.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

public abstract class Person
        extends Entity
{

	protected String firstname;
    protected String birthday;
    protected Country country;
    protected String profilePath;

	public Person(int id ){
		super(id);
	}

    public Person(Parcel in ) {
        super(in);
        this.firstname = in.readString();
        this.birthday = in.readString();
        this.country = in.readParcelable(Country.class.getClassLoader());
        this.profilePath = in.readString();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLongName() {
        if(firstname==null) return name;
        return firstname.substring(0,1).toUpperCase() +
                firstname.substring(1).toLowerCase() +
                " " + name.toUpperCase();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(firstname);
        dest.writeString(birthday);
        dest.writeParcelable(country,flags);
        dest.writeString(profilePath);
    }

    @Override
    public String toString() {
        return "firstname='" + firstname + '\'' +
                ", birthday='" + birthday + '\'' +
                ", country=" + country +
                ", profilePath='" + profilePath + '\'' +
                ", " + super.toString();
    }

    @Override
    protected void initFromCursor(Cursor cursor, SQLiteDatabase dbR) {
        super.initFromCursor(cursor, dbR);
        firstname = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.PersonEntry.COLUMN_FIRSTNAME));
        birthday = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.PersonEntry.COLUMN_BIRTHDAY));
        profilePath = cursor.getString(cursor.getColumnIndexOrThrow(MovieCatalogContract.PersonEntry.COLUMN_PROFILE_PATH));
        country = new Country(cursor.getInt(cursor.getColumnIndexOrThrow(MovieCatalogContract.PersonEntry.COLUMN_COUNTRY_ID)));
    }

    @Override
    protected ContentValues getContentValues() {
        ContentValues values = super.getContentValues();
        values.put(MovieCatalogContract.PersonEntry.COLUMN_FIRSTNAME,firstname);
        values.put(MovieCatalogContract.PersonEntry.COLUMN_BIRTHDAY,birthday);
        values.put(MovieCatalogContract.PersonEntry.COLUMN_PROFILE_PATH,profilePath);
        if(country!=null) values.put(MovieCatalogContract.PersonEntry.COLUMN_COUNTRY_ID,country.getId());
        return values;
    }
}

