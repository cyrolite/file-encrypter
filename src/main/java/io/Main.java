package io;

import io.FileParser;
import io.FileOutputParser;
import util.ParsedFile;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.File;


/**
 * Main class is the entry point for the JavaFX application.
 * It provides a user interface for file encryption and decryption.
 * It allows users to select a file to encrypt, specify an output directory,
 * and perform encryption and decryption operations.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Encrypter/Decrypter");

        Label fileLabel = new Label("File to Encrypt:");
        TextField filePathField = new TextField();
        Button browseFileBtn = new Button("Browse...");

        Label outputLabel = new Label("Output Directory:");
        TextField outputDirField = new TextField();
        Button browseOutputBtn = new Button("Browse...");

        Label passwordLabel = new Label("Secret Key:");
        PasswordField passwordField = new PasswordField();

        Label saltLabel = new Label("Salt:");
        TextField saltField = new TextField();
        Button generateSaltBtn = new Button("Generate Salt");

        generateSaltBtn.setOnAction(e -> {
            // Secure random salt generation (16 bytes, base64 encoded)
            byte[] saltBytes = new byte[16];
            new java.security.SecureRandom().nextBytes(saltBytes);
            String salt = java.util.Base64.getEncoder().encodeToString(saltBytes);
            saltField.setText(salt);
        });

        Button encryptBtn = new Button("Encrypt and Decrypt");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        browseFileBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File to Encrypt");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                filePathField.setText(file.getAbsolutePath().replace("\\", "/"));
            }
        });

        browseOutputBtn.setOnAction(e -> {
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Select Output Directory");
            File dir = dirChooser.showDialog(primaryStage);
            if (dir != null) {
                outputDirField.setText(dir.getAbsolutePath().replace("\\", "/"));
            }
        });

        encryptBtn.setOnAction(e -> {
            try {
                String filePath = filePathField.getText();
                String outputDir = outputDirField.getText();
                String secretKey = passwordField.getText();
                String salt = saltField.getText();

                ParsedFile originalFile = FileParser.parse(filePath);
                FileOutputParser.writeEncrypted(outputDir, originalFile, secretKey, salt);

                String encryptedPath = outputDir + "/encrypt.txt";
                ParsedFile encryptedFile = FileParser.parse(encryptedPath);
                FileOutputParser.writeDecrypted(outputDir, encryptedFile, secretKey, salt);

                String decryptedPath = outputDir + "/decrypt." + originalFile.getFileType();

                resultArea.setText("Encryption and Decryption complete.\n" +
                    "Encrypted: " + encryptedPath + "\nDecrypted: " + decryptedPath +
                    "\nSalt used: " + salt);
            } catch (Exception ex) {
                resultArea.setText("Error: " + ex.getMessage());
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(fileLabel, 0, 0);
        grid.add(filePathField, 1, 0);
        grid.add(browseFileBtn, 2, 0);

        grid.add(outputLabel, 0, 1);
        grid.add(outputDirField, 1, 1);
        grid.add(browseOutputBtn, 2, 1);

        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);

        grid.add(saltLabel, 0, 3);
        grid.add(saltField, 1, 3);
        grid.add(generateSaltBtn, 2, 3);

        grid.add(encryptBtn, 1, 4);

        VBox vbox = new VBox(10, grid, resultArea);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vbox, 600, 350);
        scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}