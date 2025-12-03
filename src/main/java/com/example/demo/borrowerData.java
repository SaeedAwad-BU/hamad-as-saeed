package com.example.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class borrowerData {
	public static ObservableList<borrower> getAllBorrowers() {
    	ObservableList<borrower> borrowers = FXCollections.observableArrayList();
        String query = "SELECT * FROM borrower";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                borrower borrower = new borrower(
                        rs.getInt("borrower_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("type_id"),
                        rs.getString("contact_info"));
                borrowers.add(borrower);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrowers;
    }
}
