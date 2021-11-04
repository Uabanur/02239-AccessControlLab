package dtu.group42.server;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class PasswordHash {
    private static final int ITERATION_NR = 1000;
    private static final int KEY_LENGTH = 512;

    public static void main(String[] args) {
        assert(hashPassword("test".toCharArray(), "salt".getBytes(StandardCharsets.UTF_8)) == hashPassword("test".toCharArray(), "salt".getBytes(StandardCharsets.UTF_8)));
        System.out.println("Test passed");
    }

    public static byte[] hashPassword(final char[] password, final byte[] salt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATION_NR, KEY_LENGTH);
            SecretKey key = skf.generateSecret(spec);
            return key.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
