package com.example.demo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class loan {
	private IntegerProperty loan_id;
	private StringProperty borrower_name;
	private StringProperty title;
	private StringProperty period_name;
    private StringProperty loan_date;
    private StringProperty due_date;
    private StringProperty return_date;

	public loan(int loan_id, String borrower_name,String title,String period_name,
				String loan_date, String due_date, String return_date) {
		this.loan_id = new SimpleIntegerProperty(loan_id);
		this.borrower_name = new SimpleStringProperty(borrower_name);
		this.title = new SimpleStringProperty(title);
		this.period_name = new SimpleStringProperty(period_name);
		this.loan_date = new SimpleStringProperty(loan_date);
		this.due_date = new SimpleStringProperty(due_date);
		this.return_date = new SimpleStringProperty(return_date);
		
	}
	
	// Getters for TableView binding
    public int getLoan_id() { return loan_id.get(); }
    public String getBorrower_name() { return borrower_name.get(); }
    public String getTitle() { return title.get(); }
    public String getPeriod_name() { return period_name.get(); }
    public String getLoan_date() { return loan_date.get(); }
    public String getDue_date() { return due_date.get(); }
    public String getReturn_date() { return return_date.get(); }
}
