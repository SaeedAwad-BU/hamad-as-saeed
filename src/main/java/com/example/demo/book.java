package com.example.demo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class book {
	private IntegerProperty book_id;
	@Override
	public String toString() {
		String str = (available.get()>0)?"yes":"no";
		return  book_id.get() +","+ title.get() +","+ publisher_id.get()
				+","+ category.get()+","+book_type.get()+","+ original_price.get()+","
				+ str;
	}
	private StringProperty title;
	private IntegerProperty publisher_id;
	private StringProperty category;
	private StringProperty book_type;
    private DoubleProperty original_price;
    private IntegerProperty available;
    
    public book(int book_id, String title, int publisher_id, String category, String book_type, 
			double original_price, int available) {
	this.book_id = new SimpleIntegerProperty(book_id);
	this.title = new SimpleStringProperty(title);
	this.publisher_id = new SimpleIntegerProperty(publisher_id);
	this.category = new SimpleStringProperty(category);
	this.book_type = new SimpleStringProperty(book_type);
	this.original_price = new SimpleDoubleProperty(original_price);
	this.available = new SimpleIntegerProperty(available);
    																}
	
	public int getBook_id() { return book_id.get(); }
    public String getTitle() { return title.get(); }
    public int getPublisher_id() { return publisher_id.get(); }
    public String getCategory() { return category.get(); }
    public String getBook_type() { return book_type.get(); }
    public double getOriginal_price() { return original_price.get(); }
    public int getAvailable() { return available.get(); }
    
    

}
