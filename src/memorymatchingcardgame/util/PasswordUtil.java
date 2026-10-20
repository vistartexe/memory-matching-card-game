package memorymatchingcardgame.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The utility password hashing with SHA-256.
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    // Convert the plain text password with hash SHA-256 (hex)
    public static String hash(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty.");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : hashed) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algorithm of SHA-256 was not available.", e);
        }
    }

    // Compare input password with stored hash
    public static boolean verify(String plainText, String storedHash) {
        if (storedHash == null) {
            return false;
        }
        return hash(plainText).equals(storedHash);
    }
}
