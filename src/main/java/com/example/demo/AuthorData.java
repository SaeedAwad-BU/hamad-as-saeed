package com.example.demo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuthorData {
    public static ObservableList<Author> getAllAuthors() {
    	ObservableList<Author> authors = FXCollections.observableArrayList();
        String query = "SELECT author_id,CONCAT(first_name,' ',last_name) AS full_name,country,bio FROM author";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Author author = new Author(
                        rs.getInt("author_id"),
                        rs.getString("full_name"),
                        rs.getString("country"),
                        rs.getString("bio"));
                authors.add(author);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }
    
    /**
     * Search authors using a column and search value with LIKE pattern.
     * Uses PreparedStatement to prevent SQL injection.
     * 
     * @param columnName The column to search in (e.g., "full_name", "country")
     * @param searchValue The value to search for (uses LIKE '%value%')
     * @return ObservableList of matching authors
     */
    public static ObservableList<Author> searchAuthors(String columnName, String searchValue) {
        ObservableList<Author> authors = FXCollections.observableArrayList();
        
        String query;
        if (searchValue == null || searchValue.trim().isEmpty()) {
            query = "SELECT author_id,CONCAT(first_name,' ',last_name) AS full_name,country,bio FROM author";
        } else {
            // Map column names to database columns
            String dbColumn = mapColumnName(columnName);
            query = "SELECT author_id,CONCAT(first_name,' ',last_name) AS full_name,country,bio FROM author WHERE " + dbColumn + " LIKE ?";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                pstmt.setString(1, "%" + searchValue + "%");
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Author author = new Author(
                            rs.getInt("author_id"),
                            rs.getString("full_name"),
                            rs.getString("country"),
                            rs.getString("bio"));
                    authors.add(author);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching authors: " + e.getMessage());
            e.printStackTrace();
        }
        
        return authors;
    }
    
    /**
     * Map UI column names to database column names for search
     */
    private static String mapColumnName(String columnName) {
        switch (columnName.toLowerCase()) {
            case "full_name":
                return "CONCAT(first_name, ' ', last_name)";
            default:
                return columnName;
        }
    }
}

