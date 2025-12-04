package com.example.demo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import javafx.application.Application;
import javafx.collections.ObservableList;
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
        
        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search books, authors, borrowers...");
        searchField.setStyle("-fx-pref-height: 45px; -fx-background-radius: 25px; -fx-border-radius: 25px; -fx-border-color: #e0e0e0; -fx-padding: 0 20px; -fx-font-size: 14px;");
        searchField.setPrefWidth(350);
        
        // Search button
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 25px; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 45px; -fx-pref-width: 120px;");
        
        searchContainer.getChildren().addAll(searchField, searchButton);
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
            });
            entityBox.getChildren().add(btn);
        }
        
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
    
    private HBox createActionButtons() {
        HBox actionBox = new HBox(30);
        actionBox.setPadding(new Insets(20));
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);");
        
        Button insertBtn = createActionButton("➕ Insert", "#4CAF50");
        Button updateBtn = createActionButton("✏️ Update", "#2196F3");
        Button deleteBtn = createActionButton("🗑️ Delete", "#f44336");
        
        insertBtn.setOnAction(e -> showInsertForm());
        updateBtn.setOnAction(e -> showUpdateForm());
        deleteBtn.setOnAction(e -> showDeleteForm());
        
        actionBox.getChildren().addAll(insertBtn, updateBtn, deleteBtn);
        return actionBox;
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
    
    private void showUpdateForm() {
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
        
        TextField idField = new TextField();
        idField.setPromptText("Enter ID to update...");
        idField.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #bdc3c7; -fx-padding: 0 12px;");
        idField.setPrefWidth(200);
        Button searchBtn = new Button("Load Data");
        searchBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        
        idSearchBox.getChildren().addAll(idLabel, idField, searchBtn);
        
        formContainer.getChildren().add(1, idSearchBox);
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
        idField.setStyle("-fx-background-radius: 8px; -fx-border-radius: 8px; -fx-border-color: #bdc3c7; -fx-padding: 0 12px;");
        idField.setPrefWidth(300);
        
        idBox.getChildren().addAll(idLabel, idField);
        
        HBox buttonBox = new HBox(25);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button confirmBtn = new Button("Confirm Deletion");
        confirmBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25px; -fx-pref-width: 200px; -fx-pref-height: 50px;");
        
        Button cancelBtn = new Button("Cancel Operation");
        cancelBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-background-radius: 25px; -fx-pref-width: 200px; -fx-pref-height: 50px;");
        cancelBtn.setOnAction(e -> updateContentArea());
        
        buttonBox.getChildren().addAll(confirmBtn, cancelBtn);
        
        deleteContainer.getChildren().addAll(warningIcon, title, warning, idBox, buttonBox);
        contentArea.getChildren().add(deleteContainer);
    }
    private int getIdFromName(String name, String table) {
        String query = "";
        switch (table.toLowerCase()) {
            case "author":
                query = "SELECT author_id FROM author WHERE full_name = '" + name + "'";
                break;
            case "borrower":
                query = "SELECT borrower_id FROM borrower WHERE full_name = '" + name + "'";
                break;
            case "borrowertype":
                query = "SELECT type_id FROM borrowertype WHERE type_name = '" + name + "'";
                break;
            case "loanperiod":
                query = "SELECT period_id FROM loanperiod WHERE period_name = '" + name + "'";
                break;
            case "publisher":
                query = "SELECT publisher_id FROM publisher WHERE name = '" + name + "'";
                break;
            default:
                return -1;
        }
        try {
            ResultSet rs = Objects.requireNonNull(DatabaseConnection.getConnection()).createStatement().executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private boolean submitBookentity() {
        List<String> values = new ArrayList<>();
        for (Control control : datafield.keySet()) {
            if (control instanceof TextField) {

                TextField textField = (TextField) control;
                if(textField.getText().trim().isEmpty()){
                    values.add("");
                    continue;
                }
                values.add(textField.getText().trim());
            } else if (control instanceof ComboBox) {
                ComboBox<String> comboBox = (ComboBox<String>) control;
                if(comboBox.getValue()==null) {
                    values.add("");
                    continue;
                }
                if(datafield.get(control).toLowerCase().contains("available")){
                    values.add(comboBox.getValue().equals("Yes")?"1":"0");
                    continue;
                }
                values.add(String.valueOf(getIdFromName(comboBox.getValue(),"publisher")));
            }
        }
        return
        new operation().insert(currentEntity.toLowerCase(), book.class, values);

    }
    
    private void submitForm() {
        boolean success = false;
        // Show success message
        if(currentEntity.equals("Book"))
        	success=submitBookentity();
        if(success){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(currentEntity + " record has been successfully processed!");
        alert.showAndWait();
        
        // Return to main view with table
        updateContentArea();}
        else {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while processing the " + currentEntity + " record. Please try again.");
            alert.showAndWait();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}