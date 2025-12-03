package com.example.demo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class loan {
	private IntegerProperty loan_id;
	private IntegerProperty borrower_id;
	private IntegerProperty book_id;
	private IntegerProperty period_id;
    private StringProperty loan_date;
    private StringProperty due_date;
    private StringProperty return_date;
	public loan(int loan_id, int borrower_id,int book_id,int period_id, 
				String loan_date, String due_date, String return_date) {
		this.loan_id = new SimpleIntegerProperty(loan_id);
		this.borrower_id = new SimpleIntegerProperty(borrower_id);
		this.book_id = new SimpleIntegerProperty(book_id);
		this.period_id = new SimpleIntegerProperty(period_id);
		this.loan_date = new SimpleStringProperty(loan_date);
		this.due_date = new SimpleStringProperty(due_date);
		this.return_date = new SimpleStringProperty(return_date);
		
	}
	
	// Getters for TableView binding
    public int getLoan_id() { return loan_id.get(); }
    public int getBorrower_id() { return borrower_id.get(); }
    public int getBook_id() { return book_id.get(); }
    public int getPeriod_id() { return period_id.get(); }
    public String getLoan_date() { return loan_date.get(); }
    public String getDue_date() { return due_date.get(); }
    public String getReturn_date() { return return_date.get(); }
}
