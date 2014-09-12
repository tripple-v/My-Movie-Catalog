package com.vwuilbea.mymoviecatalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Set;
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

    private Set<Person> actors;

    public Country(int id) {
        super(id);
    }

    public Country(Parcel in) {
        super(in);
        actors = new HashSet<Person>(Arrays.asList((Person[]) in.readArray(Person.class.getClassLoader())));
    }

    public Set<Person> getActors() {
        return actors;
    }

    public void setActors(Set<Person> actors) {
        this.actors = actors;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeArray(actors.toArray());
    }

    @Override
    public String toString() {
        return "Country{" +
                "actors=" + actors +
                ", " + super.toString() +
                '}';
    }
}

