package ru.mstoyan.shiko.androidlogin.security;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Abstract class to encrypt/decrypt String
 */

interface Encryptor {
    String encrypt(String str, String password) throws UnsupportedEncodingException, GeneralSecurityException;
    String decrypt(String str, String password) throws ParseException, GeneralSecurityException;
}
