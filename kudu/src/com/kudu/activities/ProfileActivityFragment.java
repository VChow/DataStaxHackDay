package com.kudu.activities;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kudu.models.ProfileModel;
import com.kudu.models.Session;
import com.kudu.models.ShaThis;

public class ProfileActivityFragment extends Fragment {

    private Button btnUpdate;
    private EditText nameText, usernameText, emailText, newPasswordText, oldPasswordText, locText, bioText;
    private Context context;
    private final ProfileModel pm = new ProfileModel();
    private String uuid, username;
    
    public ProfileActivityFragment() {}
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.profile_activity_fragment,container, false);
		context = container.getContext();
		
		Session session = new Session();
		session = MainActivity.db.checkSessionExists();
		uuid = session.getUuid();
		username = session.getUsername();
		pm.setID(uuid);
		pm.setUsername(username);
		
		
		btnUpdate = (Button) rootView.findViewById(R.id.update_btn);
		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				if(checkInternetConnection()) {
					new Thread(new Runnable() {
						public void run() {
							updateProfile(rootView);
						}
					}).start();
				}
			}
		});
		
		if(checkInternetConnection()) {
			new Thread(new Runnable() {
				public void run() {
					retrieveProfile(rootView);
				}
			}).start();
		}
		return rootView;
	}
	
	public void updateProfile(View view) {
		nameText = (EditText) view.findViewById(R.id.txt_name);
		usernameText = (EditText) view.findViewById(R.id.txt_username);
		emailText = (EditText) view.findViewById(R.id.txt_email);
		newPasswordText = (EditText) view.findViewById(R.id.txt_password_new);
		oldPasswordText = (EditText) view.findViewById(R.id.txt_password_old);
		locText = (EditText) view.findViewById(R.id.txt_location);
		bioText = (EditText) view.findViewById(R.id.txt_biography);

		String name = nameText.getText().toString();
		String username = usernameText.getText().toString();
		String email = emailText.getText().toString();
		String location = locText.getText().toString();
		String bio = bioText.getText().toString();
		
		Log.v("Email: ", email);

		ProfileModel profileModel = new ProfileModel(name, username, email, location, bio, uuid);
		if(newPasswordText.getText().toString().equals(oldPasswordText.getText().toString())) {
			if(!newPasswordText.getText().toString().equals("")) { 
				try {
					String password = ShaThis.getSha(newPasswordText.getText().toString());
					profileModel.setPassword(password);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		} else {
			((Activity)context).runOnUiThread(new Runnable(){
			    public void run(){
			    	newPasswordText.setError("Passwords do not match.");
			    }
			});
		}
		try {
			profileModel.updateProfile();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void retrieveProfile(View view) {
		try {
			pm.retrieveProfile();
			nameText = (EditText) view.findViewById(R.id.txt_name);
			usernameText = (EditText) view.findViewById(R.id.txt_username);
			emailText = (EditText) view.findViewById(R.id.txt_email);
			locText = (EditText) view.findViewById(R.id.txt_location);
			bioText = (EditText) view.findViewById(R.id.txt_biography);
			
			((Activity)context).runOnUiThread(new Runnable(){
			    public void run(){
			    	System.out.println(pm.getUsername());
			    	nameText.setText(pm.getName());
					usernameText.setText(pm.getUsername());
					emailText.setText(pm.getEmail());
					locText.setText(pm.getLocation());
					bioText.setText(pm.getBio());
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
