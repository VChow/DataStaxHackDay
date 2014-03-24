package com.kudu.activities;

import java.io.IOException;

import org.json.JSONException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kudu.models.ContactsModel;

public class AddContactDialog extends DialogFragment {
	String username = "admin";
	public AddContactDialog() {}
	
    public static AddContactDialog newInstance() {
    	AddContactDialog frag = new AddContactDialog();
        return frag;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
    	View view = getActivity().getLayoutInflater().inflate(R.layout.add_contact, null);
    	adb.setView(view);
    	adb.setTitle(getString(R.string.add_contact_message));
    	
    	final TextView errorText = (TextView) view.findViewById(R.id.textView2);
    	final TextView successText = (TextView) view.findViewById(R.id.textView3);
    	errorText.setVisibility(View.INVISIBLE);
    	successText.setVisibility(View.INVISIBLE);
    	final EditText editText = (EditText) view.findViewById(R.id.add_contact_txtb);
    	final Button addContact = (Button) view.findViewById(R.id.add_button);
    	final Button cancelBtn = (Button) view.findViewById(R.id.cancel_button);
    	addContact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (checkInternetConnection()) {
					new Thread(new Runnable() {
						public void run(){
							String contactToAdd = editText.getText().toString();
							ContactsModel cm = new ContactsModel();
							try {
								final boolean added = cm.addContact(contactToAdd, username);
								getActivity().runOnUiThread(new Runnable(){
								    public void run(){
								    	if(added == true) {
											errorText.setVisibility(View.GONE);
											successText.setVisibility(View.VISIBLE);
										} else if(added == false) {
											errorText.setVisibility(View.VISIBLE);
											successText.setVisibility(View.GONE);
										}
								    }
								});
							} catch (IllegalStateException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
    	});
    	
    	cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Fragment prev = getFragmentManager().findFragmentByTag("dialog");
			    if (prev != null) {
			        DialogFragment df = (DialogFragment) prev;
			        df.dismiss();
			    }
			}
    	});
    	return adb.create();
    }

    private boolean checkInternetConnection() {
		ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
}
