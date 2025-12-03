package com.example.demo;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class loanperiod {
	private IntegerProperty period_id;
	private StringProperty period_name;
	private IntegerProperty days;

	public loanperiod(int period_id, String period_name, int days) {
		this.period_id = new SimpleIntegerProperty(period_id);
		this.period_name = new SimpleStringProperty(period_name);
		this.days = new SimpleIntegerProperty(days);
	}
	 	public int getPeriod_id() { return period_id.get(); }
	    public String getPeriod_name() { return period_name.get(); }
	    public int getDays() { return days.get(); }
}
