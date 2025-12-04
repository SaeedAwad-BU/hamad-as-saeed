package com.example.demo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class borrower {
	private IntegerProperty borrower_id;
    private StringProperty first_name;
    private StringProperty full_name;
    private StringProperty type_name;
    private StringProperty contact_info;
    private StringProperty last_name;
	public borrower(int borrower_id,String first_name,String last_name,String type_name, String contact_info) {
		this.borrower_id = new SimpleIntegerProperty(borrower_id);
        this.first_name = new SimpleStringProperty(first_name);
        this.last_name = new SimpleStringProperty(last_name);
		this.type_name = new SimpleStringProperty(type_name);
		this.contact_info = new SimpleStringProperty(contact_info);
	}
     public borrower(int borrower_id,String full_name,String type_name, String contact_info) {
        this.borrower_id = new SimpleIntegerProperty(borrower_id);
        this.full_name = new SimpleStringProperty(full_name);
        this.type_name = new SimpleStringProperty(type_name);
        this.contact_info = new SimpleStringProperty(contact_info);
    }
	
	// Getters for TableView binding
    public int getBorrower_id() { return borrower_id.get(); }
    public String getFull_name() { return full_name.get(); }
    public String getType_name() { return type_name.get(); }
    public String getContact_info() { return contact_info.get(); }

    public String getLast_name() {
        return last_name.get();
    }

    public StringProperty last_nameProperty() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name.set(last_name);
    }

    public String getFirst_name() {
        return first_name.get();
    }

    public StringProperty first_nameProperty() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name.set(first_name);
    }
}
