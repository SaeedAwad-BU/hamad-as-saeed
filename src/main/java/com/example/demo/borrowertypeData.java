package com.example.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class borrowertypeData {
	public static ObservableList<borrowertype> getAllBorrowerTypes() {
    	ObservableList<borrowertype> borrowertypes = FXCollections.observableArrayList();
        String query = "SELECT * FROM borrowertype";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                borrowertype borrowertype = new borrowertype(
                        rs.getInt("type_id"),
                        rs.getString("type_name"));
                borrowertypes.add(borrowertype);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrowertypes;
    }
}
