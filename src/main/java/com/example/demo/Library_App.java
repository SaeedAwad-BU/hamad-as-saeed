package com.example.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Library_App<E> extends Application {
	private static ObservableList<user> UserList;
	
	@Override
	public void start(Stage primaryStage) {
		UserList = new userData().getAllUsers();
		
		// Modern Login Scene
		Scene loginScene = createModernLoginScene(primaryStage);
		primaryStage.setScene(loginScene);
		primaryStage.setTitle("Saeed Library Management System");
		primaryStage.setMinWidth(500);
		primaryStage.setMinHeight(600);
		primaryStage.show();
	}

	public Scene createModernLoginScene(Stage primaryStage) {
		// ===== Main Container =====
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a0a, #1a1a1a);");
		
		// ===== Header with Custom Library Logo =====
		VBox header = new VBox(10);
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(40, 0, 20, 0));
		
		// Custom Library Logo
		VBox logoContainer = new VBox(5);
		logoContainer.setAlignment(Pos.CENTER);
		
		// Create a book-shaped logo
		HBox bookLogo = new HBox(0);
		bookLogo.setAlignment(Pos.CENTER);
		
		// Book spine
		Rectangle spine = new Rectangle(15, 50);
		spine.setFill(Color.web("#8A2BE2"));
		spine.setArcWidth(5);
		spine.setArcHeight(5);
		
		// Book cover
		Rectangle cover = new Rectangle(60, 50);
		cover.setFill(Color.web("#FFD700"));
		cover.setArcWidth(10);
		cover.setArcHeight(10);
		cover.setStroke(Color.web("#8A2BE2"));
		cover.setStrokeWidth(2);
		
		// Book pages
		Rectangle pages = new Rectangle(45, 46);
		pages.setFill(Color.WHITE);
		pages.setTranslateX(-5);
		
		bookLogo.getChildren().addAll(spine, cover);
		
		// Library Name
		Label libraryName = new Label("Saeed Library");
		libraryName.setFont(Font.font("Arial", FontWeight.BOLD, 32));
		libraryName.setTextFill(Color.web("#FFD700"));
		libraryName.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,215,0,0.4), 15, 0.5, 0, 0);");
		
		Label librarySubtitle = new Label("Knowledge • Wisdom • Innovation");
		librarySubtitle.setTextFill(Color.web("#A855F7"));
		librarySubtitle.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
		librarySubtitle.setStyle("-fx-effect: dropshadow(gaussian, rgba(168,85,247,0.3), 10, 0.5, 0, 0);");
		
		logoContainer.getChildren().addAll(bookLogo, libraryName, librarySubtitle);
		
		header.getChildren().addAll(logoContainer);
		
		// ===== Login Form =====
		VBox formContainer = new VBox(25);
		formContainer.setAlignment(Pos.CENTER);
		formContainer.setPadding(new Insets(30, 40, 30, 40));
		formContainer.setMaxWidth(400);
		formContainer.setStyle("-fx-background-color: rgba(20, 20, 20, 0.85); " +
							  "-fx-background-radius: 20; " +
							  "-fx-border-color: rgba(255,215,0,0.2); " +
							  "-fx-border-radius: 20; " +
							  "-fx-border-width: 1;");
		
		// Form Title
		Label formTitle = new Label("Welcome to Your Library");
		formTitle.setTextFill(Color.WHITE);
		formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		
		Label formSubtitle = new Label("Sign in to access your account");
		formSubtitle.setTextFill(Color.web("#A0A0A0"));
		formSubtitle.setFont(Font.font("Arial", 14));
		
		// Form Fields
		VBox fieldsContainer = new VBox(20);
		fieldsContainer.setAlignment(Pos.CENTER);
		
		// Username/Email Field
		VBox usernameBox = new VBox(8);
		Label usernameLabel = new Label("Username or Email");
		usernameLabel.setTextFill(Color.WHITE);
		usernameLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
		
		TextField usernameField = new TextField();
		usernameField.setPromptText("Enter your username or email");
		usernameField.setStyle("-fx-background-color: rgba(255,255,255,0.08); " +
							  "-fx-text-fill: white; " +
							  "-fx-prompt-text-fill: #666; " +
							  "-fx-background-radius: 10; " +
							  "-fx-padding: 14; " +
							  "-fx-border-color: rgba(138,43,226,0.3); " +
							  "-fx-border-radius: 10; " +
							  "-fx-border-width: 1;");
		usernameField.setPrefWidth(320);
		
		usernameBox.getChildren().addAll(usernameLabel, usernameField);
		
		// Password Field
		VBox passwordBox = new VBox(8);
		Label passwordLabel = new Label("Password");
		passwordLabel.setTextFill(Color.WHITE);
		passwordLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
		
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter your password");
		passwordField.setStyle("-fx-background-color: rgba(255,255,255,0.08); " +
							  "-fx-text-fill: white; " +
							  "-fx-prompt-text-fill: #666; " +
							  "-fx-background-radius: 10; " +
							  "-fx-padding: 14; " +
							  "-fx-border-color: rgba(138,43,226,0.3); " +
							  "-fx-border-radius: 10; " +
							  "-fx-border-width: 1;");
		passwordField.setPrefWidth(320);
		
		passwordBox.getChildren().addAll(passwordLabel, passwordField);
		
		// Error Message
		Label errorMessage = new Label();
		errorMessage.setTextFill(Color.web("#FF6B6B"));
		errorMessage.setFont(Font.font("Arial", 13));
		errorMessage.setVisible(false);
		errorMessage.setWrapText(true);
		errorMessage.setMaxWidth(320);
		errorMessage.setAlignment(Pos.CENTER);
		
		// Buttons Container
		HBox buttonsContainer = new HBox(15);
		buttonsContainer.setAlignment(Pos.CENTER);
		
		// Login Button
		Button loginButton = new Button("Log In");
		loginButton.setStyle("-fx-background-color: linear-gradient(to right, #8A2BE2, #A855F7); " +
							"-fx-text-fill: white; " +
							"-fx-font-weight: bold; " +
							"-fx-font-size: 14; " +
							"-fx-background-radius: 10; " +
							"-fx-padding: 14 35; " +
							"-fx-cursor: hand;");
		loginButton.setPrefWidth(150);
		
		// Sign Up Button
		Button signupButton = new Button("Create Account");
		signupButton.setStyle("-fx-background-color: transparent; " +
							 "-fx-text-fill: #FFD700; " +
							 "-fx-font-weight: bold; " +
							 "-fx-font-size: 14; " +
							 "-fx-border-color: #FFD700; " +
							 "-fx-border-radius: 10; " +
							 "-fx-border-width: 2; " +
							 "-fx-padding: 12 25; " +
							 "-fx-cursor: hand;");
		signupButton.setPrefWidth(150);
		
		buttonsContainer.getChildren().addAll(loginButton, signupButton);
		
		// Programmer Info Button
		Button infoButton = new Button("👨‍💻 About Developer");
		infoButton.setStyle("-fx-background-color: transparent; " +
						   "-fx-text-fill: #A855F7; " +
						   "-fx-border-color: transparent; " +
						   "-fx-cursor: hand; " +
						   "-fx-underline: false; " +
						   "-fx-font-size: 12;");
		
		// Footer
		Label footer = new Label("© 2025 Saeed Library • Empowering Knowledge");
		footer.setTextFill(Color.web("#555"));
		footer.setFont(Font.font("Arial", 11));
		
		// Add all elements to form container
		fieldsContainer.getChildren().addAll(usernameBox, passwordBox, errorMessage, buttonsContainer, infoButton);
		formContainer.getChildren().addAll(formTitle, formSubtitle, fieldsContainer);
		
		// ===== Layout Setup =====
		root.setTop(header);
		root.setCenter(formContainer);
		root.setBottom(footer);
		BorderPane.setAlignment(footer, Pos.CENTER);
		BorderPane.setMargin(footer, new Insets(0, 0, 20, 0));
		
		// ===== Event Handlers =====
		// Login Button Action
		loginButton.setOnAction(e -> {
			String username = usernameField.getText();
			String password = passwordField.getText();
			
			if (username.isEmpty() || password.isEmpty()) {
				errorMessage.setText("Please fill in all fields to continue");
				errorMessage.setVisible(true);
				return;
			}
			
			boolean loginSuccessful = false;
			for (user u : UserList) {
				if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
					loginSuccessful = true;
					break;
				}
			}
			
			if (loginSuccessful) {
				errorMessage.setVisible(false);
				// Add success animation
				loginButton.setText("✓ Success!");
				loginButton.setStyle("-fx-background-color: linear-gradient(to right, #00b09b, #96c93d); " +
									"-fx-text-fill: white; " +
									"-fx-font-weight: bold; " +
									"-fx-font-size: 14; " +
									"-fx-background-radius: 10; " +
									"-fx-padding: 14 35; " +
									"-fx-cursor: hand;");
				
				// Navigate to main application after delay
				new Thread(() -> {
					try {
						Thread.sleep(1000);
						javafx.application.Platform.runLater(() -> {
							getTables<user> getuser = new getTables<>();
							VBox vbox = new VBox(getuser.gettable(user.class, UserList));
							primaryStage.setScene(new Scene(vbox, 800, 600));
						});
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}).start();
			} else {
				errorMessage.setText("Invalid username or password. Please try again.");
				errorMessage.setVisible(true);
				
				// Shake animation for error
				loginButton.setStyle("-fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b); " +
									"-fx-text-fill: white; " +
									"-fx-font-weight: bold; " +
									"-fx-font-size: 14; " +
									"-fx-background-radius: 10; " +
									"-fx-padding: 14 35; " +
									"-fx-cursor: hand;");
				
				// Reset button style after delay
				new Thread(() -> {
					try {
						Thread.sleep(1500);
						javafx.application.Platform.runLater(() -> {
							loginButton.setStyle("-fx-background-color: linear-gradient(to right, #8A2BE2, #A855F7); " +
												"-fx-text-fill: white; " +
												"-fx-font-weight: bold; " +
												"-fx-font-size: 14; " +
												"-fx-background-radius: 10; " +
												"-fx-padding: 14 35; " +
												"-fx-cursor: hand;");
							loginButton.setText("Log In");
						});
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}).start();
			}
		});
		
		// Sign Up Button Action
		signupButton.setOnAction(e -> {
			Scene signupScene = createEnhancedSignupScene(primaryStage);
			primaryStage.setScene(signupScene);
		});
		
		// Programmer Info Button Action
		infoButton.setOnAction(e -> {
			Scene programmerInfoScene = createProgrammerInfoScene(primaryStage);
			primaryStage.setScene(programmerInfoScene);
		});
		
		// ===== Enhanced Hover Effects =====
		loginButton.setOnMouseEntered(e -> {
			loginButton.setStyle("-fx-background-color: linear-gradient(to right, #A855F7, #8A2BE2); " +
								"-fx-text-fill: white; " +
								"-fx-font-weight: bold; " +
								"-fx-font-size: 14; " +
								"-fx-background-radius: 10; " +
								"-fx-padding: 14 35; " +
								"-fx-cursor: hand; " +
								"-fx-effect: dropshadow(gaussian, rgba(168,85,247,0.5), 10, 0.5, 0, 0);");
		});
		
		loginButton.setOnMouseExited(e -> {
			if (!loginButton.getText().equals("✓ Success!")) {
				loginButton.setStyle("-fx-background-color: linear-gradient(to right, #8A2BE2, #A855F7); " +
									"-fx-text-fill: white; " +
									"-fx-font-weight: bold; " +
									"-fx-font-size: 14; " +
									"-fx-background-radius: 10; " +
									"-fx-padding: 14 35; " +
									"-fx-cursor: hand;");
			}
		});
		
		signupButton.setOnMouseEntered(e -> {
			signupButton.setStyle("-fx-background-color: rgba(255,215,0,0.15); " +
								 "-fx-text-fill: #FFD700; " +
								 "-fx-font-weight: bold; " +
								 "-fx-font-size: 14; " +
								 "-fx-border-color: #FFD700; " +
								 "-fx-border-radius: 10; " +
								 "-fx-border-width: 2; " +
								 "-fx-padding: 12 25; " +
								 "-fx-cursor: hand; " +
								 "-fx-effect: dropshadow(gaussian, rgba(255,215,0,0.3), 10, 0.5, 0, 0);");
		});
		
		signupButton.setOnMouseExited(e -> {
			signupButton.setStyle("-fx-background-color: transparent; " +
								 "-fx-text-fill: #FFD700; " +
								 "-fx-font-weight: bold; " +
								 "-fx-font-size: 14; " +
								 "-fx-border-color: #FFD700; " +
								 "-fx-border-radius: 10; " +
								 "-fx-border-width: 2; " +
								 "-fx-padding: 12 25; " +
								 "-fx-cursor: hand;");
		});
		
		infoButton.setOnMouseEntered(e -> {
			infoButton.setStyle("-fx-background-color: rgba(168,85,247,0.1); " +
							   "-fx-text-fill: #A855F7; " +
							   "-fx-border-color: transparent; " +
							   "-fx-cursor: hand; " +
							   "-fx-underline: false; " +
							   "-fx-font-size: 12; " +
							   "-fx-background-radius: 5;");
		});
		
		infoButton.setOnMouseExited(e -> {
			infoButton.setStyle("-fx-background-color: transparent; " +
							   "-fx-text-fill: #A855F7; " +
							   "-fx-border-color: transparent; " +
							   "-fx-cursor: hand; " +
							   "-fx-underline: false; " +
							   "-fx-font-size: 12;");
		});
		
		// Input field focus effects
		usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				usernameField.setStyle("-fx-background-color: rgba(255,255,255,0.12); " +
									  "-fx-text-fill: white; " +
									  "-fx-prompt-text-fill: #666; " +
									  "-fx-background-radius: 10; " +
									  "-fx-padding: 14; " +
									  "-fx-border-color: rgba(255,215,0,0.6); " +
									  "-fx-border-radius: 10; " +
									  "-fx-border-width: 2;");
			} else {
				usernameField.setStyle("-fx-background-color: rgba(255,255,255,0.08); " +
									  "-fx-text-fill: white; " +
									  "-fx-prompt-text-fill: #666; " +
									  "-fx-background-radius: 10; " +
									  "-fx-padding: 14; " +
									  "-fx-border-color: rgba(138,43,226,0.3); " +
									  "-fx-border-radius: 10; " +
									  "-fx-border-width: 1;");
			}
		});
		
		passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				passwordField.setStyle("-fx-background-color: rgba(255,255,255,0.12); " +
									  "-fx-text-fill: white; " +
									  "-fx-prompt-text-fill: #666; " +
									  "-fx-background-radius: 10; " +
									  "-fx-padding: 14; " +
									  "-fx-border-color: rgba(255,215,0,0.6); " +
									  "-fx-border-radius: 10; " +
									  "-fx-border-width: 2;");
			} else {
				passwordField.setStyle("-fx-background-color: rgba(255,255,255,0.08); " +
									  "-fx-text-fill: white; " +
									  "-fx-prompt-text-fill: #666; " +
									  "-fx-background-radius: 10; " +
									  "-fx-padding: 14; " +
									  "-fx-border-color: rgba(138,43,226,0.3); " +
									  "-fx-border-radius: 10; " +
									  "-fx-border-width: 1;");
			}
		});
		
		return new Scene(root, 500, 650);
	}

	public Scene createEnhancedSignupScene(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a0a, #1a1a1a);");
		
		// Header with library logo
		VBox header = new VBox(10);
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(30, 0, 20, 0));
		
		Label title = new Label("Join Saeed Library");
		title.setStyle("-fx-font-size: 28px; -fx-text-fill: #FFD700; -fx-font-weight: bold;");
		
		Label subtitle = new Label("Create your account in seconds");
		subtitle.setStyle("-fx-text-fill: #A0A0A0; -fx-font-size: 14px;");
		
		header.getChildren().addAll(title, subtitle);
		
		// Form container
		VBox formContainer = new VBox(20);
		formContainer.setAlignment(Pos.CENTER);
		formContainer.setPadding(new Insets(30, 40, 30, 40));
		formContainer.setMaxWidth(450);
		formContainer.setStyle("-fx-background-color: rgba(20, 20, 20, 0.85); " +
							  "-fx-background-radius: 20; " +
							  "-fx-border-color: rgba(255,215,0,0.2); " +
							  "-fx-border-radius: 20; " +
							  "-fx-border-width: 1;");
		
		// Form Fields with labels
		VBox fieldsContainer = new VBox(15);
		fieldsContainer.setAlignment(Pos.CENTER_LEFT);
		fieldsContainer.setMaxWidth(350);
		
		// Username Field
		VBox usernameBox = new VBox(5);
		Label usernameLabel = new Label("Username");
		usernameLabel.setTextFill(Color.WHITE);
		usernameLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
		
		TextField usernameField = new TextField();
		usernameField.setPromptText("Choose a username");
		usernameField.setStyle("-fx-background-color: rgba(255,255,255,0.08); " +
							  "-fx-text-fill: white; " +
							  "-fx-prompt-text-fill: #666; " +
							  "-fx-background-radius: 10; " +
							  "-fx-padding: 12;");
		usernameField.setPrefWidth(350);
		
		usernameBox.getChildren().addAll(usernameLabel, usernameField);
		
		// Email Field
		VBox emailBox = new VBox(5);
		Label emailLabel = new Label("Email Address");
		emailLabel.setTextFill(Color.WHITE);
		emailLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
		
		TextField emailField = new TextField();
		emailField.setPromptText("your.email@example.com");
		emailField.setStyle("-fx-background-color: rgba(255,255,255,0.08); " +
						   "-fx-text-fill: white; " +
						   "-fx-prompt-text-fill: #666; " +
						   "-fx-background-radius: 10; " +
						   "-fx-padding: 12;");
		emailField.setPrefWidth(350);
		
		emailBox.getChildren().addAll(emailLabel, emailField);
		
		// Password Field
		VBox passwordBox = new VBox(5);
		Label passwordLabel = new Label("Password");
		passwordLabel.setTextFill(Color.WHITE);
		passwordLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
		
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Create a strong password");
		passwordField.setStyle("-fx-background-color: rgba(255,255,255,0.08); " +
							  "-fx-text-fill: white; " +
							  "-fx-prompt-text-fill: #666; " +
							  "-fx-background-radius: 10; " +
							  "-fx-padding: 12;");
		passwordField.setPrefWidth(350);
		
		passwordBox.getChildren().addAll(passwordLabel, passwordField);
		
		// Confirm Password Field
		VBox confirmPasswordBox = new VBox(5);
		Label confirmPasswordLabel = new Label("Confirm Password");
		confirmPasswordLabel.setTextFill(Color.WHITE);
		confirmPasswordLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
		
		PasswordField confirmPasswordField = new PasswordField();
		confirmPasswordField.setPromptText("Re-enter your password");
		confirmPasswordField.setStyle("-fx-background-color: rgba(255,255,255,0.08); " +
									 "-fx-text-fill: white; " +
									 "-fx-prompt-text-fill: #666; " +
									 "-fx-background-radius: 10; " +
									 "-fx-padding: 12;");
		confirmPasswordField.setPrefWidth(350);
		
		confirmPasswordBox.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);
		
		// Role Field with improved styling
		VBox roleBox = new VBox(5);
		Label roleLabel = new Label("Account Type");
		roleLabel.setTextFill(Color.WHITE);
		roleLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
		
		ComboBox<String> roleComboBox = new ComboBox<>();
		roleComboBox.getItems().addAll("Student", "Staff", "Administrator");
		roleComboBox.setPromptText("Select your role");
		roleComboBox.setStyle("-fx-background-color: rgba(255,255,255,0.08); " +
							 "-fx-text-fill: white; " +
							 "-fx-prompt-text-fill: #666; " +
							 "-fx-background-radius: 10; " +
							 "-fx-padding: 12; " +
							 "-fx-border-color: rgba(138,43,226,0.3); " +
							 "-fx-border-radius: 10;");
		
		// Set the text color for the selected item and items in the dropdown
		roleComboBox.setCellFactory(lv -> {
			return new javafx.scene.control.ListCell<String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
						setStyle("");
					} else {
						setText(item);
						setStyle("-fx-text-fill: white; -fx-background-color: rgba(30,30,30,0.9);");
					}
				}
			};
		});
		
		roleComboBox.setButtonCell(new javafx.scene.control.ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText("Select your role");
					setStyle("-fx-text-fill: #666;");
				} else {
					setText(item);
					setStyle("-fx-text-fill: white;");
				}
			}
		});
		
		roleComboBox.setPrefWidth(350);
		roleBox.getChildren().addAll(roleLabel, roleComboBox);
		
		// Message Label
		Label msgLabel = new Label("");
		msgLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 14px; -fx-alignment: center;");
		msgLabel.setMaxWidth(350);
		msgLabel.setWrapText(true);
		
		// Sign Up Button
		Button signupBtn = new Button("Create Account");
		signupBtn.setStyle("-fx-background-color: linear-gradient(to right, #8A2BE2, #A855F7); " +
						  "-fx-text-fill: white; " +
						  "-fx-font-weight: bold; " +
						  "-fx-background-radius: 10; " +
						  "-fx-padding: 14 40; " +
						  "-fx-cursor: hand;");
		signupBtn.setPrefWidth(350);
		
		// Back to Login Button
		Button backButton = new Button("← Back to Login");
		backButton.setStyle("-fx-background-color: transparent; " +
						   "-fx-text-fill: #A855F7; " +
						   "-fx-border-color: transparent; " +
						   "-fx-cursor: hand; " +
						   "-fx-font-size: 12;");
		
		// Add all fields to container
		fieldsContainer.getChildren().addAll(
			usernameBox, emailBox, passwordBox, confirmPasswordBox, roleBox, msgLabel, signupBtn, backButton
		);
		
		formContainer.getChildren().addAll(fieldsContainer);
		
		root.setTop(header);
		root.setCenter(formContainer);
		
		// ===== Event Handlers for Signup =====
		signupBtn.setOnAction(e -> {
			String name = usernameField.getText().trim();
			String email = emailField.getText().trim();
			String pass = passwordField.getText().trim();
			String confirmPass = confirmPasswordField.getText().trim();
			String role = roleComboBox.getValue();
			List<String> list = new ArrayList<>();
			list.addAll(Arrays.asList(name, email, pass, role));
			
			if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || role == null) {
				msgLabel.setText("Please fill all fields to continue!");
				msgLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 14px;");
				return;
			} else if (UserList.stream().anyMatch(ea -> ea.getUsername().equals(name))) {
				msgLabel.setText("Username already exists! Please choose a different one.");
				msgLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 14px;");
				return;
			}
			
			if (!pass.equals(confirmPass)) {
				msgLabel.setText("Passwords do not match! Please check and try again.");
				msgLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 14px;");
				return;
			}
			
			if (pass.length() < 6) {
				msgLabel.setText("Password must be at least 6 characters long.");
				msgLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 14px;");
				return;
			}
			
			// Simulate database operation
			operation<user> op = new operation<>();
			op.insert("user", user.class, list);
			
			// Success feedback
			msgLabel.setText("✓ Account created successfully! Welcome to Saeed Library.");
			msgLabel.setStyle("-fx-text-fill: #51cf66; -fx-font-size: 14px;");
			signupBtn.setText("✓ Account Created!");
			signupBtn.setStyle("-fx-background-color: linear-gradient(to right, #00b09b, #96c93d); " +
							  "-fx-text-fill: white; " +
							  "-fx-font-weight: bold; " +
							  "-fx-background-radius: 10; " +
							  "-fx-padding: 14 40; " +
							  "-fx-cursor: hand;");
			
			// Reset form after delay
			new Thread(() -> {
				try {
					Thread.sleep(3000);
					javafx.application.Platform.runLater(() -> {
						usernameField.clear();
						emailField.clear();
						passwordField.clear();
						confirmPasswordField.clear();
						roleComboBox.setValue(null);
						msgLabel.setText("");
						signupBtn.setText("Create Account");
						signupBtn.setStyle("-fx-background-color: linear-gradient(to right, #8A2BE2, #A855F7); " +
										  "-fx-text-fill: white; " +
										  "-fx-font-weight: bold; " +
										  "-fx-background-radius: 10; " +
										  "-fx-padding: 14 40; " +
										  "-fx-cursor: hand;");
					});
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}).start();
		});
		
		backButton.setOnAction(e -> {
			Scene loginScene = createModernLoginScene(primaryStage);
			primaryStage.setScene(loginScene);
		});
		
		// Hover effects
		signupBtn.setOnMouseEntered(e -> {
			if (!signupBtn.getText().equals("✓ Account Created!")) {
				signupBtn.setStyle("-fx-background-color: linear-gradient(to right, #A855F7, #8A2BE2); " +
								  "-fx-text-fill: white; " +
								  "-fx-font-weight: bold; " +
								  "-fx-background-radius: 10; " +
								  "-fx-padding: 14 40; " +
								  "-fx-cursor: hand; " +
								  "-fx-effect: dropshadow(gaussian, rgba(168,85,247,0.5), 10, 0.5, 0, 0);");
			}
		});
		
		signupBtn.setOnMouseExited(e -> {
			if (!signupBtn.getText().equals("✓ Account Created!")) {
				signupBtn.setStyle("-fx-background-color: linear-gradient(to right, #8A2BE2, #A855F7); " +
								  "-fx-text-fill: white; " +
								  "-fx-font-weight: bold; " +
								  "-fx-background-radius: 10; " +
								  "-fx-padding: 14 40; " +
								  "-fx-cursor: hand;");
			}
		});
		
		backButton.setOnMouseEntered(e -> {
			backButton.setStyle("-fx-background-color: rgba(168,85,247,0.1); " +
							   "-fx-text-fill: #A855F7; " +
							   "-fx-border-color: transparent; " +
							   "-fx-cursor: hand; " +
							   "-fx-font-size: 12; " +
							   "-fx-background-radius: 5;");
		});
		
		backButton.setOnMouseExited(e -> {
			backButton.setStyle("-fx-background-color: transparent; " +
							   "-fx-text-fill: #A855F7; " +
							   "-fx-border-color: transparent; " +
							   "-fx-cursor: hand; " +
							   "-fx-font-size: 12;");
		});
		
		return new Scene(root, 500, 700);
	}
	
	public Scene createProgrammerInfoScene(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0a0a0a, #1a1a1a);");
		
		// Header
		VBox header = new VBox(10);
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(30, 0, 20, 0));
		
		Label title = new Label("Developer Information");
		title.setStyle("-fx-font-size: 28px; -fx-text-fill: #FFD700; -fx-font-weight: bold;");
		
		Label subtitle = new Label("Meet the mind behind Saeed Library");
		subtitle.setStyle("-fx-text-fill: #A0A0A0; -fx-font-size: 14px;");
		
		header.getChildren().addAll(title, subtitle);
		
		// Main Content
		VBox content = new VBox(30);
		content.setAlignment(Pos.CENTER);
		content.setPadding(new Insets(30, 40, 40, 40));
		content.setMaxWidth(600);
		content.setStyle("-fx-background-color: rgba(20, 20, 20, 0.85); " +
						"-fx-background-radius: 20; " +
						"-fx-border-color: rgba(255,215,0,0.2); " +
						"-fx-border-radius: 20; " +
						"-fx-border-width: 1;");
		
		// Developer Profile
		VBox profile = new VBox(20);
		profile.setAlignment(Pos.CENTER);
		
		// Profile Icon
		Rectangle profileIcon = new Rectangle(100, 100);
		profileIcon.setFill(Color.TRANSPARENT);
		profileIcon.setStroke(Color.web("#FFD700"));
		profileIcon.setStrokeWidth(3);
		profileIcon.setArcWidth(20);
		profileIcon.setArcHeight(20);
		
		Label profileInitials = new Label("SA");
		profileInitials.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 32px; -fx-font-weight: bold;");
		
		VBox profileContainer = new VBox();
		profileContainer.setAlignment(Pos.CENTER);
		profileContainer.getChildren().addAll(profileIcon, profileInitials);
		
		// Developer Info
		VBox infoContainer = new VBox(15);
		infoContainer.setAlignment(Pos.CENTER);
		
		Label name = new Label("Saeed Awad");
		name.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
		
		Label titleRole = new Label("Full Stack Developer & System Architect");
		titleRole.setStyle("-fx-text-fill: #A855F7; -fx-font-size: 16px; -fx-font-weight: medium;");
		
		// Details Grid
		GridPane detailsGrid = new GridPane();
		detailsGrid.setHgap(20);
		detailsGrid.setVgap(15);
		detailsGrid.setAlignment(Pos.CENTER);
		
		// Detail items
		String[][] details = {
			{"🎓 Education", "3rd year of Software Engnieering\nAt Bethlehem University"},
			{"💼 Experience", "3+ Years in Full Stack Development\n1+ Years in System Architecture\nI'm not being honest"},
			{"🔧 Specialties", "Java • JavaFX • Web Development\nDatabase Design • UI/UX • System Integration"},
			{"🌍 Languages", "Arabic • English • German"}
		};
		
		for (int i = 0; i < details.length; i++) {
			Label category = new Label(details[i][0]);
			category.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 14px;");
			
			Label value = new Label(details[i][1]);
			value.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
			value.setWrapText(true);
			
			detailsGrid.add(category, 0, i);
			detailsGrid.add(value, 1, i);
		}
		
		// Project Description
		VBox projectBox = new VBox(10);
		projectBox.setAlignment(Pos.CENTER_LEFT);
		projectBox.setMaxWidth(500);
		
		Label projectTitle = new Label("About Saeed Library System");
		projectTitle.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 16px;");
		
		Label projectDesc = new Label("Saeed Library represents the pinnacle of modern library management systems. " +
									"Built with cutting-edge JavaFX technology, it combines elegant design with " +
									"powerful functionality. The system handles complex operations including user " +
									"management, inventory tracking, loan systems, and advanced reporting - all " +
									"within an intuitive interface that delights both administrators and patrons.");
		projectDesc.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-line-spacing: 1.5;");
		projectDesc.setWrapText(true);
		
		projectBox.getChildren().addAll(projectTitle, projectDesc);
		
		// Contact Information
		VBox contactBox = new VBox(10);
		contactBox.setAlignment(Pos.CENTER);
		
		Label contactTitle = new Label("Contact & Connect");
		contactTitle.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 16px;");
		
		HBox contactLinks = new HBox(20);
		contactLinks.setAlignment(Pos.CENTER);
		
		String[] contacts = {"📧 saeed@library.com", "🌐 www.saeeddev.com", "💼 LinkedIn", "🐙 GitHub"};
		for (String contact : contacts) {
			Label contactLabel = new Label(contact);
			contactLabel.setStyle("-fx-text-fill: #A855F7; -fx-font-size: 13px; -fx-cursor: hand;");
			contactLinks.getChildren().add(contactLabel);
		}
		
		contactBox.getChildren().addAll(contactTitle, contactLinks);
		
		// Action Buttons
		HBox actionButtons = new HBox(20);
		actionButtons.setAlignment(Pos.CENTER);
		
		Button backButton = new Button("← Back to Login");
		backButton.setStyle("-fx-background-color: transparent; " +
						   "-fx-text-fill: #A855F7; " +
						   "-fx-border-color: #A855F7; " +
						   "-fx-border-radius: 10; " +
						   "-fx-border-width: 2; " +
						   "-fx-padding: 10 25; " +
						   "-fx-cursor: hand;");
		
		Button portfolioButton = new Button("View Portfolio");
		portfolioButton.setStyle("-fx-background-color: linear-gradient(to right, #8A2BE2, #A855F7); " +
								"-fx-text-fill: white; " +
								"-fx-font-weight: bold; " +
								"-fx-background-radius: 10; " +
								"-fx-padding: 10 25; " +
								"-fx-cursor: hand;");
		
		actionButtons.getChildren().addAll(backButton, portfolioButton);
		
		// Add all to content
		infoContainer.getChildren().addAll(name, titleRole, detailsGrid, projectBox, contactBox);
		profile.getChildren().addAll(profileContainer, infoContainer);
		content.getChildren().addAll(profile, actionButtons);
		
		root.setTop(header);
		root.setCenter(content);
		
		// Event Handlers
		backButton.setOnAction(e -> {
			Scene loginScene = createModernLoginScene(primaryStage);
			primaryStage.setScene(loginScene);
		});
		
		portfolioButton.setOnAction(e -> {
			Alert portfolioAlert = new Alert(Alert.AlertType.INFORMATION);
			portfolioAlert.setTitle("Portfolio");
			portfolioAlert.setHeaderText("Saeed's Development Portfolio");
			portfolioAlert.setContentText("Thank you for your interest!\n\n" +
										"My portfolio showcases various projects including:\n" +
										"• Enterprise Management Systems\n" +
										"• Mobile Applications\n" +
										"• Web Platforms\n" +
										"• Database Design\n\n" +
										"Visit: www.saeeddev.com/portfolio");
			portfolioAlert.showAndWait();
		});
		
		// Hover effects
		backButton.setOnMouseEntered(e -> {
			backButton.setStyle("-fx-background-color: rgba(168,85,247,0.1); " +
							   "-fx-text-fill: #A855F7; " +
							   "-fx-border-color: #A855F7; " +
							   "-fx-border-radius: 10; " +
							   "-fx-border-width: 2; " +
							   "-fx-padding: 10 25; " +
							   "-fx-cursor: hand;");
		});
		
		backButton.setOnMouseExited(e -> {
			backButton.setStyle("-fx-background-color: transparent; " +
							   "-fx-text-fill: #A855F7; " +
							   "-fx-border-color: #A855F7; " +
							   "-fx-border-radius: 10; " +
							   "-fx-border-width: 2; " +
							   "-fx-padding: 10 25; " +
							   "-fx-cursor: hand;");
		});
		
		portfolioButton.setOnMouseEntered(e -> {
			portfolioButton.setStyle("-fx-background-color: linear-gradient(to right, #A855F7, #8A2BE2); " +
									"-fx-text-fill: white; " +
									"-fx-font-weight: bold; " +
									"-fx-background-radius: 10; " +
									"-fx-padding: 10 25; " +
									"-fx-cursor: hand; " +
									"-fx-effect: dropshadow(gaussian, rgba(168,85,247,0.5), 10, 0.5, 0, 0);");
		});
		
		portfolioButton.setOnMouseExited(e -> {
			portfolioButton.setStyle("-fx-background-color: linear-gradient(to right, #8A2BE2, #A855F7); " +
									"-fx-text-fill: white; " +
									"-fx-font-weight: bold; " +
									"-fx-background-radius: 10; " +
									"-fx-padding: 10 25; " +
									"-fx-cursor: hand;");
		});
		
		return new Scene(root, 700, 800);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}