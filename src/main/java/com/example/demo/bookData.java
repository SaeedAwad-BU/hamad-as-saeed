package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    
    /**
     * Search books using a column and search value with LIKE pattern.
     * Uses PreparedStatement to prevent SQL injection.
     * 
     * @param columnName The column to search in (e.g., "title", "category")
     * @param searchValue The value to search for (uses LIKE '%value%')
     * @return ObservableList of matching books
     */
    public static ObservableList<book> searchBooks(String columnName, String searchValue) {
        ObservableList<book> books = FXCollections.observableArrayList();
        
        // Map UI column names to database column names
        String dbColumnName = mapColumnName(columnName);
        
        String query;
        if (searchValue == null || searchValue.trim().isEmpty()) {
            // Return all books if search value is empty
            query = "SELECT book_id,title,name,category,book_type,original_price,available FROM book LEFT JOIN publisher ON book.publisher_id = publisher.publisher_id";
        } else {
            // Use LIKE with parameterized query
            query = "SELECT book_id,title,name,category,book_type,original_price,available FROM book LEFT JOIN publisher ON book.publisher_id = publisher.publisher_id WHERE " + dbColumnName + " LIKE ?";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                pstmt.setString(1, "%" + searchValue + "%");
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    book book = new book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getString("book_type"),
                            rs.getDouble("original_price"),
                            rs.getInt("available") > 0 ? "Yes" : "No"
                    );
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
            e.printStackTrace();
        }
        
        return books;
    }
    
    /**
     * Map UI column names to database column names for search
     */
    private static String mapColumnName(String columnName) {
        // Handle column name mapping - some columns might have different names in UI vs DB
        switch (columnName.toLowerCase()) {
            case "publisher_name":
            case "name":
                return "publisher.name";
            default:
                return "book." + columnName;
        }
    }
}
