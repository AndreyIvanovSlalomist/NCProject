package ru.nc.musiclib.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordUtils {
    public static String hashPassword(String password){
        byte[] salt = getSalt();
        String hashedPassword = hash(password,salt);
        return hashedPassword+"$"+ Base64.getEncoder().encodeToString(salt);
    }
    public static boolean verifyPassword(String password, String stored){
        String[] passwordAndSalt = stored.split("\\$");
        String hashedPassword = hash(password, Base64.getDecoder().decode(passwordAndSalt[1]));
        return hashedPassword.equals(passwordAndSalt[0]);
    }
    private static String hash(String password, byte[] salt){
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory;
        byte[] hash = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.getMessage();
        }
        return Base64.getEncoder().encodeToString(hash);
    }
    private static byte[] getSalt(){
        SecureRandom random = new SecureRandom();
        byte[]salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
