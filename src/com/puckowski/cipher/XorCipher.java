package com.puckowski.cipher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XorCipher {
	private final int NO_ERROR = 0;
	private final int FILE_ERROR = -1;
	private final int END_OF_STREAM = -1;
	
	public int encryptStream(String encryptionKey, FileInputStream currentFile, FileOutputStream encryptedOutput) {
		int nextRead;
		int encryptionIndex = 0;
	
		char currentChar;
		
		try {
			while((nextRead = currentFile.read()) != END_OF_STREAM) {
			    currentChar = (char) nextRead;
			    encryptedOutput.write(currentChar ^= encryptionKey.charAt(encryptionIndex));
		
			    if(encryptionIndex < (encryptionKey.length() - 1)) {
					encryptionIndex++;
				} else {
					encryptionIndex = 0;
				}
			}
		} catch(IOException ioException) {
			return FILE_ERROR;
		}
		
		return NO_ERROR;
	}
	
	public int decryptStream(String encryptionKey, FileInputStream currentFile, FileOutputStream decryptedOutput) {
		int nextRead;
		int encryptionIndex = 0;
		
		char currentChar;
		
		try {
			while((nextRead = currentFile.read()) != END_OF_STREAM) {
			    currentChar = (char) nextRead;
			    decryptedOutput.write(currentChar ^= encryptionKey.charAt(encryptionIndex));
			  
			    if(encryptionIndex < (encryptionKey.length() - 1)) {
					encryptionIndex++;
				} else {
					encryptionIndex = 0;
				}
			}
		} catch(IOException ioException) {
			return FILE_ERROR;
		}
		
		return NO_ERROR;
	}
}
