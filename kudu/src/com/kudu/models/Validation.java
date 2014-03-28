package com.kudu.models;

import java.util.regex.Pattern;

import android.app.Activity;
import android.widget.EditText;

public class Validation {
	private Activity activity;
	private final String regex = "^[a-zA-Z0-9]{4,10}$";
	public Validation(Activity activity) {
		this.activity = activity;
	}
	
	public boolean validate(final EditText editText) {
		String text = editText.getText().toString().trim();
        if(!hasText(editText)) 
        	return false;
        if(!Pattern.matches(regex, text)) {
        	activity.runOnUiThread(new Runnable(){
        		public void run() {
        			editText.setError("Invalid character entered.");
        		}
        	});
            return false;
        }
        return true;
	}
	
	public boolean hasText(final EditText editText) {
		String text = editText.getText().toString().trim();
		if (text.length() == 0) {
			activity.runOnUiThread(new Runnable(){
        		public void run() {
        			editText.setError("Please enter something.");
        		}
        	});
			return false;
		}
		return true;
	}
}
