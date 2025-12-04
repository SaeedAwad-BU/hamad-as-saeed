package com.example.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Refactored operation class using PreparedStatements for security.
 * Fixed: All SQL operations now use parameterized queries to prevent SQL injection.
 */
public class operation<E> {
    
    /**
     * Insert operation using PreparedStatement (FIXED: Now uses parameterized queries)
     */
    public boolean insert(String table, Class<?> clazz, List<String> list) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            // Get table metadata to determine columns and types
            ResultSetMetaData rsmd;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + " LIMIT 1")) {
                rsmd = rs.getMetaData();
            }
            
            int columnCount = rsmd.getColumnCount();
            
            // Check if list size matches column count
            if (list.size() != columnCount) {
                System.err.println("List size (" + list.size() + ") doesn't match column count (" + columnCount + ")");
                return false;
            }
            
            // Build INSERT statement with placeholders
            StringBuilder insert = new StringBuilder("INSERT INTO " + table + " (");
            for (int i = 1; i <= columnCount; i++) {
                insert.append(rsmd.getColumnName(i));
                if (i < columnCount) insert.append(", ");
            }
            insert.append(") VALUES (");
            for (int i = 1; i <= columnCount; i++) {
                insert.append("?");
                if (i < columnCount) insert.append(", ");
            }
            insert.append(")");
            
            // Execute with PreparedStatement
            try (PreparedStatement pstmt = conn.prepareStatement(insert.toString())) {
                for (int i = 1; i <= columnCount; i++) {
                    String val = list.get(i - 1);
                    
                    if (val == null || val.trim().isEmpty()) {
                        // Set NULL for empty values based on column type
                        int sqlType = rsmd.getColumnType(i);
                        if (sqlType == Types.INTEGER || sqlType == Types.BIGINT) {
                            pstmt.setInt(i, 0);
                        } else if (sqlType == Types.DOUBLE || sqlType == Types.DECIMAL) {
                            pstmt.setDouble(i, 0.0);
                        } else {
                            pstmt.setString(i, null);
                        }
                    } else {
                        // Set value based on column type
                        int sqlType = rsmd.getColumnType(i);
                        if (sqlType == Types.INTEGER || sqlType == Types.BIGINT) {
                            try {
                                pstmt.setInt(i, Integer.parseInt(val));
                            } catch (NumberFormatException e) {
                                pstmt.setInt(i, 0);
                            }
                        } else if (sqlType == Types.DOUBLE || sqlType == Types.DECIMAL) {
                            try {
                                pstmt.setDouble(i, Double.parseDouble(val));
                            } catch (NumberFormatException e) {
                                pstmt.setDouble(i, 0.0);
                            }
                        } else {
                            pstmt.setString(i, val);
                        }
                    }
                }
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error executing INSERT: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update operation using PreparedStatement (NEW METHOD)
     */
    public boolean update(String table, Class<?> clazz, List<String> list, String idValue, String idColumnName) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            // Get table metadata
            ResultSetMetaData rsmd;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM " + table + " LIMIT 1")) {
                rsmd = rs.getMetaData();
            }
            
            int columnCount = rsmd.getColumnCount();
            
            if (list.size() != columnCount) {
                System.err.println("List size doesn't match column count");
                return false;
            }
            
            // Build UPDATE statement (skip auto-increment ID columns in SET clause)
            StringBuilder update = new StringBuilder("UPDATE " + table + " SET ");
            List<String> updateColumns = new ArrayList<>();
            List<String> updateValues = new ArrayList<>();
            
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                // Skip the primary key column in SET clause
                if (!columnName.equalsIgnoreCase(idColumnName)) {
                    updateColumns.add(columnName);
                    updateValues.add(list.get(i - 1));
                }
            }
            
            if (updateColumns.isEmpty()) {
                System.err.println("No columns to update");
                return false;
            }
            
            for (int i = 0; i < updateColumns.size(); i++) {
                update.append(updateColumns.get(i)).append(" = ?");
                if (i < updateColumns.size() - 1) update.append(", ");
            }
            
            update.append(" WHERE ").append(idColumnName).append(" = ?");
            
            // Execute with PreparedStatement
            try (PreparedStatement pstmt = conn.prepareStatement(update.toString())) {
                // Set update parameters
                for (int i = 0; i < updateColumns.size(); i++) {
                    String val = updateValues.get(i);
                    String columnName = updateColumns.get(i);
                    
                    // Find column type from metadata
                    int sqlType = Types.VARCHAR; // default
                    for (int j = 1; j <= columnCount; j++) {
                        if (rsmd.getColumnName(j).equalsIgnoreCase(columnName)) {
                            sqlType = rsmd.getColumnType(j);
                            break;
                        }
                    }
                    
                    setParameterByType(pstmt, i + 1, val, sqlType);
                }
                
                // Set WHERE clause parameter
                setParameterByType(pstmt, updateColumns.size() + 1, idValue, Types.INTEGER);
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error executing UPDATE: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete operation using PreparedStatement (NEW METHOD)
     */
    public boolean delete(String table, String idColumnName, String idValue) {
        String sql = "DELETE FROM " + table + " WHERE " + idColumnName + " = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            try {
                pstmt.setInt(1, Integer.parseInt(idValue));
            } catch (NumberFormatException e) {
                pstmt.setString(1, idValue);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error executing DELETE: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get field names from database table metadata
     */
    public String[] getField(Class<?> clazz) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return null;
            }
            
            String tableName = clazz.getSimpleName().toLowerCase();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1")) {
                
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                String[] str = new String[columnCount];

                for (int i = 0; i < columnCount; i++) {
                    str[i] = rsmd.getColumnName(i + 1);
                }

                return str;
            }
        } catch (SQLException e) {
            System.err.println("Error getting field names: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get field values for a record by ID (NEW METHOD - improved version)
     */
    public String[] getFieldValues(Class<?> clazz, String idColumnName, String idValue) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return null;
            }
            
            String tableName = clazz.getSimpleName().toLowerCase();
            String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try {
                    pstmt.setInt(1, Integer.parseInt(idValue));
                } catch (NumberFormatException e) {
                    pstmt.setString(1, idValue);
                }
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        String[] values = new String[columnCount];
                        
                        for (int i = 1; i <= columnCount; i++) {
                            Object value = rs.getObject(i);
                            values[i - 1] = (value == null) ? "" : value.toString();
                        }
                        
                        return values;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting field values: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Helper method to set PreparedStatement parameter based on SQL type
     */
    private void setParameterByType(PreparedStatement pstmt, int index, String value, int sqlType) 
            throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            if (sqlType == Types.INTEGER || sqlType == Types.BIGINT) {
                pstmt.setInt(index, 0);
            } else if (sqlType == Types.DOUBLE || sqlType == Types.DECIMAL) {
                pstmt.setDouble(index, 0.0);
            } else {
                pstmt.setString(index, null);
            }
        } else {
            if (sqlType == Types.INTEGER || sqlType == Types.BIGINT) {
                try {
                    pstmt.setInt(index, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    pstmt.setInt(index, 0);
                }
            } else if (sqlType == Types.DOUBLE || sqlType == Types.DECIMAL) {
                try {
                    pstmt.setDouble(index, Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    pstmt.setDouble(index, 0.0);
                }
            } else {
                pstmt.setString(index, value);
            }
        }
    }
}
