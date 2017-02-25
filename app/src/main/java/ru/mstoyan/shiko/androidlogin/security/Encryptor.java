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

abstract class Encryptor {
    public abstract String encrypt(String str, String password) throws UnsupportedEncodingException, GeneralSecurityException;
    public abstract String decrypt(String str, String password) throws ParseException, GeneralSecurityException;
}
