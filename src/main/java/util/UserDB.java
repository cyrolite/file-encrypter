package util;

import java.io.File;
import java.sql.*;
import tools.SHA256;

/**
 * UserDB handles local user authentication using SQLite and SHA-256 hashing.
 */
public class UserDB {
    private static final String DB_PATH = "users.db";

    /**
     * Registers a new user in the database.
     * @param username The username.
     * @param password The plaintext password to hash.
     * @return true if user registered successfully, false if already exists or failed.
     */
    public static boolean registerUser(String username, String password) {
        try {
            // Show where the database file will be created
            File dbFile = new File(DB_PATH);
            System.out.println("Attempting DB connection at: " + dbFile.getAbsolutePath());

            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            ensureUsersTableExists(conn);

            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO users (username, password) VALUES (?, ?)"
            );
            pstmt.setString(1, username);
            pstmt.setString(2, SHA256.hash(password));

            int rows = pstmt.executeUpdate();
            conn.close();

            return rows == 1;
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates a user's login credentials.
     * @param username The username.
     * @param password The plaintext password to check.
     * @return true if the credentials match, false otherwise.
     */
    public static boolean validateUser(String username, String password) {
        try {
            File dbFile = new File(DB_PATH);
            System.out.println("Attempting DB connection at: " + dbFile.getAbsolutePath());

            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            ensureUsersTableExists(conn);

            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT password FROM users WHERE username = ?"
            );
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                boolean valid = SHA256.hash(password).equals(hashedPassword);
                conn.close();
                return valid;
            } else {
                conn.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Login validation failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ensures the users table exists before inserting or querying.
     * @param conn The database connection.
     * @throws SQLException if table creation fails.
     */
    private static void ensureUsersTableExists(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)");
    }
}
