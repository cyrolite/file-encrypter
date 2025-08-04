package io;

import io.FileParser;
import io.FileOutputParser;
import util.ParsedFile;
import tools.AES256;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("File Encrypter/Decrypter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            frame.setLayout(new GridLayout(6, 1));

            JTextField filePathField = new JTextField();
            JButton browseFileBtn = new JButton("Select File to Encrypt");

            JTextField outputDirField = new JTextField();
            JButton browseOutputBtn = new JButton("Select Output Directory");

            JButton encryptBtn = new JButton("Encrypt and Decrypt");
            JTextArea resultArea = new JTextArea();

            browseFileBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    filePathField.setText(file.getAbsolutePath().replace("\\", "/"));
                }
            });

            browseOutputBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    outputDirField.setText(file.getAbsolutePath().replace("\\", "/"));
                }
            });

            encryptBtn.addActionListener(e -> {
                try {
                    String filePath = filePathField.getText();
                    String outputDir = outputDirField.getText();
                    String secretKey = "pass";
                    String salt = "salt";

                    ParsedFile originalFile = FileParser.parse(filePath);
                    FileOutputParser.writeEncrypted(outputDir, originalFile, secretKey, salt);

                    String encryptedPath = outputDir + "/encrypt.txt";
                    ParsedFile encryptedFile = FileParser.parse(encryptedPath);
                    FileOutputParser.writeDecrypted(outputDir, encryptedFile, secretKey, salt);

                    String decryptedPath = outputDir + "/decrypt." + originalFile.getFileType();

                    resultArea.setText("Encryption and Decryption complete.\n" +
                        "Encrypted: " + encryptedPath + "\nDecrypted: " + decryptedPath);
                } catch (Exception ex) {
                    resultArea.setText("Error: " + ex.getMessage());
                }
            });

            frame.add(browseFileBtn);
            frame.add(filePathField);
            frame.add(browseOutputBtn);
            frame.add(outputDirField);
            frame.add(encryptBtn);
            frame.add(new JScrollPane(resultArea));

            frame.setVisible(true);
        });
    }
}
