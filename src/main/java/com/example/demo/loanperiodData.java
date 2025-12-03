package com.example.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class loanperiodData {
	public static ObservableList<loanperiod> getAllLoanperiods() {
    	ObservableList<loanperiod> loanperiods = FXCollections.observableArrayList();
        String query = "SELECT * FROM loanperiod";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                loanperiod loanperiod = new loanperiod(
                        rs.getInt("period_id"),
                        rs.getString("period_name"),
                        rs.getInt("days"));
                loanperiods.add(loanperiod);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loanperiods;
    }
}
