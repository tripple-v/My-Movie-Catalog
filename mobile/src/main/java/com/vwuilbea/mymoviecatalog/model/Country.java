package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;

public class Country extends Entity {

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>()
    {
        @Override
        public Country createFromParcel(Parcel source)
        {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size)
        {
            return new Country[size];
        }
    };
    private static int _ID;

    private List<Person> persons = new ArrayList<Person>();
    private String iso;

    public Country(int id) {
        super(_ID++);
    }

    public Country(Parcel in) {
        super(in);
        in.readList(persons, Person.class.getClassLoader());
        iso = in.readString();
    }

    public Country(String name, String iso) {
        this(name,iso,null);
    }

    public Country(String name, String iso, Person person) {
        super(_ID++, name);
        this.iso = iso;
        addPerson(person);
    }

    public void addPerson(Person person) {
        if(person!=null && !persons.contains(person)) persons.add(person);
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(persons);
        dest.writeString(iso);
    }

    @Override
    public String toString() {
        return "Country{" +
                "iso=" + iso +
                ", persons=" + persons +
                ", " + super.toString() +
                '}';
    }

    @Override
    protected String getTableName() {
        return MovieCatalogContract.CountryEntry.TABLE_NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return MovieCatalogContract.CountryEntry.ALL_COLUMNS;
    }
}

