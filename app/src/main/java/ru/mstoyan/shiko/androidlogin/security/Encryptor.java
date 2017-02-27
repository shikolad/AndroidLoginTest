package ru.mstoyan.shiko.androidlogin.security;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Abstract class to encrypt/decrypt String
 */

interface Encryptor {
    KeysPair getKeys(String password) throws NoSuchAlgorithmException, KeyStoreException, InvalidKeySpecException;
    boolean checkIntegrity(String str, SecretKey key) throws GeneralSecurityException;
    String encrypt(String str, KeysPair keys) throws UnsupportedEncodingException, GeneralSecurityException;
    String decrypt(String str, SecretKey confidentialityKey) throws ParseException, GeneralSecurityException;
}
