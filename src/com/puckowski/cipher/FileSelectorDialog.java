package com.puckowski.cipher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FileSelectorDialog {
	private Context mContext;
	private boolean mEncryptionMode;
	
	private String mDirectory = "";
	private String mSdCardDirectory = "";
	private String mSelectedFileName = "";

	private TextView mTitleView;
	private EditText mFileSelection;
	
	private List<String> mFolders = null;
	private ArrayAdapter<String> mListAdapter = null;
	private FileDialogListener mFileDialogListener = null;

	public interface FileDialogListener {
		public void onChosenDirectory(String chosenDirectory);
	}

	public FileSelectorDialog(Context context, FileDialogListener fileDialogListener, boolean encryption) {	
		mContext = context;
		mEncryptionMode = encryption;
		
		mSdCardDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileDialogListener = fileDialogListener;

		try {
			mSdCardDirectory = new File(mSdCardDirectory).getCanonicalPath();
		} catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void chooseFileOrDirectory() {
		if(mDirectory.equals("")) {
			chooseFileOrDirectory(mSdCardDirectory);
		} else {
			chooseFileOrDirectory(mDirectory);
		}
	}

	public void chooseFileOrDirectory(String directory) {
		File directoryFile = new File(directory);
		
		if(! directoryFile.exists() || ! directoryFile.isDirectory()) {
			directory = mSdCardDirectory;
		}

		try {
			directory = new File(directory).getCanonicalPath();
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return;
		}

		mDirectory = directory;
		mFolders = getDirectories(directory);

		class FileDialogOnClickListener implements DialogInterface.OnClickListener {
			public void onClick(DialogInterface dialog, int item) {
				String mOldDirectory = mDirectory;
				String selection = "" + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
				
				if(selection.charAt(selection.length() - 1) == '/') {
					selection = selection.substring(0, selection.length() - 1);
				}
				
				if(selection.equals("..")) {
					   mDirectory = mDirectory.substring(0, mDirectory.lastIndexOf("/"));
				} else {
					   mDirectory += "/" + selection;
				}
				
				mSelectedFileName = "";
				
				if((new File(mDirectory).isFile())) {
					mDirectory = mOldDirectory;
					mSelectedFileName = selection;
				}
				
				updateDirectory();
			}
		}

		AlertDialog.Builder dialogBuilder = createDirectoryChooserDialog(directory, mFolders, 
				new FileDialogOnClickListener());

		dialogBuilder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mFileDialogListener != null) {
					displayEncryptionNotification();
					
					mSelectedFileName = mFileSelection.getText() + "";
					mFileDialogListener.onChosenDirectory(mDirectory + "/" + mSelectedFileName);
				}
			}
		}).setNegativeButton("Cancel", null);

		final AlertDialog directoriesDialog = dialogBuilder.create();
		directoriesDialog.show();
	}

	private List<String> getDirectories(String directory) {
		List<String> directories = new ArrayList<String>();
		
		try {
			File directoryFile = new File(directory);
			
			if(! mDirectory.equals(mSdCardDirectory)) {
				directories.add("..");
			}
			
			if(! directoryFile.exists() || ! directoryFile.isDirectory()) {
				return directories;
			}

			for(File file : directoryFile.listFiles()) {
				if(file.isDirectory()) {
					directories.add(file.getName() + "/");
				} else {
					directories.add(file.getName());
				}
			}
		} catch(Exception exception) {
			exception.printStackTrace();
		}

		Collections.sort(directories, new Comparator<String>() {	
			public int compare(String objectOne, String objectTwo) {
				return objectOne.compareTo(objectTwo);
			}
		});
		
		return directories;
	}
	
	private AlertDialog.Builder createDirectoryChooserDialog(String title, List<String> listItems,
			DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View titleView = layoutInflater.inflate(R.layout.dialog_title, null);
		View dialogView = layoutInflater.inflate(R.layout.file_dialog, null);

		mTitleView = (TextView) titleView.findViewById(R.id.titleView);
		mTitleView.setText(title);
		
		mFileSelection = (EditText) dialogView.findViewById(R.id.fileSelection);
		
		dialogBuilder.setCustomTitle(titleView);
		dialogBuilder.setView(dialogView);
		dialogBuilder.setCancelable(false);
		
		mListAdapter = createListAdapter(listItems);
		dialogBuilder.setSingleChoiceItems(mListAdapter, -1, onClickListener);
		
		return dialogBuilder;
	}

	private void updateDirectory() {
		mFolders.clear();
		mFolders.addAll(getDirectories(mDirectory));
		
		mTitleView.setText(mDirectory);
		mListAdapter.notifyDataSetChanged();
	
		mFileSelection.setText(mSelectedFileName);
	}

	private ArrayAdapter<String> createListAdapter(List<String> items) {
		return new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_item, android.R.id.text1, items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				
				if(view instanceof TextView) {
					TextView textView = (TextView) view;
					textView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
					textView.setEllipsize(null);
				}
				
				return view;
			}
		};
	}
	
	private void displayEncryptionNotification() {      
		if(mEncryptionMode) {
			Toast.makeText(mContext, R.string.encrypting_message, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(mContext, R.string.decrypting_message, Toast.LENGTH_LONG).show();
		}        
	}
} 