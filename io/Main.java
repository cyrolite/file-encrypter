package io;

import io.FileParser;
import io.FileOutputParser;
import util.ParsedFile;
import tools.AES256;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the absolute path of the file to be encrypted: ");
        String filePathLocation = sc.nextLine()
            .replace("\\", "/")
            .replaceAll("\\.\\./", "")
            .replaceAll("\\.\\.", "");
        System.out.println("File to be encrypted: " + filePathLocation);


        System.out.print("Select the output of the directory you want the file to be in: ");
        String fileDestination = sc.nextLine()
            .replace("\\", "/")
            .replaceAll("\\.\\./", "")
            .replaceAll("\\.\\.", "");
        System.out.println("Output directory: " + fileDestination);


        String secretKey = "pass";
        String salt = "salt";
        FileOutputParser.writeEncrypted(fileDestination, FileParser.parse(filePathLocation), secretKey, salt);

        System.out.println("File encrypted successfully and saved in " + fileDestination + "/encrypt.txt");

        String encryptedFilePathLocation = fileDestination + "\\encrypt.txt";

        ParsedFile encryptedFile = FileParser.parse(encryptedFilePathLocation);
        System.out.print(encryptedFilePathLocation + " has been parsed successfully.\n");


        FileOutputParser.writeDecrypted(fileDestination, encryptedFile, secretKey, salt);
        System.out.println("File decrypted successfully and saved in " + fileDestination + 
            "\\decrypt." + FileParser.parse(filePathLocation).getFileType());
        sc.close();
        System.out.println("Program completed.");
    }
}