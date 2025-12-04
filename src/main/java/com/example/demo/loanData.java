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
        String query = "SELECT loan_id,CONCAT(b.first_name,' ',b.last_name) as borrower_name,bo.title,lo.period_name,loan.loan_date,due_date,return_date FROM loan LEFT JOIN borrower AS b ON loan.borrower_id=b.borrower_id LEFT JOIN book  as bo ON loan.book_id=bo.book_id LEFT JOIN loanperiod as lo ON lo.period_id=loan.loanperiod_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                loan loan = new loan(
                        rs.getInt("loan_id"),
                        rs.getString("borrower_name"),
                        rs.getString("title"),
                        rs.getString("period_name"),
                        rs.getString("loan_date"),
                        rs.getString("due_date"),
                        rs.getString("return_date")==null?"Not Returned":rs.getString("return_date")

                );
                loans.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loans;
    }
}
