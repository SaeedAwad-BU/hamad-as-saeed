package com.example.demo;

import java.time.LocalDate;

import javafx.beans.property.*;

public class Author {
	
	private IntegerProperty author_id;
    private StringProperty full_name;
    private StringProperty country;
    private StringProperty bio;

    public Author(int author_id, String full_name,
                  String country, String bio) {
        this.author_id = new SimpleIntegerProperty(author_id);
        this.full_name = new SimpleStringProperty(full_name);
        this.country = new SimpleStringProperty(country);
        this.bio = new SimpleStringProperty(bio);

    }
    public int getAuthor_id() { return author_id.get(); }
    public String getCountry() { return country.get(); }
    public String getBio() { return bio.get(); }
    public String getFull_name() { return full_name.get(); }
}
