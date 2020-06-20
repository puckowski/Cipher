package com.puckowski.cipher;

public enum CipherMethod {
    XOR("XOR");

    private final String mCipherMethod;
    
    private CipherMethod(final String cipherMethod) {
        mCipherMethod = cipherMethod;
    }
    
    @Override
    public String toString() {
        return mCipherMethod;
    }
}