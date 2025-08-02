import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


/**
 * Parser class provides methods to parse files and return their content.
 */
class FileParser {
    /**
     * Parses a file and returns its content as a string.
     *
     * @param filePath The path to the file to be parsed.
     * @return The content of the file as a string.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    public static String parse(String filePath) {
        try {
            // Read the file content
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder content = new StringBuilder();
            String line;

            // Read each line and append it to the content
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            reader.close();

            // Return the content as a string
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }
}