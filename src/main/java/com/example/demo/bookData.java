package com.example.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class bookData {
	public static ObservableList<book> getAllBooks() {
    	ObservableList<book> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM book";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
            	book book = new book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getInt("publisher_id"),
                        rs.getString("category"),
                        rs.getString("book_type"),
                        rs.getDouble("original_price"),
                        rs.getInt("available"));
            	books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
}
