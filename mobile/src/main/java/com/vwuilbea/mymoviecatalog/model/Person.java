package com.vwuilbea.mymoviecatalog.model;

public abstract class Person extends Entity
{
	
	private String firstname;
	private String birthday;
	private Country country;
    private String profilePath;

	public Person(){
		super();
	}

}

