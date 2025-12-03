package com.example.demo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class borrowertype {
		private IntegerProperty type_id;
		private StringProperty type_name;
		public borrowertype(int type_id, String type_name) {
			this.type_id = new SimpleIntegerProperty(type_id);
			this.type_name = new SimpleStringProperty(type_name);}
		
		// Getters for TableView binding
	    public int getType_id() { return type_id.get(); }
	    public String getType_name() { return type_name.get(); }
}
