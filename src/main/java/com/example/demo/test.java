package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class test extends Application {
    ResultSetMetaData rs;

    private BorderPane mainLayout;
    private VBox contentArea;
    private String currentEntity = "Book";
    private TableView<Object> dataTable;
    private static ObservableList<Author> AuthorList;
	private static ObservableList<user> UserList;
	private static ObservableList<borrower> borrowerList;
	private static ObservableList<borrowertype> borrowerTypeList;
	private static ObservableList<loan> loanList;
	private static ObservableList<loanperiod> loanPeriodList;
	private static ObservableList<publisher> publisherList;
	private static ObservableList<sale> saleList;

    Map<Control,String> datafield = new LinkedHashMap<>();
    @Override
    public void start(Stage primaryStage) {

        try {
            rs= Objects.requireNonNull(DatabaseConnection.getConnection()).createStatement().executeQuery("SELECT * FROM "+currentEntity).getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea 0%, #764ba2 100%);");
        
        // Create top search area
        HBox searchArea = createSearchArea();
        
        // Create entity buttons area
        VBox entityButtons = createEntityButtons();
        
        // Create action buttons
        HBox actionButtons = createActionButtons();
        
        // Create content area with table
        contentArea = new VBox(20);
        contentArea.setAlignment(Pos.TOP_CENTER);
        contentArea.setPadding(new Insets(20));
        
        // Setup main layout
        mainLayout.setTop(searchArea);
        mainLayout.setLeft(entityButtons);
        mainLayout.setBottom(actionButtons);
        mainLayout.setCenter(contentArea);
        
        // Create scene
        Scene scene = new Scene(mainLayout, 1400, 900);
        
        primaryStage.setTitle("Library Management System - Professional Interface");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Initialize content
        updateContentArea();
    }
    
    // Search components (class-level variables for access)
    private ComboBox<String> searchColumnComboBox;
    private TextField searchField;
    private Button searchButton;
    
    private HBox createSearchArea() {
        HBox searchBox = new HBox(15);
        searchBox.setPadding(new Insets(20));
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);");
        
        // App title
        Label appTitle = new Label("📚 Library Management System");
        appTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Search container
        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER_RIGHT);
        searchContainer.setPadding(new Insets(0, 20, 0, 0));
        
        // Column selection ComboBox
        searchColumnComboBox = new ComboBox<>();
        searchColumnComboBox.setPromptText("Select column to search...");
        searchColumnComboBox.setStyle("-fx-pref-height: 45px; -fx-background-radius: 25px; -fx-border-radius: 25px; -fx-border-color: #e0e0e0; -fx-padding: 0 15px; -fx-font-size: 14px;");
        searchColumnComboBox.setPrefWidth(200);
        
        // Update search columns when entity changes
        updateSearchColumns();
        
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Enter search value...");
        searchField.setStyle("-fx-pref-height: 45px; -fx-background-radius: 25px; -fx-border-radius: 25px; -fx-border-color: #e0e0e0; -fx-padding: 0 20px; -fx-font-size: 14px;");
        searchField.setPrefWidth(300);
        
        // Search button
        searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 25px; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 45px; -fx-pref-width: 120px;");
        searchButton.setOnAction(e -> performSearch());
        
        // Allow Enter key to trigger search
        searchField.setOnAction(e -> performSearch());
        
        searchContainer.getChildren().addAll(searchColumnComboBox, searchField, searchButton);
        searchBox.getChildren().addAll(appTitle, searchContainer);
        
        HBox.setHgrow(searchContainer, Priority.ALWAYS);
        
        return searchBox;
    }
    
    private VBox createEntityButtons() {
        VBox entityBox = new VBox(10);
        entityBox.setPadding(new Insets(30, 20, 30, 20));
        entityBox.setPrefWidth(280);
        entityBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);");
        
        Label sectionTitle = new Label("MANAGEMENT ENTITIES");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #7f8c8d; -fx-padding: 0 0 10px 0;");
        entityBox.getChildren().add(sectionTitle);
        
        String[] entities = {"Book", "Author", "Borrower", "Borrower Type", "Loan", "Loan Period", "Publisher", "Sale"};
        String[] icons = {"📕", "✍️", "👤", "🏷️", "📅", "⏰", "🏢", "💰"};
        
        for (int i = 0; i < entities.length; i++) {
            Button btn = createEntityButton(entities[i], icons[i]);
            final String entity = entities[i];
            btn.setOnAction(e -> {
                currentEntity = entity;
                if(entity.equals("Borrower Type")) currentEntity="Borrowertype";
                else if(entity.equals("Loan Period")) currentEntity="Loanperiod";
                updateContentArea();
                highlightSelectedButton(btn);
                // Update search columns when entity changes
                if (searchColumnComboBox != null) {
                    updateSearchColumns();
                }
                // Update CRUD button visibility when entity changes
                updateCRUDButtonVisibility();
            });
            entityBox.getChildren().add(btn);
        }
        
        // Add separator before Reports
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));
        entityBox.getChildren().add(separator);
        
        // Add Reports button
        Label reportsSectionTitle = new Label("REPORTS & STATISTICS");
        reportsSectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #7f8c8d; -fx-padding: 0 0 10px 0;");
        entityBox.getChildren().add(reportsSectionTitle);
        
        Button reportsBtn = createEntityButton("Reports", "📊");
        reportsBtn.setOnAction(e -> {
            loadReportsView();
            highlightSelectedButton(reportsBtn);
            // Hide CRUD buttons when viewing reports
            if (actionButtonsBox != null) {
                actionButtonsBox.setVisible(false);
            }
        });
        entityBox.getChildren().add(reportsBtn);
        
        return entityBox;
    }
    
    private Button createEntityButton(String text, String icon) {
        Button btn = new Button(text);
        btn.setGraphic(new Label(icon));
        btn.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f9fa); -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 12px; -fx-border-radius: 12px; -fx-border-color: #e0e0e0; -fx-content-display: left; -fx-graphic-text-gap: 15px;");
        btn.setPrefSize(240, 60);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }
    
    // Store action buttons as class variables for role-based visibility control
    private Button insertBtn;
    private Button updateBtn;
    private Button deleteBtn;
    private HBox actionButtonsBox;
    
    private HBox createActionButtons() {
        actionButtonsBox = new HBox(30);
        actionButtonsBox.setPadding(new Insets(20));
        actionButtonsBox.setAlignment(Pos.CENTER);
        actionButtonsBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);");
        
        insertBtn = createActionButton("➕ Insert", "#4CAF50");
        updateBtn = createActionButton("✏️ Update", "#2196F3");
        deleteBtn = createActionButton("🗑️ Delete", "#f44336");
        
        insertBtn.setOnAction(e -> showInsertForm());
        updateBtn.setOnAction(e -> showUpdateForm());
        deleteBtn.setOnAction(e -> showDeleteForm());
        
        // Check user role and show/hide buttons accordingly
        updateCRUDButtonVisibility();
        
        actionButtonsBox.getChildren().addAll(insertBtn, updateBtn, deleteBtn);
        return actionButtonsBox;
    }
    
    /**
     * Update CRUD button visibility based on user role.
     * Only admin users can see Insert, Update, Delete buttons.
     */
    private void updateCRUDButtonVisibility() {
        boolean isAdmin = UserSession.getInstance().isAdmin();
        
        if (isAdmin) {
            insertBtn.setVisible(true);
            updateBtn.setVisible(true);
            deleteBtn.setVisible(true);
            
            // Also check if INSERT is allowed for this table
            if (!isInsertAllowed()) {
                insertBtn.setDisable(true);
                insertBtn.setTooltip(new Tooltip("Insert not allowed for this table"));
            } else {
                insertBtn.setDisable(false);
                insertBtn.setTooltip(null);
            }
        } else {
            // Non-admin users can only view/search, not modify
            insertBtn.setVisible(false);
            updateBtn.setVisible(false);
            deleteBtn.setVisible(false);
        }
    }
    
    /**
     * Check if INSERT is allowed for the current table.
     * Only Book, Author, Publisher, Borrower, Borrowertype tables allow INSERT.
     */
    private boolean isInsertAllowed() {
        String entity = currentEntity.toLowerCase();
        return entity.equals("book") || 
               entity.equals("author") || 
               entity.equals("publisher") || 
               entity.equals("borrower") || 
               entity.equals("borrowertype");
    }
    
    private Button createActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 30px;");
        btn.setPrefSize(180, 60);
        return btn;
    }
    
    private void highlightSelectedButton(Button selectedBtn) {
        // Remove highlight from all entity buttons
        VBox entityBox = (VBox) mainLayout.getLeft();
        for (javafx.scene.Node node : entityBox.getChildren()) {
            if (node instanceof Button) {
                node.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f9fa); -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 12px; -fx-border-radius: 12px; -fx-border-color: #e0e0e0;");
            }
        }
        // Add highlight to selected button
        selectedBtn.setStyle("-fx-background-color: linear-gradient(to bottom, #2196F3, #1976D2); -fx-text-fill: white; -fx-border-color: #1565C0;");
    }
    
    private void updateContentArea() {
        contentArea.getChildren().clear();
        
        // Show CRUD buttons when viewing entities
        if (actionButtonsBox != null) {
            actionButtonsBox.setVisible(true);
        }
        
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));
        
        Label title = new Label(currentEntity + " Management");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label subtitle = new Label("Manage " + currentEntity.toLowerCase() + " records in the library system");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.8);");
        
        headerBox.getChildren().addAll(title, subtitle);
        contentArea.getChildren().add(headerBox);
        
        // Create and add table view
        createTableView();
        contentArea.getChildren().add(dataTable);
        
        VBox.setVgrow(dataTable, Priority.ALWAYS);
        
        // Update search columns when entity changes
        if (searchColumnComboBox != null) {
            updateSearchColumns();
        }
        
        // Update CRUD button visibility when entity changes
        updateCRUDButtonVisibility();
    }
    
    /**
     * Load the Reports FXML view into the content area
     */
    private void loadReportsView() {
        contentArea.getChildren().clear();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/reports-view.fxml"));
            BorderPane reportsView = loader.load();
            
            // Make reports view fill the content area
            contentArea.getChildren().add(reportsView);
            VBox.setVgrow(reportsView, Priority.ALWAYS);
            
        } catch (Exception e) {
            System.err.println("Error loading Reports view: " + e.getMessage());
            e.printStackTrace();
            
            // Show error message
            Label errorLabel = new Label("Error loading Reports view: " + e.getMessage());
            errorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #f44336;");
            contentArea.getChildren().add(errorLabel);
        }
    }
    
    /**
     * Update the search column ComboBox with valid columns for the current table.
     */
    /**
     * Update the search column ComboBox with valid columns for the current table.
     */
    private void updateSearchColumns() {
        if (searchColumnComboBox == null) {
            return;
        }
        
        searchColumnComboBox.getItems().clear();
        
        // Get table name - some entities have different table names
        String tableName = getTableNameForEntity(currentEntity);
        List<String> columns = SearchHelper.getTableColumns(tableName);
        
        // For tables with JOINs, we need to get columns from the view/result structure
        // Get columns from the actual table structure displayed
        List<String> displayColumns = getDisplayColumnsForEntity();
        if (!displayColumns.isEmpty()) {
            searchColumnComboBox.getItems().addAll(displayColumns);
        } else {
            searchColumnComboBox.getItems().addAll(columns);
        }
        
        // Set prompt text
        if (searchColumnComboBox.getItems().isEmpty()) {
            searchColumnComboBox.setPromptText("No columns available");
        } else {
            searchColumnComboBox.setPromptText("Select column to search...");
        }
    }
    
    /**
     * Get the database table name for an entity (handles special cases)
     */
    private String getTableNameForEntity(String entity) {
        String entityLower = entity.toLowerCase();
        switch (entityLower) {
            case "borrowertype":
                return "borrowertype";
            case "loanperiod":
                return "loanperiod";
            default:
                return entityLower;
        }
    }
    
    /**
     * Get display column names for the current entity (as shown in TableView)
     */
    private List<String> getDisplayColumnsForEntity() {
        List<String> columns = new ArrayList<>();
        
        switch (currentEntity) {
            case "Book":
                columns.addAll(Arrays.asList("book_id", "title", "name", "category", "book_type", "original_price", "available"));
                break;
            case "Author":
                columns.addAll(Arrays.asList("author_id", "full_name", "country", "bio"));
                break;
            case "Borrower":
                columns.addAll(Arrays.asList("borrower_id", "full_name", "email", "phone", "borrowertype_name"));
                break;
            case "Borrowertype":
                columns.addAll(Arrays.asList("type_id", "type_name", "max_books", "loan_period_days"));
                break;
            case "Loan":
                columns.addAll(Arrays.asList("loan_id", "borrower_name", "title", "period_name", "loan_date", "due_date", "return_date"));
                break;
            case "Loanperiod":
                columns.addAll(Arrays.asList("period_id", "period_name", "days"));
                break;
            case "Publisher":
                columns.addAll(Arrays.asList("publisher_id", "name", "address", "phone"));
                break;
            case "Sale":
                columns.addAll(Arrays.asList("sale_id", "book_title", "sale_date", "price", "quantity"));
                break;
            default:
                // Fallback to database columns
                break;
        }
        
        return columns;
    }
    
    /**
     * Perform search operation using PreparedStatement with LIKE query.
     * Updates the TableView immediately with search results.
     */
    private void performSearch() {
        if (searchColumnComboBox == null || searchField == null) {
            return;
        }
        
        String selectedColumn = searchColumnComboBox.getValue();
        String searchValue = searchField.getText();
        
        // If no column selected, show all data
        if (selectedColumn == null || selectedColumn.trim().isEmpty()) {
            updateContentArea();
            return;
        }
        
        // Perform search using PreparedStatement (safe from SQL injection)
        searchTableData(selectedColumn, searchValue);
    }
    
    /**
     * Search table data using PreparedStatement with LIKE pattern.
     * This method queries the database and updates the TableView.
     */
    private void searchTableData(String columnName, String searchValue) {
        String tableName = currentEntity.toLowerCase();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return;
            }
            
            String sql;
            PreparedStatement pstmt;
            
            // If search value is empty, return all records
            if (searchValue == null || searchValue.trim().isEmpty()) {
                sql = "SELECT * FROM " + tableName;
                pstmt = conn.prepareStatement(sql);
            } else {
                // Validate column name to prevent SQL injection
                List<String> validColumns = SearchHelper.getTableColumns(tableName);
                if (!validColumns.contains(columnName)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Column");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid column selected for search.");
                    alert.showAndWait();
                    return;
                }
                
                // Use LIKE with parameterized query for safe searching
                sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " LIKE ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + searchValue + "%");
            }
            
            // Execute search and refresh table
            try (ResultSet rs = pstmt.executeQuery()) {
                // Refresh the table with search results by reloading data
                refreshTableViewWithSearch(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error performing search: " + e.getMessage());
            e.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred during search: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    /**
     * Refresh TableView with search results.
     * Uses Data class search methods to get filtered results.
     */
    private void refreshTableViewWithSearch(ResultSet rs) {
        // Get search parameters
        String selectedColumn = searchColumnComboBox != null ? searchColumnComboBox.getValue() : null;
        String searchValue = searchField != null ? searchField.getText() : null;
        
        // Reload table with search results using Data class methods
        loadTableDataWithSearch(selectedColumn, searchValue);
    }
    
    /**
     * Load table data with search filter using Data class methods.
     */
    private void loadTableDataWithSearch(String columnName, String searchValue) {
        // Use search methods from Data classes if available
        if (columnName != null && searchValue != null) {
            switch (currentEntity) {
                case "Book":
                    ObservableList<book> bookResults = bookData.searchBooks(columnName, searchValue);
                    dataTable = new getTables().gettable(book.class, bookResults);
                    replaceTableInContentArea();
                    return;
                case "Author":
                    ObservableList<Author> authorResults = AuthorData.searchAuthors(columnName, searchValue);
                    dataTable = new getTables().gettable(Author.class, authorResults);
                    replaceTableInContentArea();
                    return;
                default:
                    // For other tables, reload all data
                    // TODO: Add search methods to other Data classes following the same pattern
                    break;
            }
        }
        
        // Fallback: reload all data
        updateContentArea();
    }
    
    /**
     * Replace the table in the content area with updated search results
     */
    private void replaceTableInContentArea() {
        int tableIndex = -1;
        for (int i = 0; i < contentArea.getChildren().size(); i++) {
            if (contentArea.getChildren().get(i) instanceof TableView) {
                tableIndex = i;
                break;
            }
        }
        if (tableIndex >= 0) {
            contentArea.getChildren().set(tableIndex, dataTable);
        } else {
            contentArea.getChildren().add(dataTable);
        }
        VBox.setVgrow(dataTable, Priority.ALWAYS);
    }
    
    private List<String> getdata(String title)  {
        return switch (title) {
            case "book" -> bookData.getAllBooks().stream().map(e -> e.getTitle()).toList();
            case "author" -> AuthorData.getAllAuthors().stream().map(e -> e.getFull_name()).toList();
            case "borrower" -> borrowerData.getAllBorrowers().stream().map(e -> e.getFull_name()).toList();
            case "borrowertype" -> borrowertypeData.getAllBorrowerTypes().stream().map(e -> e.getType_name()).toList();
            case "loan" -> loanData.getAllLoans().stream().map(e -> String.valueOf(e.getLoan_id())).toList();
            case "loanperiod" -> loanperiodData.getAllLoanperiods().stream().map(e -> e.getPeriod_name()).toList();
            case "publisher" -> publisherData.getAllPublishers().stream().map(e -> e.getName()).toList();
            case "Sale" -> saleData.getAllSales().stream().map(e -> String.valueOf(e.getSale_id())).toList();
            default -> null;
        };
    }
    
    /**
     * Get descriptive prompt text for ComboBox based on field type
     */
    private String getPromptTextForComboBox(String fieldName, String entityType) {
        String lowerField = fieldName.toLowerCase();
        
        if (lowerField.contains("publisher")) {
            return "Select publisher";
        } else if (lowerField.contains("author")) {
            return "Select author";
        } else if (lowerField.contains("borrower")) {
            return "Select borrower";
        } else if (lowerField.contains("borrowertype") || lowerField.contains("type")) {
            return "Select borrower type";
        } else if (lowerField.contains("loanperiod") || lowerField.contains("period")) {
            return "Select loan period";
        } else if (lowerField.contains("book")) {
            return "Select book";
        } else if (lowerField.contains("available")) {
            return "Choose availability status";
        } else {
            return "Select " + entityType;
        }
    }
    
    private void createTableView() {
        dataTable = new TableView<>();
        dataTable.setStyle("-fx-background-color: rgba(255, 255, 255, 0.98); -fx-background-radius: 15px;");
        dataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Create columns based on current entity
        switch (currentEntity) {
            case "Book":
            	dataTable = new getTables().gettable(book.class, bookData.getAllBooks());
                break;
            case "Author":
            	dataTable = new getTables().gettable(Author.class, AuthorData.getAllAuthors());
                break;
            case "Borrower":
            	dataTable = new getTables().gettable(borrower.class, borrowerData.getAllBorrowers());
                break;
            case "Borrowertype":
            	dataTable = new getTables().gettable(borrowertype.class, borrowertypeData.getAllBorrowerTypes());
                break;
            case "Loan":
            	dataTable = new getTables().gettable(loan.class, loanData.getAllLoans());
                break;
            case "Loanperiod":
            	dataTable = new getTables().gettable(loanperiod.class, loanperiodData.getAllLoanperiods());
                break;
            case "Publisher":
            	dataTable = new getTables().gettable(publisher.class, publisherData.getAllPublishers());
                break;
            case "Sale":
            	dataTable = new getTables().gettable(sale.class, saleData.getAllSales());
                break;
        }
        
        // Add sample data
        
    }
    
    private void showInsertForm() {
        isUpdateMode = false; // FIXED: Reset update mode flag
        updateIdField = null;
        contentArea.getChildren().clear();
        
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.TOP_CENTER);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.98); -fx-background-radius: 20px;");
        
        Label title = new Label("➕ Insert New " + currentEntity);
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        VBox form = createForm();
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button submitBtn = new Button("Submit Record");
        submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25px; -fx-pref-width: 200px; -fx-pref-height: 50px;");
        submitBtn.setOnAction(e -> submitForm());
        
        Button clearBtn = new Button("Clear Form");
        clearBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25px; -fx-pref-width: 150px; -fx-pref-height: 50px;");
        clearBtn.setOnAction(e -> showInsertForm());
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25px; -fx-pref-width: 150px; -fx-pref-height: 50px;");
        cancelBtn.setOnAction(e -> updateContentArea());
        
        buttonBox.getChildren().addAll(submitBtn, clearBtn, cancelBtn);
        
        formContainer.getChildren().addAll(title, form, buttonBox);
        contentArea.getChildren().add(formContainer);

    }
    
    private VBox createForm() {
        VBox form = new VBox(15);
        form.setPadding(new Insets(30));
        form.setMaxWidth(800);
        form.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 15px;");
        
        // Create 2 columns for the form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(30);
        formGrid.setVgap(20);
        formGrid.setPadding(new Insets(10));


            createFormField();

                int i=0;
                int j=0;
                for(Control s:datafield.keySet()) {
                    VBox field = new VBox(10);
                    Label label = new Label(datafield.get(s)+"");
                    label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
                		if(s instanceof ComboBox){
                			ComboBox<String> comboBox=(ComboBox<String>) s;
                			field.getChildren().addAll(label,comboBox);
                		}
                        else if(s instanceof TextField){
                    			TextField textField=(TextField) s;
                    			field.getChildren().addAll(label,textField);
                    		}
                    formGrid.add(field, i%3 , j/3);
                    i++;
                    j++;
                    if(i==3) i=0;

                	}


        form.getChildren().add(formGrid);
        return form;
            }


    private void createFormField() {
    datafield.clear();
for(String labelText: getFieldLabelsForEntity()) {
    if(labelText.equals("type_id"))
        labelText="borrowertype_id";
    else if(labelText.equals("period_id"))
        labelText="loanperiod_id";
    if(labelText.toLowerCase().contains("available")){
        Label label = new Label();
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #bdc3c7; -fx-padding: 0 12px; -fx-font-size: 14px;");
        comboBox.getItems().addAll("Yes","No");
        comboBox.setPromptText("Choose availability status"); // FIXED: Added prompt text
        datafield.put(comboBox, labelText);
        continue;
    }
    if(labelText.toLowerCase().contains("_id")&&!labelText.toLowerCase().split("_")[0].equals(currentEntity.toLowerCase())) {
        System.out.println(currentEntity+" "+labelText);

        List<String>data= getdata( labelText.split("_")[0]);
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #bdc3c7; -fx-padding: 0 12px; -fx-font-size: 14px;");

        for(String s: Objects.requireNonNull(data))
            comboBox.getItems().add(s);
        labelText= labelText.replace("_id", "_name");
        // FIXED: Add descriptive prompt text based on field type
        String promptText = getPromptTextForComboBox(labelText, labelText.split("_")[0]);
        comboBox.setPromptText(promptText);
        datafield.put(comboBox, labelText);
    }
    else if(!labelText.toLowerCase().contains("id")){
    System.out.println(labelText);
    Label label = new Label();
    label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
    TextField textField = new TextField();
    textField.setPromptText("Enter " + labelText.toLowerCase() + "...");
    textField.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #bdc3c7; -fx-padding: 0 12px;");
    datafield.put(textField, labelText);}



      }
    }
    private String[] getFieldLabelsForEntity() {
        switch (currentEntity) {
            case "Book":
               return new operation().getField(book.class);
            case "Author":
            	return new operation().getField(Author.class);
            case "Borrower":
            	return new operation().getField(borrower.class);
            case "Borrowertype":
            	return new operation().getField(borrowertype.class);
            case "Loan":
            	return new operation().getField(loan.class);
            case "Loanperiod":
            	return new operation().getField(loanperiod.class);
            case "Publisher":
            	return new operation().getField(publisher.class);
            case "Sale":
            	return new operation().getField(sale.class);
            default:
            	return null;
        }
    }
    
    // Track current form mode and update ID field
    private boolean isUpdateMode = false;
    private TextField updateIdField = null;
    
    private void showUpdateForm() {
        isUpdateMode = true;
        showInsertForm();
        VBox formContainer = (VBox) contentArea.getChildren().get(0);
        Label title = (Label) formContainer.getChildren().get(0);
        title.setText("✏️ Update " + currentEntity);
        
        // Add ID search at the top
        HBox idSearchBox = new HBox(15);
        idSearchBox.setAlignment(Pos.CENTER_LEFT);
        idSearchBox.setPadding(new Insets(0, 0, 20, 0));
        
        Label idLabel = new Label("Enter " + currentEntity + " ID:");
        idLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        
        updateIdField = new TextField();
        updateIdField.setPromptText("Enter ID to update...");
        updateIdField.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #bdc3c7; -fx-padding: 0 12px;");
        updateIdField.setPrefWidth(200);
        Button searchBtn = new Button("Load Data");
        searchBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        
        // FIXED: Load data into form fields when search button is clicked
        searchBtn.setOnAction(e -> loadDataForUpdate());
        
        idSearchBox.getChildren().addAll(idLabel, updateIdField, searchBtn);
        
        // Get button box and modify Clear button behavior
        HBox buttonBox = (HBox) formContainer.getChildren().get(formContainer.getChildren().size() - 1);
        Button clearBtn = (Button) buttonBox.getChildren().get(1);
        
        // FIXED: Clear button should only clear fields, stay in Update mode
        clearBtn.setOnAction(e -> clearUpdateForm());
        
        // Change submit button to "Update Record"
        Button submitBtn = (Button) buttonBox.getChildren().get(0);
        submitBtn.setText("Update Record");
        submitBtn.setOnAction(e -> submitUpdateForm());
        
        formContainer.getChildren().add(1, idSearchBox);
    }
    
    /**
     * Clear form fields but stay in Update mode (FIXED)
     */
    private void clearUpdateForm() {
        // Clear all form fields
        for (Control control : datafield.keySet()) {
            if (control instanceof TextField) {
                ((TextField) control).clear();
            } else if (control instanceof ComboBox) {
                ((ComboBox<?>) control).setValue(null);
            }
        }
        // Clear ID field but keep in update mode
        if (updateIdField != null) {
            updateIdField.clear();
        }
    }
    
    /**
     * Load data for update operation
     */
    private void loadDataForUpdate() {
        if (updateIdField == null || updateIdField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter an ID to load data.");
            alert.showAndWait();
            return;
        }
        
        String idValue = updateIdField.getText().trim();
        String idColumnName = getIdColumnName();
        
        // Get field values from database
        String[] fieldNames = getFieldLabelsForEntity();
        if (fieldNames == null) {
            showErrorAlert("Error loading data", "Could not retrieve field names.");
            return;
        }
        
        // Get values using the improved operation class
        operation<Object> op = new operation<>();
        String[] values = op.getFieldValues(getEntityClass(), idColumnName, idValue);
        
        if (values == null || values.length == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No record found with ID: " + idValue);
            alert.showAndWait();
            return;
        }
        
        // Populate form fields with loaded values
        int valueIndex = 0;
        for (Control control : datafield.keySet()) {
            if (valueIndex >= values.length || valueIndex >= fieldNames.length) break;
            
            String fieldName = datafield.get(control);
            String dbFieldName = fieldNames[valueIndex];
            
            // Skip ID fields in the form (they're handled separately)
            if (dbFieldName.toLowerCase().contains("_id") && 
                dbFieldName.toLowerCase().split("_")[0].equals(currentEntity.toLowerCase())) {
                valueIndex++;
                continue;
            }
            
            if (control instanceof TextField) {
                ((TextField) control).setText(values[valueIndex] != null ? values[valueIndex] : "");
            } else if (control instanceof ComboBox) {
                ComboBox<String> comboBox = (ComboBox<String>) control;
                String value = values[valueIndex];
                // For foreign keys, convert ID to name
                if (fieldName.toLowerCase().contains("_name") && !fieldName.toLowerCase().contains("full_name")) {
                    String relatedEntity = fieldName.toLowerCase().replace("_name", "");
                    String name = getNameFromId(relatedEntity, value);
                    if (name != null && comboBox.getItems().contains(name)) {
                        comboBox.setValue(name);
                    }
                } else {
                    if (value != null && comboBox.getItems().contains(value)) {
                        comboBox.setValue(value);
                    }
                }
            }
            valueIndex++;
        }
    }
    
    /**
     * Submit update form
     */
    private void submitUpdateForm() {
        if (updateIdField == null || updateIdField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter an ID to update.");
            alert.showAndWait();
            return;
        }
        
        String idValue = updateIdField.getText().trim();
        String idColumnName = getIdColumnName();
        
        List<String> values = collectFormValues();
        if (values == null) return;
        
        operation<Object> op = new operation<>();
        boolean success = op.update(currentEntity.toLowerCase(), getEntityClass(), values, idValue, idColumnName);
        
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(currentEntity + " record has been successfully updated!");
            alert.showAndWait();
            updateContentArea(); // Refresh table
        } else {
            showErrorAlert("Update Failed", "An error occurred while updating the " + currentEntity + " record. Please try again.");
        }
    }
    
    /**
     * Get ID column name for current entity
     */
    private String getIdColumnName() {
        return switch (currentEntity.toLowerCase()) {
            case "book" -> "book_id";
            case "author" -> "author_id";
            case "borrower" -> "borrower_id";
            case "borrowertype" -> "type_id";
            case "loan" -> "loan_id";
            case "loanperiod" -> "period_id";
            case "publisher" -> "publisher_id";
            case "sale" -> "sale_id";
            case "user" -> "username";
            default -> currentEntity.toLowerCase() + "_id";
        };
    }
    
    /**
     * Get entity class for current entity
     */
    private Class<?> getEntityClass() {
        return switch (currentEntity) {
            case "Book" -> book.class;
            case "Author" -> Author.class;
            case "Borrower" -> borrower.class;
            case "Borrowertype" -> borrowertype.class;
            case "Loan" -> loan.class;
            case "Loanperiod" -> loanperiod.class;
            case "Publisher" -> publisher.class;
            case "Sale" -> sale.class;
            default -> Object.class;
        };
    }
    
    /**
     * Get name from ID for foreign key relationships
     */
    private String getNameFromId(String entityType, String idValue) {
        // This would need to query the database to get name from ID
        // For now, return null - can be implemented if needed
        return null;
    }
    
    private void showDeleteForm() {
        contentArea.getChildren().clear();
        
        VBox deleteContainer = new VBox(30);
        deleteContainer.setAlignment(Pos.CENTER);
        deleteContainer.setPadding(new Insets(40));
        deleteContainer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.98); -fx-background-radius: 20px;");
        
        Label warningIcon = new Label("⚠️");
        warningIcon.setStyle("-fx-font-size: 48px; -fx-text-fill: #e74c3c;");
        
        Label title = new Label("Delete " + currentEntity);
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #c0392b;");
        
        Label warning = new Label("You are about to permanently delete this " + currentEntity.toLowerCase() + " record.");
        warning.setStyle("-fx-font-size: 18px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        
        VBox idBox = new VBox(10);
        idBox.setAlignment(Pos.CENTER);
        
        Label idLabel = new Label("Enter " + currentEntity + " ID to confirm deletion:");
        idLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        
        TextField idField = new TextField();
        idField.setPromptText("Enter ID to delete...");
        idField.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #bdc3c7; -fx-padding: 0 12px;");
        idField.setPrefWidth(300);
        
        idBox.getChildren().addAll(idLabel, idField);
        
        HBox buttonBox = new HBox(25);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button confirmBtn = new Button("Confirm Deletion");
        confirmBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25px; -fx-pref-width: 200px; -fx-pref-height: 50px;");
        
        // FIXED: Implement delete functionality
        confirmBtn.setOnAction(e -> {
            String idValue = idField.getText().trim();
            if (idValue.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please enter an ID to delete.");
                alert.showAndWait();
                return;
            }
            
            // Confirm deletion
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Are you sure?");
            confirmAlert.setContentText("This action cannot be undone. Do you want to proceed?");
            
            if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                String idColumnName = getIdColumnName();
                operation<Object> op = new operation<>();
                boolean success = op.delete(currentEntity.toLowerCase(), idColumnName, idValue);
                
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText(currentEntity + " record has been successfully deleted!");
                    alert.showAndWait();
                    
                    // Refresh table
                    updateContentArea();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to delete " + currentEntity + " record. The record may not exist or may be referenced by other records.");
                    alert.showAndWait();
                }
            }
        });
        
        Button cancelBtn = new Button("Cancel Operation");
        cancelBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25px; -fx-pref-width: 200px; -fx-pref-height: 50px;");
        cancelBtn.setOnAction(e -> updateContentArea());
        
        buttonBox.getChildren().addAll(confirmBtn, cancelBtn);
        
        deleteContainer.getChildren().addAll(warningIcon, title, warning, idBox, buttonBox);
        contentArea.getChildren().add(deleteContainer);
    }
    /**
     * Get ID from name using PreparedStatement (FIXED: Now uses parameterized queries)
     * Handles special cases like full_name (CONCAT) properly
     */
    private int getIdFromName(String name, String table) {
        if (name == null || name.trim().isEmpty()) {
            return -1;
        }
        
        String sql = "";
        
        switch (table.toLowerCase()) {
            case "author":
                // For author, full_name is CONCAT(first_name, ' ', last_name)
                sql = "SELECT author_id FROM author WHERE CONCAT(first_name, ' ', last_name) = ?";
                break;
            case "borrower":
                // For borrower, full_name is CONCAT(first_name, ' ', last_name)
                sql = "SELECT borrower_id FROM borrower WHERE CONCAT(first_name, ' ', last_name) = ?";
                break;
            case "borrowertype":
                sql = "SELECT type_id FROM borrowertype WHERE type_name = ?";
                break;
            case "loanperiod":
                sql = "SELECT period_id FROM loanperiod WHERE period_name = ?";
                break;
            case "publisher":
                sql = "SELECT publisher_id FROM publisher WHERE name = ?";
                break;
            case "book":
                sql = "SELECT book_id FROM book WHERE title = ?";
                break;
            default:
                return -1;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (conn == null) {
                System.err.println("Failed to get database connection");
                return -1;
            }
            
            pstmt.setString(1, name);
            
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
    /**
     * Collect form values for insert/update operations (FIXED: Works for all entities)
     */
    private List<String> collectFormValues() {
        List<String> values = new ArrayList<>();
        String[] fieldNames = getFieldLabelsForEntity();
        
        if (fieldNames == null) {
            showErrorAlert("Error", "Could not retrieve field names.");
            return null;
        }
        
        // Create a map of field names to controls for easier lookup
        Map<String, Control> fieldMap = new LinkedHashMap<>();
        for (Control control : datafield.keySet()) {
            fieldMap.put(datafield.get(control), control);
        }
        
        // Process values in order of database columns
        for (String dbFieldName : fieldNames) {
            String formFieldName = dbFieldName;
            
            // Handle special field name mappings
            if (dbFieldName.equals("type_id")) {
                formFieldName = "borrowertype_name";
            } else if (dbFieldName.equals("period_id")) {
                formFieldName = "loanperiod_name";
            } else if (dbFieldName.toLowerCase().contains("_id") && 
                      !dbFieldName.toLowerCase().split("_")[0].equals(currentEntity.toLowerCase())) {
                formFieldName = dbFieldName.replace("_id", "_name");
            }
            
            Control control = fieldMap.get(formFieldName);
            
            if (control == null) {
                // Field not in form (might be auto-generated ID), use empty string
                values.add("");
                continue;
            }
            
            if (control instanceof TextField) {
                TextField textField = (TextField) control;
                values.add(textField.getText().trim());
            } else if (control instanceof ComboBox) {
                ComboBox<String> comboBox = (ComboBox<String>) control;
                String selectedValue = comboBox.getValue();
                
                if (selectedValue == null || selectedValue.trim().isEmpty()) {
                    values.add("");
                    continue;
                }
                
                // Handle available field
                if (dbFieldName.toLowerCase().contains("available")) {
                    values.add(selectedValue.equals("Yes") ? "1" : "0");
                } else {
                    // Convert name to ID for foreign keys
                    String relatedEntity = dbFieldName.split("_")[0];
                    int id = getIdFromName(selectedValue, relatedEntity);
                    values.add(id > 0 ? String.valueOf(id) : "");
                }
            } else {
                values.add("");
            }
        }
        
        return values;
    }
    
    private void submitForm() {
        if (isUpdateMode) {
            submitUpdateForm();
            return;
        }
        
        List<String> values = collectFormValues();
        if (values == null || values.isEmpty()) {
            showErrorAlert("Error", "Could not collect form values.");
            return;
        }
        
        // Get the correct entity class
        Class<?> entityClass = getEntityClass();
        if (entityClass == Object.class) {
            showErrorAlert("Error", "Unknown entity type: " + currentEntity);
            return;
        }
        
        operation<Object> op = new operation<>();
        boolean success = false;
        
        try {
            success = op.insert(currentEntity.toLowerCase(), entityClass, values);
        } catch (Exception e) {
            System.err.println("Error during insert: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Error", "An error occurred: " + e.getMessage());
            return;
        }
        
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(currentEntity + " record has been successfully inserted!");
            alert.showAndWait();
            
            // Refresh table
            updateContentArea();
        } else {
            showErrorAlert("Insert Failed", "An error occurred while inserting the " + currentEntity + " record. Please check your input and try again.");
        }
    }
    
    /**
     * Show error alert dialog
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}