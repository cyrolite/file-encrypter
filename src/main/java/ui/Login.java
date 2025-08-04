package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.UserDB;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        // UI Elements
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        userField.getStyleClass().add("text-field");

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.getStyleClass().add("password-field");

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        Label statusLabel = new Label();
        statusLabel.setId("status-label");

        // Login action
        loginBtn.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if (UserDB.validateUser(username, password)) {
                System.out.println("Login successful for: " + username);
                new Main().startForUser(new Stage(), username);
                primaryStage.close();
            } else {
                statusLabel.setText("Invalid credentials.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        // Register action
        registerBtn.setOnAction(e -> {
            if (UserDB.registerUser(userField.getText(), passField.getText())) {
                statusLabel.setText("User registered! You can now log in.");
                statusLabel.setStyle("-fx-text-fill: green;");
            } else {
                statusLabel.setText("User already exists.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        // Layout setup
        VBox layout = new VBox(10, userLabel, userField, passLabel, passField, loginBtn, registerBtn, statusLabel);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Scene & Stage
        Scene scene = new Scene(layout, 320, 280);
        scene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());

        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}