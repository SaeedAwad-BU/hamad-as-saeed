package com.example.demo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class borrower {
	private IntegerProperty borrower_id;
	private StringProperty first_name;
    private StringProperty last_name;
    private IntegerProperty type_id;
    private StringProperty contact_info;
	public borrower(int borrower_id, String first_name, String last_name,
			int type_id, String contact_info) {
		this.borrower_id = new SimpleIntegerProperty(borrower_id);
		this.first_name = new SimpleStringProperty(first_name);
		this.last_name = new SimpleStringProperty(last_name);
		this.type_id = new SimpleIntegerProperty(type_id);
		this.contact_info = new SimpleStringProperty(contact_info);
	}
	
	// Getters for TableView binding
    public int getBorrower_id() { return borrower_id.get(); }
    public String getFirst_name() { return first_name.get(); }
    public String getLast_name() { return last_name.get(); }
    public int getType_id() { return type_id.get(); }
    public String getContact_info() { return contact_info.get(); }
}
