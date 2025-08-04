package ui;

import io.FileParser;
import io.FileOutputParser;
import util.ParsedFile;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.File;

/**
 * Main UI for the File Encrypter/Decrypter application.
 * Launched after user login and tied to the current user's folder.
 */
public class Main extends Application {
    private static String currentUser;

    public static void launchWithUser(String username) {
        Main.currentUser = username;
        Application.launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Encrypter/Decrypter - User: " + currentUser);

        // Prepare user's directory
        File userDir = new File("users/" + currentUser);
        if (!userDir.exists()) userDir.mkdirs();

        // UI components
        Label fileLabel = new Label("File:");
        TextField filePathField = new TextField();
        Button browseFileBtn = new Button("Browse...");

        Label outputLabel = new Label("Output Directory:");
        TextField outputDirField = new TextField(userDir.getAbsolutePath().replace("\\", "/"));
        Button outputBrowseBtn = new Button("Browse...");

        outputBrowseBtn.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Output Directory");
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                outputDirField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        Label passwordLabel = new Label("Secret Key:");
        PasswordField passwordField = new PasswordField();

        Label saltLabel = new Label("Salt:");
        TextField saltField = new TextField();
        Button generateSaltBtn = new Button("Generate Salt");

        Label actionLabel = new Label("Action:");
        ComboBox<String> actionComboBox = new ComboBox<>();
        actionComboBox.getItems().addAll("Encrypt", "Decrypt");
        actionComboBox.setValue("Encrypt");

        Button runBtn = new Button("Run");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // Button actions
        browseFileBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                filePathField.setText(file.getAbsolutePath().replace("\\", "/"));
            }
        });

        generateSaltBtn.setOnAction(e -> {
            byte[] saltBytes = new byte[16];
            new java.security.SecureRandom().nextBytes(saltBytes);
            String salt = java.util.Base64.getEncoder().encodeToString(saltBytes);
            saltField.setText(salt);
        });

        actionComboBox.setOnAction(e -> {
            boolean encrypting = actionComboBox.getValue().equals("Encrypt");
            saltField.setDisable(!encrypting);
            generateSaltBtn.setDisable(!encrypting);
        });

        runBtn.setOnAction(e -> {
            try {
                String filePath = filePathField.getText();
                String outputDir = outputDirField.getText();
                String secretKey = passwordField.getText();
                String action = actionComboBox.getValue();

                if (secretKey == null || secretKey.isEmpty()) {
                    resultArea.setText("Error: Secret key is required.");
                    return;
                }

                if (filePath == null || filePath.isEmpty()) {
                    resultArea.setText("Error: Please select a file.");
                    return;
                }

                if (action.equals("Encrypt")) {
                    String salt = saltField.getText();
                    if (salt == null || salt.isEmpty()) {
                        resultArea.setText("Error: Salt is required for encryption.");
                        return;
                    }

                    ParsedFile originalFile = FileParser.parse(filePath);
                    FileOutputParser.writeEncrypted(outputDir, originalFile, secretKey, salt);

                    String encryptedPath = outputDir + "/encrypt.txt";
                    resultArea.setText("Encryption complete.\nEncrypted: " + encryptedPath + "\nSalt used: " + salt);

                } else if (action.equals("Decrypt")) {
                    ParsedFile encryptedFile = FileParser.parse(filePath);
                    try {
                        FileOutputParser.writeDecrypted(outputDir, encryptedFile, secretKey, null);
                        String fileType = new ParsedFile(encryptedFile.getContent()).getFileType();
                        String decryptedPath = outputDir + "/decrypt." + fileType;

                        resultArea.setText("Decryption successful.\nDecrypted: " + decryptedPath);
                    } catch (Exception ex) {
                        resultArea.setText("Incorrect password or corrupted file.\n" + ex.getMessage());
                    }
                }
            } catch (Exception ex) {
                resultArea.setText("Error: " + ex.getMessage());
            }
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(fileLabel, 0, 0);
        grid.add(filePathField, 1, 0);
        grid.add(browseFileBtn, 2, 0);

        grid.add(outputLabel, 0, 1);
        grid.add(outputDirField, 1, 1);
        grid.add(outputBrowseBtn, 2, 1);

        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);

        grid.add(saltLabel, 0, 3);
        grid.add(saltField, 1, 3);
        grid.add(generateSaltBtn, 2, 3);

        grid.add(actionLabel, 0, 4);
        grid.add(actionComboBox, 1, 4);

        grid.add(runBtn, 1, 5);

        VBox vbox = new VBox(10, grid, resultArea);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vbox, 600, 400);
        try {
            scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("main.css not found or failed to load.");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startForUser(Stage stage, String username) {
        currentUser = username;
        start(stage);  // Reuse existing start method
    }
}
