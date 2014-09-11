package com.vwuilbea.mymoviecatalog.model;
import java.util.Set;
import java.util.HashSet;

public class Country extends Entity
{
	
	private Set<Person> actors;

	public Country(){
		super();
	}

    public Set<Person> getActors() {
        return actors;
    }

    public void setActors(Set<Person> actors) {
        this.actors = actors;
    }
}

