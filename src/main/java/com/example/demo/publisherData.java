package com.example.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class publisherData {
	public static ObservableList<publisher> getAllPublishers() {
    	ObservableList<publisher> publishers = FXCollections.observableArrayList();
        String query = "SELECT * FROM publisher";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                publisher publisher = new publisher(
                        rs.getInt("publisher_id"),
                        rs.getString("name"),
                        rs.getString("city"),
                        rs.getString("country"),
                        rs.getString("contact_info"));
                publishers.add(publisher);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publishers;
    }
}
