package ru.mstoyan.shiko.androidlogin.security;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class to generate secret keys.
 */

class KeysGenerator {

    private static final int AES_KEY_LENGTH_BITS = 128;
    private static final String SALT = "P':/>pc$^Uv?LA725;!2r*)42}Ow/^1B\n";
    private static final int SALT_LENGTH = 16;
    private static final int PBE_ITERATION_COUNT = 1000;
    private static final int HMAC_KEY_LENGTH_BITS = 256;
    private static final String PBE_ALGORITHM = "PBKDF2WithHmacSHA1";

    static KeysPair generateConfidentialKey(String password, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException {
        KeysPair result = new KeysPair();
        final String salt = generateSalt();
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(),salt.getBytes(),PBE_ITERATION_COUNT,
                AES_KEY_LENGTH_BITS + HMAC_KEY_LENGTH_BITS);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        byte[] confidentialKeyBytes = new byte[AES_KEY_LENGTH_BITS / 8];
        byte[] integrityBytes = new byte[HMAC_KEY_LENGTH_BITS / 8];

        System.arraycopy(keyBytes,0,confidentialKeyBytes,0,confidentialKeyBytes.length);
        System.arraycopy(keyBytes,confidentialKeyBytes.length,integrityBytes,0,integrityBytes.length);

        result.confidentialityKey = new SecretKeySpec(confidentialKeyBytes,algorithm);
        result.integrityKey = new SecretKeySpec(integrityBytes,algorithm);

        return result;
    }

    private static String generateSalt() throws NoSuchAlgorithmException {
        String saltBuilder = String.valueOf(SALT.charAt(6)) +
                SALT.charAt(3) +
                SALT.charAt(18) +
                SALT.substring(7, 10) +
                AES_KEY_LENGTH_BITS +
                SALT.codePointAt(24) +
                SALT_LENGTH +
                "35" +
                SALT.charAt(4);
        return saltBuilder;
    }

    static class KeysPair{
        SecretKey confidentialityKey;
        SecretKey integrityKey;
    }
}
