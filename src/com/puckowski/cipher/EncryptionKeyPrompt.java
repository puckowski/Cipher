package com.puckowski.cipher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EncryptionKeyPrompt {
	private Context mContext;
	
	private EditText mVerifyKeyField;
	private EditText mEncryptionKeyField;
	private ToggleButton mEncryptionToggle;
	
	private String mFilePath;
	private PromptDialogListener mPromptDialogListener = null;
	
	public interface PromptDialogListener {
		public void onKeyEntered(String encryptionKey, boolean encrypt);
	}
	
	public EncryptionKeyPrompt(Context context, PromptDialogListener promptDialogListener, String filePath) {
		mContext = context;
		mFilePath = filePath;
		mPromptDialogListener = promptDialogListener;
	}
	
	public void showDialog() {
		AlertDialog.Builder dialogBuilder = createEnterKeyDialog();

		dialogBuilder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(! doEncryptionKeysMatch() || getEncryptionKey().length() == 0) {
					Toast.makeText(mContext, R.string.key_match_error, Toast.LENGTH_LONG).show();
					return;
				}
				
				if(mPromptDialogListener != null) {
					displayEncryptionNotification();
					
					mPromptDialogListener.onKeyEntered(mEncryptionKeyField.getText().toString(), 
							mEncryptionToggle.isChecked());
				}
			}
		}).setNegativeButton("Cancel", null);

		final AlertDialog keyDialog = dialogBuilder.create();
		keyDialog.show();
	}
	
	private AlertDialog.Builder createEnterKeyDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View titleView = layoutInflater.inflate(R.layout.dialog_title, null);
		View dialogView = layoutInflater.inflate(R.layout.prompt_dialog, null);
		
		((TextView) titleView.findViewById(R.id.titleView)).setText("File: " + mFilePath);
		
		mEncryptionKeyField = (EditText) dialogView.findViewById(R.id.encryptionKey);
		mVerifyKeyField = (EditText) dialogView.findViewById(R.id.verifyKey);
		mEncryptionToggle = (ToggleButton) dialogView.findViewById(R.id.encryptionToggle);
		
		dialogBuilder.setCustomTitle(titleView);
		dialogBuilder.setView(dialogView); 
		dialogBuilder.setCancelable(false);
		
		return dialogBuilder;
	}
	
	private String getEncryptionKey() {
		return mEncryptionKeyField.getText().toString();
	}
	
	private boolean doEncryptionKeysMatch() {
		if(mEncryptionKeyField.getText().toString().equals(
				mVerifyKeyField.getText().toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	private void displayEncryptionNotification() {      
		if(mEncryptionToggle.isChecked()) {
			Toast.makeText(mContext, R.string.encrypting_message, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(mContext, R.string.decrypting_message, Toast.LENGTH_LONG).show();
		}        
	}
}
