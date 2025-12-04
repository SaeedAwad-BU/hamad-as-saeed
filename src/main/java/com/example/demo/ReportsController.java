package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Controller for the Reports view
 * Handles all report generation and display
 */
public class ReportsController {
    
    @FXML
    private Label resultLabel;
    
    @FXML
    private TableView<Map<String, Object>> resultsTable;
    
    // Report buttons (referenced for potential styling updates)
    @FXML
    private javafx.scene.control.Button btnTotalBooks;
    @FXML
    private javafx.scene.control.Button btnTotalValue;
    @FXML
    private javafx.scene.control.Button btnBooksPerCategory;
    @FXML
    private javafx.scene.control.Button btnAllBooks;
    @FXML
    private javafx.scene.control.Button btnBorrowedCount;
    @FXML
    private javafx.scene.control.Button btnOverdueBooks;
    @FXML
    private javafx.scene.control.Button btnBooksPerAuthor;
    @FXML
    private javafx.scene.control.Button btnBooksPerPublisher;
    @FXML
    private javafx.scene.control.Button btnBorrowersWithCount;
    @FXML
    private javafx.scene.control.Button btnDailyStats;
    @FXML
    private javafx.scene.control.Button btnMonthlyStats;
    @FXML
    private javafx.scene.control.Button btnMostBorrowed;
    @FXML
    private javafx.scene.control.Button btnMostActive;
    
    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        // Set initial state
        resultLabel.setVisible(true);
        resultLabel.setManaged(true);
        resultsTable.setVisible(false);
        resultsTable.setManaged(false);
    }
    
    /**
     * Report 1: Total number of books
     */
    @FXML
    private void generateTotalBooks() {
        int total = ReportsDAO.getTotalBooks();
        displaySingleValue("Total Number of Books", total + " books");
    }
    
    /**
     * Report 2: Total library value
     */
    @FXML
    private void generateTotalValue() {
        double total = ReportsDAO.getTotalLibraryValue();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        displaySingleValue("Total Library Value", "$" + df.format(total));
    }
    
    /**
     * Report 3: Books per category
     */
    @FXML
    private void generateBooksPerCategory() {
        List<Map<String, Object>> results = ReportsDAO.getBooksPerCategory();
        displayTableResults(results, "Books Per Category");
    }
    
    /**
     * Report 4: All books with categories
     */
    @FXML
    private void generateAllBooks() {
        List<Map<String, Object>> results = ReportsDAO.getAllBooksWithCategories();
        displayTableResults(results, "All Books with Categories");
    }
    
    /**
     * Report 5: Currently borrowed books count
     */
    @FXML
    private void generateBorrowedCount() {
        int count = ReportsDAO.getCurrentlyBorrowedBooksCount();
        displaySingleValue("Currently Borrowed Books", count + " books currently out on loan");
    }
    
    /**
     * Report 6: Overdue books
     */
    @FXML
    private void generateOverdueBooks() {
        List<Map<String, Object>> results = ReportsDAO.getOverdueBooks();
        displayTableResults(results, "Overdue Books");
    }
    
    /**
     * Report 7: Books per author
     */
    @FXML
    private void generateBooksPerAuthor() {
        List<Map<String, Object>> results = ReportsDAO.getBooksPerAuthor();
        displayTableResults(results, "Books Per Author");
    }
    
    /**
     * Report 8: Books per publisher
     */
    @FXML
    private void generateBooksPerPublisher() {
        List<Map<String, Object>> results = ReportsDAO.getBooksPerPublisher();
        displayTableResults(results, "Books Per Publisher");
    }
    
    /**
     * Report 9: Borrowers with loan count
     */
    @FXML
    private void generateBorrowersWithCount() {
        List<Map<String, Object>> results = ReportsDAO.getBorrowersWithBookCount();
        displayTableResults(results, "Borrowers with Loan Count");
    }
    
    /**
     * Report 10: Daily borrowing statistics
     */
    @FXML
    private void generateDailyStats() {
        List<Map<String, Object>> results = ReportsDAO.getDailyBorrowingStatistics();
        displayTableResults(results, "Daily Borrowing Statistics");
    }
    
    /**
     * Report 11: Monthly borrowing statistics
     */
    @FXML
    private void generateMonthlyStats() {
        List<Map<String, Object>> results = ReportsDAO.getMonthlyBorrowingStatistics();
        displayTableResults(results, "Monthly Borrowing Statistics");
    }
    
    /**
     * Report 12: Most borrowed books
     */
    @FXML
    private void generateMostBorrowed() {
        List<Map<String, Object>> results = ReportsDAO.getMostBorrowedBook();
        displayTableResults(results, "Most Borrowed Books");
    }
    
    /**
     * Report 13: Most active borrowers
     */
    @FXML
    private void generateMostActive() {
        List<Map<String, Object>> results = ReportsDAO.getMostActiveBorrower();
        displayTableResults(results, "Most Active Borrowers");
    }
    
    /**
     * Display a single numeric result in a Label
     */
    private void displaySingleValue(String title, String value) {
        resultLabel.setText(title + ":\n\n" + value);
        resultLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-alignment: center;");
        resultLabel.setVisible(true);
        resultLabel.setManaged(true);
        
        resultsTable.setVisible(false);
        resultsTable.setManaged(false);
    }
    
    /**
     * Display tabular results in a TableView
     */
    private void displayTableResults(List<Map<String, Object>> results, String title) {
        if (results == null || results.isEmpty()) {
            resultLabel.setText(title + ":\n\nNo data found.");
            resultLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d; -fx-alignment: center;");
            resultLabel.setVisible(true);
            resultLabel.setManaged(true);
            resultsTable.setVisible(false);
            resultsTable.setManaged(false);
            return;
        }
        
        // Hide label, show table
        resultLabel.setVisible(false);
        resultLabel.setManaged(false);
        resultsTable.setVisible(true);
        resultsTable.setManaged(true);
        
        // Clear existing columns
        resultsTable.getColumns().clear();
        resultsTable.getItems().clear();
        
        // Get column names from first row
        Map<String, Object> firstRow = results.get(0);
        String[] columnNames = firstRow.keySet().toArray(new String[0]);
        
        // Create columns dynamically
        for (String columnName : columnNames) {
            TableColumn<Map<String, Object>, Object> column = new TableColumn<>(formatColumnName(columnName));
            column.setCellValueFactory(data -> {
                Object value = data.getValue().get(columnName);
                if (value == null) {
                    return new javafx.beans.property.SimpleObjectProperty<>("");
                }
                // Format numbers appropriately
                if (value instanceof Double) {
                    DecimalFormat df = new DecimalFormat("#,##0.00");
                    return new javafx.beans.property.SimpleObjectProperty<>(df.format((Double) value));
                } else if (value instanceof Integer) {
                    return new javafx.beans.property.SimpleObjectProperty<>(value.toString());
                }
                return new javafx.beans.property.SimpleObjectProperty<>(value.toString());
            });
            column.setPrefWidth(200);
            resultsTable.getColumns().add(column);
        }
        
        // Populate table with data
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList(results);
        resultsTable.setItems(data);
        
        // Set table style
        resultsTable.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");
    }
    
    /**
     * Format column name for display (convert snake_case to Title Case)
     */
    private String formatColumnName(String columnName) {
        String[] parts = columnName.split("_");
        StringBuilder formatted = new StringBuilder();
        for (String part : parts) {
            if (formatted.length() > 0) {
                formatted.append(" ");
            }
            formatted.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1).toLowerCase());
        }
        return formatted.toString();
    }
}

