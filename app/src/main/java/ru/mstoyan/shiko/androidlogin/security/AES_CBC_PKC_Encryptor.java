package ru.mstoyan.shiko.androidlogin.security;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Encrypts/decrypts strings
 */

public class AES_CBC_PKC_Encryptor extends Encryptor{

    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String CIPHER_ALGORITHM = "AES";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String ENCODING = "UTF-8";
    private static final int IV_LENGTH_BYTES = 16;
    private static final int BASE64_FLAGS = Base64.NO_WRAP;

    public String encrypt(String str) throws UnsupportedEncodingException, GeneralSecurityException {

        KeysGenerator.KeysPair keysPair = KeysGenerator.generateConfidentialKey(str,CIPHER_ALGORITHM);

        byte[] rawData = str.getBytes(ENCODING);

        byte[] iv = generateIv();
        Cipher aesCipherForEncryption = Cipher.getInstance(CIPHER_TRANSFORMATION);
        aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, keysPair.confidentialityKey,new IvParameterSpec(iv));
        iv = aesCipherForEncryption.getIV();
        byte[] encoded = aesCipherForEncryption.doFinal(rawData);
        EncryptedStruct struct = new EncryptedStruct(iv,encoded, keysPair.integrityKey);

        return struct.toString();
    }

    public String decrypt(String str) throws ParseException, GeneralSecurityException {

        KeysGenerator.KeysPair keysPair = KeysGenerator.generateConfidentialKey(str,CIPHER_ALGORITHM);

        EncryptedStruct data = new EncryptedStruct(str);
        if (!data.checkMacIntegrity(keysPair.integrityKey)){
            throw new GeneralSecurityException("MAC stored in civ does not match computed MAC.");
        }

        Cipher aesCipherForEncryption = Cipher.getInstance(CIPHER_TRANSFORMATION);
        aesCipherForEncryption.init(Cipher.DECRYPT_MODE, keysPair.confidentialityKey, new IvParameterSpec(data.mIv));
        byte[] decrypted = aesCipherForEncryption.doFinal(data.mEncoded);

        return new String(decrypted);
    }

    private boolean constantTimeEq(byte[] a, byte[] b){
        if (a.length != b.length)
            return false;
        int result = 0;
        for (int i = 0; i < a.length; i++){
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    private static byte[] generateIv() throws GeneralSecurityException {
        SecureRandom random = new SecureRandom();
        byte[] b = new byte[IV_LENGTH_BYTES];
        random.nextBytes(b);
        return b;
    }


    private class EncryptedStruct{
        byte[] mIv;
        byte[] mEncoded;
        byte[] mac;

        EncryptedStruct(String str){
            String[] params = str.split(":");
            if (params.length != 3)
                throw new IllegalArgumentException("Cannot parse mIv:ciphertext:mac!");
            mEncoded = Base64.decode(params[1],BASE64_FLAGS);
            mIv = Base64.decode(params[0],BASE64_FLAGS);
            mac = Base64.decode(params[2],BASE64_FLAGS);
        }

        EncryptedStruct(byte[] iv, byte encoded[], SecretKey key) throws InvalidKeyException, NoSuchAlgorithmException {
            mIv = iv;
            mEncoded = encoded;
            mac = calculateIntegrityMac(key, iv, encoded);
        }

        boolean checkMacIntegrity(SecretKey key) throws GeneralSecurityException {
            byte[] calculatedMac = calculateIntegrityMac(key, mIv, mEncoded);

            return constantTimeEq(mac,calculatedMac);
        }

        private byte[] calculateIntegrityMac(SecretKey integrityKey, byte[] iv, byte[] encoded) throws NoSuchAlgorithmException, InvalidKeyException {
            byte[] ivCipherConcat = new byte[iv.length + encoded.length];
            System.arraycopy(iv,0,ivCipherConcat,0,iv.length);
            System.arraycopy(encoded,0,ivCipherConcat,iv.length,encoded.length);

            Mac sha256_MAC = Mac.getInstance(HMAC_ALGORITHM);
            sha256_MAC.init(integrityKey);
            return sha256_MAC.doFinal(ivCipherConcat);
        }

        @Override
        public String toString() {
            String result = Base64.encodeToString(mIv, BASE64_FLAGS) +
                    ":" +
                    Base64.encodeToString(mEncoded, BASE64_FLAGS) +
                    ":" +
                    Base64.encodeToString(mac, BASE64_FLAGS);

            return result;
        }
    }
}
