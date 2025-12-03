package com.example.demo;

import java.time.LocalDate;

import javafx.beans.property.*;

public class Author {
	
	private IntegerProperty author_id;
	private StringProperty first_name;
    private StringProperty last_name;
    private StringProperty country;
    private StringProperty bio;
	public Author(int author_id, String first_name, String last_name,
			String country, String bio) {
		this.author_id = new SimpleIntegerProperty(author_id);
		this.first_name = new SimpleStringProperty(first_name);
		this.last_name = new SimpleStringProperty(last_name);
		this.country = new SimpleStringProperty(country);
		this.bio = new SimpleStringProperty(bio);
	}
	
	// Getters for TableView binding
    public int getAuthor_id() { return author_id.get(); }
    public String getFirst_name() { return first_name.get(); }
    public String getLast_name() { return last_name.get(); }
    public String getCountry() { return country.get(); }
    public String getBio() { return bio.get(); }
}
