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

    public static void writeDecrypted(String fileDirectory, ParsedFile parsedFile, String secretKey, String salt) {
        ParsedFile decryptedFile = AES256.decryptFile(parsedFile, secretKey, salt);
        System.out.println("Decrypted file type: " + decryptedFile.getFileType());

        String filePath = fileDirectory.replace("\\", "/").replaceAll("\\.\\./", "").replaceAll("\\.\\.", "");
        filePath += "/decrypt." + decryptedFile.getFileType();

        System.out.println("Decrypted file path: " + filePath);
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