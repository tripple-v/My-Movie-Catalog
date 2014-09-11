package com.vwuilbea.mymoviecatalog.model;
import java.util.Set;
import java.util.HashSet;

public abstract class Entity
{
	
	private int id;
	private String name;
	private Set<Video> videos;

	public Entity(){
		super();
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public void setVideos(Set<Video> videos) {
        this.videos = videos;
    }

}

