package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Reports
 * Contains all SQL queries for generating statistical reports
 */
public class ReportsDAO {
    
    /**
     * Report 1: Total number of books
     */
    public static int getTotalBooks() {
        String query = "SELECT COUNT(*) as total FROM book";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total books: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Report 2: Sum of all book prices (total value of library)
     */
    public static double getTotalLibraryValue() {
        String query = "SELECT SUM(original_price) as total_value FROM book";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total_value");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total library value: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    /**
     * Report 3: Count of books per category
     */
    public static List<Map<String, Object>> getBooksPerCategory() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT category, COUNT(*) as count FROM book GROUP BY category ORDER BY count DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("category", rs.getString("category"));
                row.put("count", rs.getInt("count"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting books per category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Report 4: List of all books with their category names
     */
    public static List<Map<String, Object>> getAllBooksWithCategories() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT book_id, title, category, book_type, original_price, " +
                      "CASE WHEN available > 0 THEN 'Yes' ELSE 'No' END as available_status " +
                      "FROM book ORDER BY title";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("book_id", rs.getInt("book_id"));
                row.put("title", rs.getString("title"));
                row.put("category", rs.getString("category"));
                row.put("book_type", rs.getString("book_type"));
                row.put("original_price", rs.getDouble("original_price"));
                row.put("available_status", rs.getString("available_status"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all books with categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Report 5: Count of borrowed books currently out
     */
    public static int getCurrentlyBorrowedBooksCount() {
        String query = "SELECT COUNT(*) as count FROM loan WHERE return_date IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting currently borrowed books count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Report 6: List of overdue books (due_date < today and return_date IS NULL)
     */
    public static List<Map<String, Object>> getOverdueBooks() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT l.loan_id, " +
                      "CONCAT(b.first_name, ' ', b.last_name) as borrower_name, " +
                      "bo.title as book_title, " +
                      "l.loan_date, l.due_date, " +
                      "DATEDIFF(CURDATE(), l.due_date) as days_overdue " +
                      "FROM loan l " +
                      "LEFT JOIN borrower b ON l.borrower_id = b.borrower_id " +
                      "LEFT JOIN book bo ON l.book_id = bo.book_id " +
                      "WHERE l.return_date IS NULL AND l.due_date < CURDATE() " +
                      "ORDER BY days_overdue DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("loan_id", rs.getInt("loan_id"));
                row.put("borrower_name", rs.getString("borrower_name"));
                row.put("book_title", rs.getString("book_title"));
                row.put("loan_date", rs.getString("loan_date"));
                row.put("due_date", rs.getString("due_date"));
                row.put("days_overdue", rs.getInt("days_overdue"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting overdue books: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Report 7: Count of books per author
     */
    public static List<Map<String, Object>> getBooksPerAuthor() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT CONCAT(a.first_name, ' ', a.last_name) as author_name, " +
                      "COUNT(ba.book_id) as book_count " +
                      "FROM author a " +
                      "LEFT JOIN book_author ba ON a.author_id = ba.author_id " +
                      "GROUP BY a.author_id, a.first_name, a.last_name " +
                      "ORDER BY book_count DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("author_name", rs.getString("author_name"));
                row.put("book_count", rs.getInt("book_count"));
                results.add(row);
            }
        } catch (SQLException e) {
            // If book_author table doesn't exist, try alternative query
            // This handles cases where author info might be stored differently
            System.err.println("Error with book_author join, trying alternative query: " + e.getMessage());
            return getBooksPerAuthorAlternative();
        }
        
        return results;
    }
    
    /**
     * Alternative query for books per author if book_author junction table doesn't exist
     */
    private static List<Map<String, Object>> getBooksPerAuthorAlternative() {
        List<Map<String, Object>> results = new ArrayList<>();
        // If there's a direct author_id in book table
        String query = "SELECT CONCAT(a.first_name, ' ', a.last_name) as author_name, " +
                      "COUNT(b.book_id) as book_count " +
                      "FROM author a " +
                      "LEFT JOIN book b ON a.author_id = b.author_id " +
                      "GROUP BY a.author_id, a.first_name, a.last_name " +
                      "ORDER BY book_count DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("author_name", rs.getString("author_name"));
                row.put("book_count", rs.getInt("book_count"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting books per author (alternative): " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Report 8: Count of books per publisher
     */
    public static List<Map<String, Object>> getBooksPerPublisher() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT p.name as publisher_name, COUNT(b.book_id) as book_count " +
                      "FROM publisher p " +
                      "LEFT JOIN book b ON p.publisher_id = b.publisher_id " +
                      "GROUP BY p.publisher_id, p.name " +
                      "ORDER BY book_count DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("publisher_name", rs.getString("publisher_name"));
                row.put("book_count", rs.getInt("book_count"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting books per publisher: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Report 9: List of borrowers with number of books borrowed
     */
    public static List<Map<String, Object>> getBorrowersWithBookCount() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT b.borrower_id, " +
                      "CONCAT(b.first_name, ' ', b.last_name) as borrower_name, " +
                      "COUNT(l.loan_id) as total_loans " +
                      "FROM borrower b " +
                      "LEFT JOIN loan l ON b.borrower_id = l.borrower_id " +
                      "GROUP BY b.borrower_id, b.first_name, b.last_name " +
                      "ORDER BY total_loans DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("borrower_id", rs.getInt("borrower_id"));
                row.put("borrower_name", rs.getString("borrower_name"));
                row.put("total_loans", rs.getInt("total_loans"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting borrowers with book count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Report 10: Daily borrowing statistics (group by date)
     */
    public static List<Map<String, Object>> getDailyBorrowingStatistics() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT DATE(loan_date) as loan_day, COUNT(*) as loan_count " +
                      "FROM loan " +
                      "GROUP BY DATE(loan_date) " +
                      "ORDER BY loan_day DESC " +
                      "LIMIT 30";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("loan_day", rs.getString("loan_day"));
                row.put("loan_count", rs.getInt("loan_count"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting daily borrowing statistics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Report 11 (Bonus): Monthly borrowing statistics
     */
    public static List<Map<String, Object>> getMonthlyBorrowingStatistics() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT DATE_FORMAT(loan_date, '%Y-%m') as loan_month, COUNT(*) as loan_count " +
                      "FROM loan " +
                      "GROUP BY DATE_FORMAT(loan_date, '%Y-%m') " +
                      "ORDER BY loan_month DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("loan_month", rs.getString("loan_month"));
                row.put("loan_count", rs.getInt("loan_count"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting monthly borrowing statistics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Report 12 (Bonus): Most borrowed book
     */
    public static List<Map<String, Object>> getMostBorrowedBook() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT b.book_id, b.title, COUNT(l.loan_id) as borrow_count " +
                      "FROM book b " +
                      "LEFT JOIN loan l ON b.book_id = l.book_id " +
                      "GROUP BY b.book_id, b.title " +
                      "ORDER BY borrow_count DESC " +
                      "LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("book_id", rs.getInt("book_id"));
                row.put("title", rs.getString("title"));
                row.put("borrow_count", rs.getInt("borrow_count"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting most borrowed book: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Report 13 (Bonus): Most active borrower
     */
    public static List<Map<String, Object>> getMostActiveBorrower() {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT b.borrower_id, " +
                      "CONCAT(b.first_name, ' ', b.last_name) as borrower_name, " +
                      "COUNT(l.loan_id) as total_loans " +
                      "FROM borrower b " +
                      "LEFT JOIN loan l ON b.borrower_id = l.borrower_id " +
                      "GROUP BY b.borrower_id, b.first_name, b.last_name " +
                      "ORDER BY total_loans DESC " +
                      "LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("borrower_id", rs.getInt("borrower_id"));
                row.put("borrower_name", rs.getString("borrower_name"));
                row.put("total_loans", rs.getInt("total_loans"));
                results.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Error getting most active borrower: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
}

