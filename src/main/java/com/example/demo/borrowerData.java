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
        String query = "SELECT borrower_id,CONCAT(first_name,' ',last_name) AS full_name,type_name,contact_info FROM borrower JOIN borrowertype ON borrower.borrowertype_id = borrowertype.type_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {

                borrower borrowers2 = new borrower(
                        rs.getInt("borrower_id"),
                        rs.getString("full_name"),
                        rs.getString("type_name"),
                        rs.getString("contact_info"));
                borrowers.add(borrowers2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrowers;
    }
}
