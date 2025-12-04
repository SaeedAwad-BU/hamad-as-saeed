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

		return  book_id.get() +","+ title.get() +","+ publisher_name.get()
				+","+ category.get()+","+book_type.get()+","+ original_price.get()+","
				+ available;
	}
	private StringProperty title;
	private StringProperty publisher_name;
	private StringProperty category;
	private StringProperty book_type;
    private DoubleProperty original_price;
    private StringProperty available;
    
    public book(int book_id, String title, String publisher_name, String category, String book_type,
			double original_price, String available) {
	this.book_id = new SimpleIntegerProperty(book_id);
	this.title = new SimpleStringProperty(title);
	this.publisher_name = new SimpleStringProperty(publisher_name);
	this.category = new SimpleStringProperty(category);
	this.book_type = new SimpleStringProperty(book_type);
	this.original_price = new SimpleDoubleProperty(original_price);
	this.available = new SimpleStringProperty(available);}


    public int getBook_id() { return book_id.get(); }
    public String getTitle() { return title.get(); }
    public String getPublisher_name() { return publisher_name.get(); }
    public String getCategory() { return category.get(); }
    public String getBook_type() { return book_type.get(); }
    public double getOriginal_price() { return original_price.get(); }
    public String getAvailable() { return available.get(); }
    
    

}
