package com.example.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class loanData {
	public static ObservableList<loan> getAllLoans() {
    	ObservableList<loan> loans = FXCollections.observableArrayList();
        String query = "SELECT * FROM loan";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                loan loan = new loan(
                        rs.getInt("loan_id"),
                        rs.getInt("borrower_id"),
                        rs.getInt("book_id"),
                        rs.getInt("period_id"),
                        rs.getString("loan_date"),
                        rs.getString("due_date"),
                        rs.getString("return_date"));
                loans.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loans;
    }
}
