package com.puckowski.cipher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Handler;

public class Decryption extends CipherUtils {
	private final Handler mHandler = new Handler();
	
	private FileInputStream mCurrentFile;
	private FileOutputStream mDecryptedOutput;
	
	private String mFilePath = "";
	private String mDecryptionKey = "";
	
	public Decryption() {
		super();
	}
	
	public Decryption(String cipherMethod) {
		super(cipherMethod);
	}
	
	public void decryptFile(String filePath, String decryptionKey) {	
		try {
			mCurrentFile = new FileInputStream(filePath);
			mDecryptedOutput = new FileOutputStream((filePath + TEMPORARY_FILE_EXTENSION));
			
			mFilePath = filePath;
			mDecryptionKey = decryptionKey;
			
			decrypt();
		} catch(FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		}
	}
	
	private void xorDecrypt() {
		FileUtils fileUtils = new FileUtils();
		XorCipher xorCipher = new XorCipher();
		
		xorCipher.decryptStream(mDecryptionKey, mCurrentFile, mDecryptedOutput);
		cleanup(fileUtils, mFilePath, mCleanupOriginalFile);
	}

	private void decrypt() {
		Thread decryptionThread = new Thread() {
		    @Override
		    public void run() {
		        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		    		
		    	switch(mCipherMethod) {
		    		case XOR:
		    			xorDecrypt();
		    			break;
		    	};
		                
		    	mHandler.post(this);
		    }
		};

		decryptionThread.start();
	}
}