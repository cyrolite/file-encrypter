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
        ParsedFile encryptedFile = AES256.encryptFile(parsedFile, secretKey, salt);
        String filePath = fileDirectory.replace("\\", "/").replaceAll("\\.\\./", "").replaceAll("\\.\\.", "");
        filePath += "/encrypt.txt"; // Add missing slash
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(encryptedFile.getContent());
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
    public static void writeDecrypted(String fileDirectory, ParsedFile parsedFile, String secretKey, String salt) {
        ParsedFile decryptedFile = AES256.decryptFile(parsedFile, secretKey, salt);

        String filePath = fileDirectory.replace("\\", "/").replaceAll("\\.\\./", "").replaceAll("\\.\\.", "");
        filePath += "/decrypt." + decryptedFile.getFileType();

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