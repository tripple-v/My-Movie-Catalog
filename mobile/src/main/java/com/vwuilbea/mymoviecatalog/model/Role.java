package com.vwuilbea.mymoviecatalog.model;

/**
 * Created by Valentin on 31/08/2014.
 */
public class Role {

    private Actor actor;
    private Movie movie;
    private String character;

    public Role(Actor actor, String character, Movie movie) {
        this.actor = actor;
        this.character = character;
        this.movie = movie;
    }
}
