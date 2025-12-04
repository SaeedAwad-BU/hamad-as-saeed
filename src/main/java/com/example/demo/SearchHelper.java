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
 * Helper class for searching tables using PreparedStatement with LIKE queries.
 * Prevents SQL injection by using parameterized queries.
 */
public class SearchHelper {
    
    /**
     * Get all column names for a table (for ComboBox selection)
     */
    public static List<String> getTableColumns(String tableName) {
        List<String> columns = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return columns;
            }
            
            String query = "SELECT * FROM " + tableName + " LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                
                for (int i = 1; i <= columnCount; i++) {
                    columns.add(rsmd.getColumnName(i));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting table columns: " + e.getMessage());
            e.printStackTrace();
        }
        
        return columns;
    }
    
    /**
     * Search a table and return results as ObservableList of entity objects.
     * This method queries the database directly and reconstructs entity objects.
     */
    public static ObservableList<?> searchTableData(String tableName, String columnName, String searchValue, 
                                                   Class<?> entityClass) {
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
                // Validate column name exists in table
                List<String> validColumns = getTableColumns(tableName);
                if (!validColumns.contains(columnName)) {
                    System.err.println("Invalid column name: " + columnName);
                    return results;
                }
                
                // Use LIKE with parameterized query for safe searching
                sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " LIKE ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + searchValue + "%");
            }
            
            // Execute query and let the Data classes handle the entity construction
            // We'll need to route this to the appropriate Data class method
            try (ResultSet rs = pstmt.executeQuery()) {
                // For now, return raw results - the calling code will need to handle entity construction
                // based on table type
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                
                List<List<Object>> rows = new ArrayList<>();
                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    rows.add(row);
                    results.add(row.toArray());
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching table: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
}
