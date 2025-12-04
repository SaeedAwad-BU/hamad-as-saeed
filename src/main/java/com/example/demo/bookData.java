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
        String query = "SELECT book_id,title,name,category,book_type,original_price,available FROM book LEFT JOIN publisher ON book.publisher_id = publisher.publisher_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
            	book book = new book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("book_type"),
                        rs.getDouble("original_price"),
                        rs.getInt("available")>0?"Yes":"No"

                );
            	books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
}
