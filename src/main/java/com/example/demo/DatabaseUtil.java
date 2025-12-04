package com.example.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Shared database utility class for CRUD operations using PreparedStatements.
 * This class provides safe, parameterized SQL operations to prevent SQL injection
 * and ensure proper error handling.
 */
public class DatabaseUtil {
    
    /**
     * Execute an INSERT operation using PreparedStatement
     * @param tableName The name of the table
     * @param columns Array of column names (excluding auto-increment ID columns)
     * @param values Array of values matching the columns (same order)
     * @return true if successful, false otherwise
     */
    public static boolean insert(String tableName, String[] columns, Object[] values) {
        if (columns == null || values == null || columns.length != values.length) {
            System.err.println("Columns and values arrays must have the same length");
            return false;
        }
        
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName).append(" (");
        
        // Build column list
        for (int i = 0; i < columns.length; i++) {
            sql.append(columns[i]);
            if (i < columns.length - 1) sql.append(", ");
        }
        
        sql.append(") VALUES (");
        for (int i = 0; i < columns.length; i++) {
            sql.append("?");
            if (i < columns.length - 1) sql.append(", ");
        }
        sql.append(")");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            // Set parameters
            for (int i = 0; i < values.length; i++) {
                setParameter(pstmt, i + 1, values[i]);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error executing INSERT: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Execute an UPDATE operation using PreparedStatement
     * @param tableName The name of the table
     * @param idColumnName The name of the primary key column
     * @param idValue The value of the primary key
     * @param columns Array of column names to update
     * @param values Array of values matching the columns (same order)
     * @return true if successful, false otherwise
     */
    public static boolean update(String tableName, String idColumnName, Object idValue, 
                                 String[] columns, Object[] values) {
        if (columns == null || values == null || columns.length != values.length) {
            System.err.println("Columns and values arrays must have the same length");
            return false;
        }
        
        if (columns.length == 0) {
            System.err.println("No columns to update");
            return false;
        }
        
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");
        
        // Build SET clause
        for (int i = 0; i < columns.length; i++) {
            sql.append(columns[i]).append(" = ?");
            if (i < columns.length - 1) sql.append(", ");
        }
        
        sql.append(" WHERE ").append(idColumnName).append(" = ?");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            // Set update parameters
            for (int i = 0; i < values.length; i++) {
                setParameter(pstmt, i + 1, values[i]);
            }
            
            // Set WHERE clause parameter
            setParameter(pstmt, values.length + 1, idValue);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error executing UPDATE: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Execute a DELETE operation using PreparedStatement
     * @param tableName The name of the table
     * @param idColumnName The name of the primary key column
     * @param idValue The value of the primary key to delete
     * @return true if successful, false otherwise
     */
    public static boolean delete(String tableName, String idColumnName, Object idValue) {
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumnName + " = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return false;
            }
            
            setParameter(pstmt, 1, idValue);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error executing DELETE: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get a single record by ID
     * @param tableName The name of the table
     * @param idColumnName The name of the primary key column
     * @param idValue The value of the primary key
     * @return ResultSet containing the record, or null if not found
     */
    public static ResultSet getById(String tableName, String idColumnName, Object idValue) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = ?";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return null;
            }
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            setParameter(pstmt, 1, idValue);
            
            return pstmt.executeQuery();
            
        } catch (SQLException e) {
            System.err.println("Error executing SELECT: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get column names for a table
     * @param tableName The name of the table
     * @return Array of column names, or null if error
     */
    public static String[] getColumnNames(String tableName) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1")) {
            
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return null;
            }
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] columns = new String[columnCount];
            
            for (int i = 0; i < columnCount; i++) {
                columns[i] = rsmd.getColumnName(i + 1);
            }
            
            return columns;
            
        } catch (SQLException e) {
            System.err.println("Error getting column names: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Set a parameter in a PreparedStatement based on its type
     * @param pstmt The PreparedStatement
     * @param index The parameter index (1-based)
     * @param value The value to set
     * @throws SQLException If setting the parameter fails
     */
    private static void setParameter(PreparedStatement pstmt, int index, Object value) 
            throws SQLException {
        if (value == null) {
            pstmt.setNull(index, Types.NULL);
        } else if (value instanceof Integer) {
            pstmt.setInt(index, (Integer) value);
        } else if (value instanceof Double) {
            pstmt.setDouble(index, (Double) value);
        } else if (value instanceof Float) {
            pstmt.setFloat(index, (Float) value);
        } else if (value instanceof Long) {
            pstmt.setLong(index, (Long) value);
        } else if (value instanceof Boolean) {
            pstmt.setBoolean(index, (Boolean) value);
        } else if (value instanceof String) {
            pstmt.setString(index, (String) value);
        } else if (value instanceof Date) {
            pstmt.setDate(index, (Date) value);
        } else if (value instanceof Timestamp) {
            pstmt.setTimestamp(index, (Timestamp) value);
        } else {
            // Default to string
            pstmt.setString(index, value.toString());
        }
    }
    
    /**
     * Get ID from a name value for foreign key relationships
     * @param tableName The table to query
     * @param nameColumn The column containing the name
     * @param idColumn The column containing the ID
     * @param nameValue The name value to search for
     * @return The ID value, or -1 if not found
     */
    public static int getIdFromName(String tableName, String nameColumn, String idColumn, String nameValue) {
        String sql = "SELECT " + idColumn + " FROM " + tableName + " WHERE " + nameColumn + " = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return -1;
            }
            
            pstmt.setString(1, nameValue);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting ID from name: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
}

