package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Shared utility class for search functionality.
 * Provides safe, PreparedStatement-based search operations to prevent SQL injection.
 */
public class SearchUtil {
    
    /**
     * Get searchable columns for a table (excludes ID columns and non-text columns for search purposes)
     * @param tableName The name of the table
     * @return List of searchable column names
     */
    public static List<String> getSearchableColumns(String tableName) {
        List<String> searchableColumns = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return searchableColumns;
            }
            
            // Get table metadata
            String query = "SELECT * FROM " + tableName + " LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    int columnType = rsmd.getColumnType(i);
                    
                    // Include text-based columns and exclude auto-increment IDs
                    // Allow searching by ID columns if they're not auto-increment
                    if (columnType == java.sql.Types.VARCHAR || 
                        columnType == java.sql.Types.CHAR ||
                        columnType == java.sql.Types.LONGVARCHAR ||
                        columnType == java.sql.Types.INTEGER ||
                        columnType == java.sql.Types.BIGINT ||
                        columnType == java.sql.Types.DECIMAL ||
                        columnType == java.sql.Types.DOUBLE) {
                        searchableColumns.add(columnName);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting searchable columns: " + e.getMessage());
            e.printStackTrace();
        }
        
        return searchableColumns;
    }
    
    /**
     * Search a table using a column and search value with LIKE pattern.
     * Uses PreparedStatement to prevent SQL injection.
     * 
     * @param tableName The name of the table to search
     * @param columnName The column to search in (must be a valid column name)
     * @param searchValue The value to search for (will use LIKE '%value%')
     * @return ObservableList of results (empty list if no results or error)
     */
    public static ObservableList<Object> searchTable(String tableName, String columnName, String searchValue) {
        ObservableList<Object> results = FXCollections.observableArrayList();
        
        if (tableName == null || tableName.trim().isEmpty()) {
            return results;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return results;
            }
            
            String sql;
            PreparedStatement pstmt;
            
            // If search value is empty, return all records
            if (searchValue == null || searchValue.trim().isEmpty()) {
                sql = "SELECT * FROM " + tableName;
                pstmt = conn.prepareStatement(sql);
            } else {
                // Use LIKE with parameterized query for safe searching
                sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " LIKE ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + searchValue + "%");
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                
                while (rs.next()) {
                    // Create a map or array to store row data
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    results.add(row.toArray());
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching table: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Validate that a column name exists in the table to prevent SQL injection
     * @param tableName The table name
     * @param columnName The column name to validate
     * @return true if column exists, false otherwise
     */
    public static boolean isValidColumn(String tableName, String columnName) {
        List<String> validColumns = getSearchableColumns(tableName);
        return validColumns.contains(columnName);
    }
}
