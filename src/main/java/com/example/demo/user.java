package com.example.demo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class user {
	private StringProperty username;
	private StringProperty password;
    private StringProperty email;
    private StringProperty rule;
	public user(String username, String password, String email, String rule) {
		this.username = new SimpleStringProperty(username);
		this.password = new SimpleStringProperty(password);
		this.email = new SimpleStringProperty(email);
		this.rule = new SimpleStringProperty(rule);
	}
	
	// Getters for TableView binding
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getEmail() { return email.get(); }
    public String getRule() { return rule.get(); }
}
