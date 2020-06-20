package com.puckowski.cipher;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Encryption mEncryption;
	private Decryption mDecryption;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mEncryption = new Encryption();
		mDecryption = new Decryption();
		
		Uri data = getIntent().getData();
		if(data != null) {
		    processIntent(data);
		}
		    
		((Button) findViewById(R.id.encryptFile)).setOnClickListener(new EncryptionListener());
		((Button) findViewById(R.id.decryptFile)).setOnClickListener(new DecryptionListener());

		((CheckBox) findViewById(R.id.showKeyText)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(! isChecked) {
                	((EditText) findViewById(R.id.encryptionKey)).setTransformationMethod(PasswordTransformationMethod.getInstance());
                	((EditText) findViewById(R.id.verifyKey)).setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                	((EditText) findViewById(R.id.encryptionKey)).setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                	((EditText) findViewById(R.id.verifyKey)).setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
		
		((CheckBox) findViewById(R.id.removeOriginalFile)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(! isChecked) {
                	mEncryption.setCleanupMode(false);
                } else {
                	mEncryption.setCleanupMode(true);
                }
            }
        });
	}
	
	private void processIntent(Uri uri) {
		final String absoluteFilePath = uri.getPath();
	    
	    EncryptionKeyPrompt encryptionKeyPrompt = new EncryptionKeyPrompt(MainActivity.this,
				new EncryptionKeyPrompt.PromptDialogListener() {
			@Override
			public void onKeyEntered(String encryptionKey, boolean encrypt) {
				if(encrypt) {
					mEncryption.encryptFile(absoluteFilePath, encryptionKey);
				} else {
					mDecryption.decryptFile(absoluteFilePath, encryptionKey);
				}
			}
		}, absoluteFilePath);
		
		encryptionKeyPrompt.showDialog();
	}
	
	private class EncryptionListener implements View.OnClickListener {		
		public void onClick(View view) {
			if(! doEncryptionKeysMatch() || getEncryptionKey().length() == 0) {
				Toast.makeText(MainActivity.this, R.string.key_match_warning, Toast.LENGTH_LONG).show();
				return;
			}
			
			FileSelectorDialog FileOpenDialog = new FileSelectorDialog(MainActivity.this,
					new FileSelectorDialog.FileDialogListener() {
				@Override
				public void onChosenDirectory(String fileSelection) {
					mEncryption.encryptFile(fileSelection, getEncryptionKey());
				}
			}, true);
			
			FileOpenDialog.chooseFileOrDirectory();
        }
    }
	
	private class DecryptionListener implements View.OnClickListener {		
		public void onClick(View view) {
			if(! doEncryptionKeysMatch() || getEncryptionKey().length() == 0) {
				Toast.makeText(MainActivity.this, R.string.key_match_warning, Toast.LENGTH_LONG).show();
				return;
			}
			
			FileSelectorDialog FileOpenDialog = new FileSelectorDialog(MainActivity.this,
					new FileSelectorDialog.FileDialogListener() {
				@Override
				public void onChosenDirectory(String fileSelection) {
					mDecryption.decryptFile(fileSelection, getEncryptionKey());
				}
			}, false);
			
			FileOpenDialog.chooseFileOrDirectory();
        }
    }
	
	private String getEncryptionKey() {
		return ((EditText) findViewById(R.id.encryptionKey)).getText().toString();
	}
	
	private boolean doEncryptionKeysMatch() {
		if(((EditText) findViewById(R.id.encryptionKey)).getText().toString().equals(
				((EditText) findViewById(R.id.verifyKey)).getText().toString())) {
			return true;
		} else {
			return false;
		}
	}
}