package com.example.demo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class sale {
	private IntegerProperty sale_id;
	private StringProperty title;
	private StringProperty borrower_name;
	private StringProperty sale_date;
    private DoubleProperty sale_price;
    
    public sale(int sale_id, String title,String borrower_name, String sale_date,
			double sale_price) {
	this.sale_id = new SimpleIntegerProperty(sale_id);
	this.title = new SimpleStringProperty(title);
	this.borrower_name = new SimpleStringProperty(borrower_name);
	this.sale_date = new SimpleStringProperty(sale_date);
	this.sale_price = new SimpleDoubleProperty(sale_price);
    																}
	
	public int getSale_id() { return sale_id.get(); }
    public String getTitle() { return title.get(); }
    public String getBorrower_name() { return borrower_name.get(); }
    public String getSale_date() { return sale_date.get(); }
    public double getSale_price() { return sale_price.get(); }
}
