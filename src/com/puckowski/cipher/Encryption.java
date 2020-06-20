package com.puckowski.cipher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Handler;

public class Encryption extends CipherUtils {
	private final Handler mHandler = new Handler();
	
	private FileInputStream mCurrentFile;
	private FileOutputStream mEncryptedOutput;
	
	private String mFilePath = "";
	private String mEncryptionKey = "";
	
	public Encryption() {
		super();
	}
	
	public Encryption(String cipherMethod) {
		super(cipherMethod);
	}
	
	public void encryptFile(String filePath, String encryptionKey) {	
		try {
			mCurrentFile = new FileInputStream(filePath);
			mEncryptedOutput = new FileOutputStream((filePath + TEMPORARY_FILE_EXTENSION));	
			
			mFilePath = filePath;
			mEncryptionKey = encryptionKey;
			
			encrypt();
		} catch(FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		}
	}
	
	private void xorEncrypt() {
		FileUtils fileUtils = new FileUtils();
		XorCipher xorCipher = new XorCipher();
		
		xorCipher.encryptStream(mEncryptionKey, mCurrentFile, mEncryptedOutput);
		cleanup(fileUtils, mFilePath, mCleanupOriginalFile);
	}

	public void encrypt() {		
		Thread encryptionThread = new Thread() {
		    @Override
		    public void run() {
		        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		    		
		    	switch(mCipherMethod) {
		    		case XOR:
		    			xorEncrypt();
		    			break;
		    	};
		                
		    	mHandler.post(this);
		    }
		};

		encryptionThread.start();
	}
}
