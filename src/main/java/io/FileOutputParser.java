package io;

import util.ParsedFile;
import tools.AES256;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * FileOutputParser class provides methods to write ParsedFile content to disk.
 */
public class FileOutputParser {
    /**
     * Writes the content of a ParsedFile to the specified file path.
     * @param filePath The path to write the file to.
     * @param parsedFile The ParsedFile object containing the content.
     * @throws RuntimeException if the file cannot be written.
     */
    public static void write(String filePath, ParsedFile parsedFile) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(parsedFile.getContent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + filePath, e);
        }
    }

    /**
     * Writes the encrypted content of a ParsedFile to a specified directory.
     * The file is saved as "encrypt.txt" in the specified directory.
     * @param fileDirectory The directory where the encrypted file will be saved.
     * @param parsedFile The ParsedFile object containing the content to encrypt.
     * @param secretKey The secret key used for encryption.
     * @param salt The salt used for key derivation.
     * @throws RuntimeException if the file cannot be written or encryption fails.
     */
    public static void writeEncrypted(String fileDirectory, ParsedFile parsedFile, String secretKey, String salt) {
        FileOutputParser.writeEncryptedWithFileName(fileDirectory, parsedFile, secretKey, salt, "encrypt.txt");    
    }

    public static void writeEncryptedWithFileName(String fileDirectory, ParsedFile parsedFile, String secretKey, String salt, String outputFileName) {
        ParsedFile encryptedFile = AES256.encryptFile(parsedFile, secretKey, salt);
        String filePath = fileDirectory.replace("\\", "/").replaceAll("\\.\\./", "").replaceAll("\\.\\.", "");
        filePath += "/" + outputFileName + ".enc";

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] saltBytes = java.util.Base64.getDecoder().decode(salt);
            byte[] encryptedBytes = encryptedFile.getContent();

            // Write salt first, then encrypted content
            fos.write(saltBytes);
            fos.write(encryptedBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write encrypted file: " + filePath, e);
        }
    }

    /**
     * Writes the decrypted content of a ParsedFile to a specified directory.
     * The file is saved as "decrypt.<fileType>" in the specified directory.
     * @param fileDirectory The directory where the decrypted file will be saved.
     * @param parsedFile The ParsedFile object containing the content to decrypt.
     * @param secretKey The secret key used for decryption.
     * @param salt The salt used for key derivation.
     * @throws RuntimeException if the file cannot be written or decryption fails.
     */
    public static void writeDecrypted(String fileDirectory, ParsedFile parsedFile, String secretKey, String ignoredSalt) {
        FileOutputParser.writeDecryptedWithFileName(fileDirectory, parsedFile, secretKey, ignoredSalt, "decrypt");
    }

    public static void writeDecryptedWithFileName(String fileDirectory, ParsedFile parsedFile, String secretKey, String ignoredSalt, String outputFileName) {
        byte[] fullContent = parsedFile.getContent();

        // Split salt and encrypted content
        byte[] saltBytes = new byte[16]; // 16 raw bytes = 128-bit salt
        byte[] encryptedBytes = new byte[fullContent.length - 16];

        System.arraycopy(fullContent, 0, saltBytes, 0, 16);
        System.arraycopy(fullContent, 16, encryptedBytes, 0, encryptedBytes.length);

        String extractedSalt = java.util.Base64.getEncoder().encodeToString(saltBytes);
        ParsedFile encryptedFile = new ParsedFile(encryptedBytes);

        ParsedFile decryptedFile = AES256.decryptFile(encryptedFile, secretKey, extractedSalt);

        String filePath = fileDirectory.replace("\\", "/").replaceAll("\\.\\./", "").replaceAll("\\.\\.", "");
        filePath += "/" + outputFileName + "." + decryptedFile.getFileType();

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(decryptedFile.getContent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write decrypted file: " + filePath, e);
        }
    }

    @Override
    /**
     * Returns a string representation of the FileOutputParser class.
     * @return A string indicating the class name.
     */
    public String toString() {
        return "FileOutputParser";
    }
}