import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.io.UnsupportedEncodingException;

/**
 * AES256 class provides methods to encrypt strings using AES-256 encryption.
 */
public class AES256 {

    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;

    /**
     * Encrypts a string using AES-256 encryption with a given secret key and salt.
     *
     * @param strToEncrypt The string to encrypt.
     * @param secretKey The secret key used for encryption.
     * @param salt The salt used for key derivation.
     * @return The Base64 encoded encrypted string.
     * @throws RuntimeException if AES-256 encryption is not available.
     */
    public static String encrypt(String strToEncrypt, String secretKey, String salt) {
        try {
            // Generate a random IV
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            
            // Derive the key using PBKDF2 with HMAC SHA-256
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
            
            // Initialize the cipher for encryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);

            // Encrypt the data
            byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            byte[] encryptedData = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);
            
            // Encode the result as Base64
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("AES-256 not available", e);
        }
    }

    /**
     * Decrypts a Base64 encoded string using AES-256 decryption with a given secret key and salt.
     * * @param strToDecrypt The Base64 encoded string to decrypt.
     * * @param secretKey The secret key used for decryption.
     * * @param salt The salt used for key derivation.
     * * @return The decrypted string.
     * @throws RuntimeException if AES-256 decryption is not available.
     */
    public static String decrypt(String strToDecrypt, String secretKey, String salt) {
        try {
            // Decode the Base64 encoded string
            byte[] encryptedData = Base64.getDecoder().decode(strToDecrypt);

            // Extract the IV from the encrypted data
            byte[] iv = new byte[16];
            System.arraycopy(encryptedData, 0, iv, 0, iv.length);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            // Derive the key using PBKDF2 with HMAC SHA-256
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Initialize the cipher for decryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);

            // Decrypt the data
            byte[] cipherText = new byte[encryptedData.length - iv.length];
            System.arraycopy(encryptedData, iv.length, cipherText, 0, cipherText.length);
            byte[] decryptedData = cipher.doFinal(cipherText);

            // Convert the decrypted data to a string
            return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("AES-256 not available", e);
        }
    }


    @Override
    /**
     * Returns a string representation of the AES256 class.
     * @return A string indicating the class name.
     */
    public String toString() {
        return "AES256";
    }
}