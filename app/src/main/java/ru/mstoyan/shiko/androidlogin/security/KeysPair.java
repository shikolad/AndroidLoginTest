package ru.mstoyan.shiko.androidlogin.security;

import javax.crypto.SecretKey;

/**
 * Class holds key pairs
 */

class KeysPair {
    private SecretKey mConfidentialityKey = null;
    private SecretKey mIntegrityKey = null;
    private KeysPair(){}

    KeysPair(SecretKey confidentialityKey, SecretKey integrityKey){
        mConfidentialityKey = confidentialityKey;
        mIntegrityKey = integrityKey;
    }

    SecretKey getConfidentialityKey(){
        return mConfidentialityKey;
    }

    SecretKey getIntegrityKey(){
        return mIntegrityKey;
    }

}
