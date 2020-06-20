package com.puckowski.cipher;

import java.io.File;

public class FileUtils {
	public boolean removeFile(String filePath) {
		File fileToRemove = new File(filePath);
		return fileToRemove.delete();
	}
	
	public boolean renameFile(String filePath, String newFileName) {
		String fullPath = filePath.substring(0, (filePath.lastIndexOf("/") + 1));
		String fileName = filePath.substring((filePath.lastIndexOf("/") + 1), filePath.length());
		
		if(newFileName.contains(fullPath)) {
			newFileName = newFileName.substring(fullPath.length(), newFileName.length());
		}
		
		File currentFile = new File(fullPath, fileName);
	    File renamedFile = new File(fullPath, newFileName);

	    if(currentFile.exists()) {
	        currentFile.renameTo(renamedFile);
	        return true;
	    } else {
	    	return false;
	    }
	}
}
