// package util;

import java.util.Arrays;


/**
 * ParsedFile class represents a file with its content and type.
 * It provides methods to retrieve the file type and content.
 */
public final class ParsedFile {
    private final byte[] content;
    private final String fileType;


    /**Constructs a ParsedFile with the given content.
     * The file type is automatically detected based on the content.
     *
     * @param content The byte array representing the file content.
     */ 
    public ParsedFile(byte[] content) {
        this.content = content.clone(); // defensive copy
        this.fileType = detectFileType();
    }

    /**
     * Returns the file type of the ParsedFile.
     *
     * @return The file type as a string.
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Returns the content of the ParsedFile.
     * The content is returned as a defensive copy to prevent external modification. 
     * @return The content of the file as a byte array.
     */
    public byte[] getContent() {
        return content.clone();
    }

    @Override
    /**
     * Returns a string representation of the ParsedFile.
     * @return A string containing the file type and content length.
     */
    public String toString() {
        return "ParsedFile{" +
                "fileType='" + fileType + '\'' +
                ", contentLength=" + content.length +
                '}';
    }  

    /**
     * Detects the file type based on the content's byte signature.
     * This method checks for common file signatures to determine the type. 
     * @return The detected file type as a string.
     */
    private String detectFileType() {
        if (startsWith(content, new byte[]{0x25, 0x50, 0x44, 0x46})) {
            return "pdf";
        } else if (startsWith(content, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})) {
            return "jpg";
        } else if (startsWith(content, new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47})) {
            return "png";
        } else if (startsWith(content, new byte[]{0x47, 0x49, 0x46, 0x38})) {
            return "gif";
        } else if (startsWith(content, new byte[]{0x50, 0x4B, 0x03, 0x04})) {
            return "zip";
        } else if (startsWith(content, new byte[]{0x25, 0x21})) {
            return "ps";
        } else if (startsWith(content, new byte[]{(byte) 0xD0, (byte) 0xCF, 0x11, (byte) 0xE0})) {
            return "doc"; // Could be DOC or XLS pre-2003
        } else if (startsWith(content, new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF})
                || isProbablyAscii(content)) {
            return "txt";
        }
        return "unknown";
    }

    /**
     * Checks if the byte array starts with the specified signature.
     * @param data The byte array to check.
     * @param signature The byte array representing the signature to check against.
     * @return true if the data starts with the signature, false otherwise.
     */
    private boolean startsWith(byte[] data, byte[] signature) {
        if (data.length < signature.length) return false;
        for (int i = 0; i < signature.length; i++) {
            if (data[i] != signature[i]) return false;
        }
        return true;
    }

    /**
     * Checks if the byte array is likely to be ASCII text.
     * This method checks the first 100 bytes for common ASCII characters. 
     * @param data The byte array to check.
     * @return true if the data is likely ASCII, false otherwise.
     */
    private boolean isProbablyAscii(byte[] data) {
        int limit = Math.min(data.length, 100);
        for (int i = 0; i < limit; i++) {
            if (data[i] < 0x09 || (data[i] > 0x0D && data[i] < 0x20)) {
                return false;
            }
        }
        return true;
    }

    @Override
    /**
     * Checks if this ParsedFile is equal to another object.
     * Two ParsedFile objects are considered equal if their content and file type are the same.
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParsedFile)) return false;
        ParsedFile that = (ParsedFile) o;
        return Arrays.equals(content, that.content) && fileType.equals(that.fileType);
    }
}
