package com.puckowski.cipher;

public class CipherUtils {
	protected final String TEMPORARY_FILE_EXTENSION = ".cipher";
	protected final CipherMethod DEFAULT_CIPHER_METHOD = CipherMethod.XOR;
	
	protected CipherMethod mCipherMethod;
	protected boolean mCleanupOriginalFile;
	
	public CipherUtils() {
		mCipherMethod = DEFAULT_CIPHER_METHOD;
		mCleanupOriginalFile = false;
	}
	
	public CipherUtils(String cipherMethod) {
		if(isValidCipherMethod(cipherMethod)) {
			mCipherMethod = CipherMethod.valueOf(cipherMethod);
		} else {
			mCipherMethod = DEFAULT_CIPHER_METHOD;
		}
		
		mCleanupOriginalFile = false;
	}
	
	public boolean isValidCipherMethod(String cipherMethod) {
		for(int i = 0; i < CipherMethod.values().length; i++) {
			if(CipherMethod.values()[i].equals(cipherMethod)) {
				return true;
			}
		}
		
		return false;
	}
	
	public CipherMethod getCipherMethod() {
		return mCipherMethod;
	}
	
	public boolean setCipherMethod(String cipherMethod) {
		if(isValidCipherMethod(cipherMethod)) {
			mCipherMethod = CipherMethod.valueOf(cipherMethod);
			return true;
		} else {
			return false;
		}
	}
	
	public void setCleanupMode(boolean cleanupOriginalFile) {
		mCleanupOriginalFile = cleanupOriginalFile;
	}
	
	public boolean getCleanupMode() {
		return mCleanupOriginalFile;
	}
	
	protected void cleanup(FileUtils fileUtils, String filePath, boolean removeOriginalFile) {
		if(removeOriginalFile == true) {
			fileUtils.removeFile(filePath);
			fileUtils.renameFile((filePath + TEMPORARY_FILE_EXTENSION), filePath);
		}
	}
}
