package com.vwuilbea.mymoviecatalog.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


public class Video
    implements Parcelable
{

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>()
    {
        @Override
        public Video createFromParcel(Parcel source)
        {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size)
        {
            return new Video[size];
        }
    };

	private int id;
	private String title;
	private String originalTitle;
	private String tagline;
	private String overview;
	private Genre genre;
	private int runtime;
	private int year;
	private List<Role> roles = new ArrayList<Role>();
	private List<Realisator> realisators = new ArrayList<Realisator>();
	private List<Country> countries = new ArrayList<Country>();
	private List<ProductionCompany> productionCompanies = new ArrayList<ProductionCompany>();
	private String language;
	private String subtitle;
	private Location location;
	private boolean adult;
	private String posterPath;
	private int budget;
	private double voteAverage;
	private int voteCount;

    public Video(int id){
		super();
	}

    public Video(Parcel in) {
        id = in.readInt();
        title = in.readString();
        originalTitle = in.readString();
        tagline = in.readString();
        overview = in.readString();
        genre = in.readParcelable(Genre.class.getClassLoader());
        runtime = in.readInt();
        year = in.readInt();
        in.readList(roles,Role.class.getClassLoader());
        in.readList(realisators,Realisator.class.getClassLoader());
        in.readList(countries,Country.class.getClassLoader());
        in.readList(productionCompanies,ProductionCompany.class.getClassLoader());
        language = in.readString();
        subtitle = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        adult = Boolean.parseBoolean(in.readString());
        posterPath = in.readString();
        budget = in.readInt();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(tagline);
        dest.writeString(overview);
        dest.writeParcelable(genre, flags);
        dest.writeInt(runtime);
        dest.writeInt(year);
        dest.writeArray(roles.toArray());
        dest.writeList(realisators);
        dest.writeList(countries);
        dest.writeList(productionCompanies);
        dest.writeString(language);
        dest.writeString(subtitle);
        dest.writeParcelable(location,flags);
        dest.writeString(String.valueOf(adult));
        dest.writeString(posterPath);
        dest.writeInt(budget);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Realisator> getRealisators() {
        return realisators;
    }

    public void setRealisators(List<Realisator> realisators) {
        this.realisators = realisators;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", tagline='" + tagline + '\'' +
                ", overview='" + overview + '\'' +
                ", genre=" + genre +
                ", runtime=" + runtime +
                ", year=" + year +
                ", roles=" + roles +
                ", realisators=" + realisators +
                ", countries=" + countries +
                ", productionCompanies=" + productionCompanies +
                ", language='" + language + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", location=" + location +
                ", adult=" + adult +
                ", posterPath='" + posterPath + '\'' +
                ", budget=" + budget +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }
}

