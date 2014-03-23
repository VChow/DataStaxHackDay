package com.kudu.activities;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AddContactDialog extends DialogFragment {
	//private final Button addButton;
	public AddContactDialog() {
	}
	
    public static AddContactDialog newInstance() {
        AddContactDialog frag = new AddContactDialog();
        return frag;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = getActivity().getLayoutInflater().inflate(R.layout.add_contact, null);
    	final Button addButton = (Button) getActivity().findViewById(R.id.add_button);
    	
    	addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkInternetConnection()) {
					//new Thread(new Runnable() {
						//public void run(){
							Log.v("shithousedsklafhsdalfd", "fucktard");
						//}
					//}).start();admin	admin
				}
			}
    	});
    	return view;
    }
    
    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
    	View view = getActivity().getLayoutInflater().inflate(R.layout.add_contact, null);
    	
    	adb.setView(view);
    	adb.setTitle(getString(R.string.add_contact_message));
    	final Button addButton = (Button) getActivity().findViewById(R.id.add_button);
    	
    	addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkInternetConnection()) {
					//new Thread(new Runnable() {
						//public void run(){
							Log.v("shithousedsklafhsdalfd", "fucktard");
						//}
					//}).start();
				}
			}
    	});
    	return adb.create();
    }*/
    
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
