package com.example.demo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class sale {
	private IntegerProperty sale_id;
	private IntegerProperty book_id;
	private IntegerProperty borrower_id;
	private StringProperty sale_date;
    private DoubleProperty sale_price;
    
    public sale(int sale_id, int book_id,int borrower_id, String sale_date, 
			double sale_price) {
	this.sale_id = new SimpleIntegerProperty(sale_id);
	this.book_id = new SimpleIntegerProperty(book_id);
	this.borrower_id = new SimpleIntegerProperty(borrower_id);
	this.sale_date = new SimpleStringProperty(sale_date);
	this.sale_price = new SimpleDoubleProperty(sale_price);
    																}
	
	public int getSale_id() { return sale_id.get(); }
    public int getBook_id() { return book_id.get(); }
    public int getBorrower_id() { return borrower_id.get(); }
    public String getSale_date() { return sale_date.get(); }
    public double getSale_price() { return sale_price.get(); }
}
