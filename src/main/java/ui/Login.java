package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import tools.SHA256;
import util.UserDB;

public class Login extends Application {
    private static final String DB_PATH = "users.db";

    @Override
    public void start(Stage primaryStage) {
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");
        Label statusLabel = new Label();

        loginBtn.setOnAction(e -> {
            System.out.println("Login button clicked");
            
            String username = userField.getText();  // ✅ Add this line
            String password = passField.getText();

            if (UserDB.validateUser(username, password)) {
                System.out.println("Login successful for: " + username);
                new Main().startForUser(new Stage(), username);  // ✅ Now works
                primaryStage.close();
            } else {
                System.out.println("Login failed for: " + username);
                statusLabel.setText("Invalid credentials.");
            }
        });

        registerBtn.setOnAction(e -> {
            if (UserDB.registerUser(userField.getText(), passField.getText())) {
                statusLabel.setText("User registered! You can now log in.");
            } else {
                statusLabel.setText("User already exists.");
            }
        });

        VBox layout = new VBox(10, userLabel, userField, passLabel, passField, loginBtn, registerBtn, statusLabel);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
