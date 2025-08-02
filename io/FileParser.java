// package io;
// package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

// import reader.ParsedFile;

/**
 * Parser class provides methods to parse files and return their content.
 */
class FileParser {
    /**
     * Parses a file and returns a ParsedFile object containing its content.
     * The file path is sanitized to prevent directory traversal attacks.
     * @param filePath The path to the file to be parsed.
     * @return A ParsedFile object containing the file's content.
     * @throws RuntimeException if the file cannot be read or parsed.
     */
    public static ParsedFile parse(String filePath) {
        String path = filePath.replace("\\", "/").replaceAll("\\.\\./", "").replaceAll("\\.\\.", "");
        File file = new File(path);

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];
            int bytesRead = fis.read(content);
            if (bytesRead != content.length) {
                throw new IOException("File read incomplete");
            }
            return new ParsedFile(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse file: " + filePath, e);
        }
    }
}