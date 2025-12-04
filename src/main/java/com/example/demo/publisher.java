package com.example.demo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class publisher {

	private IntegerProperty publisher_id;
    private StringProperty name;
    private StringProperty city;
    private StringProperty country;
    private StringProperty contact_info;
    
    public publisher(int publisher_id, String name, String city, 
    				 String country, String contact_info) {
	this.publisher_id = new SimpleIntegerProperty(publisher_id);
	this.name = new SimpleStringProperty(name);
	this.city = new SimpleStringProperty(city);
	this.country = new SimpleStringProperty(country);
	this.contact_info = new SimpleStringProperty(contact_info);
}

// Getters for TableView binding
public int getPublisher_id() { return publisher_id.get(); }
public String getName() { return name.get(); }
public String getCity() { return city.get(); }
public String getCountry() { return country.get(); }
public String getContact_info() { return contact_info.get(); }
}
