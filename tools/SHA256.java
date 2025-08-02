import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

class SHA256 {
    /**
     * Generates a SHA-256 hash of the given plaintext.
     * * @param plaintext The input string to hash.
     * * @return The SHA-256 hash as a hexadecimal string.
     * @throws RuntimeException if SHA-256 is not available.
     */
    public static String hash(String plaintext) {
        try {
            // Create a SHA-256 MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(plaintext.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            // Convert the byte array to a hexadecimal string
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            
            // Return the hexadecimal string
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }


    @Override
    /**
     * Returns a string representation of the SHA256 class.
     * @return A string indicating the class name. 
     */
    public String toString() {
        return "SHA256";
    }
}
