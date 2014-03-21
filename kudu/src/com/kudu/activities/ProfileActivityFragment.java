package com.kudu.activities;

import java.io.IOException;

import org.json.JSONException;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kudu.models.ProfileModel;

public class ProfileActivityFragment extends Fragment  {

    private Button btnUpdate;
	public ProfileModel profileModel = new ProfileModel();
	EditText nameText, usernameText, emailText, old_passwordText, new_passwordText, locationText, bioText;
    public ProfileActivityFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_activity_fragment,
				container, false);

		nameText = (EditText) getView().findViewById(R.id.txt_name);
		usernameText = (EditText) getView().findViewById(R.id.txt_username);
		emailText = (EditText) getView().findViewById(R.id.txt_email);
		old_passwordText = (EditText) getView().findViewById(R.id.txt_password_old);
		new_passwordText = (EditText) getView().findViewById(R.id.txt_password_new);
		locationText = (EditText) getView().findViewById(R.id.txt_location);
		bioText = (EditText) getView().findViewById(R.id.txt_biography);
		
		if (checkInternetConnection()) {
			retrieveProfile();
		} else {

			Toast.makeText(ProfileActivityFragment.this.getActivity(), "Unable to retrieve details",
					Toast.LENGTH_LONG).show();
		}
		return rootView;
	}

	public void retrieveProfile() {
		
		new Thread(new Runnable() {
			public void run() {
				try {
					String[] userProfile = new String[5];
					userProfile = profileModel.retrieveProfile();
					populateProfile(userProfile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setUpdateButtonListener() {
		btnUpdate = (Button) getView().findViewById(R.id.update);
		btnUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkInternetConnection()) {
					new Thread(new Runnable() {
						public void run() {
							try {
								ProfileModel profileModel = new ProfileModel();					
								if (profileModel.updateProfile()) {
									Toast.makeText(ProfileActivityFragment.this.getActivity(),
											"Profile Updated",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(
											ProfileActivityFragment.this.getActivity(),
											"An error occured while updating your profile",
											Toast.LENGTH_LONG).show();
								}
							} catch (IllegalStateException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
	}

	public void populateProfile(String[] userProfile) {
		usernameText.setText(userProfile[0]);
		nameText.setText(userProfile[1]);
		bioText.setText(userProfile[3]);
		emailText.setText(userProfile[4]);
		locationText.setText(userProfile[5]);
	}

	public void updateProfileModel(){
		String username = usernameText.getText().toString();
		String name = nameText.getText().toString();
		String password_old = old_passwordText.getText().toString();
		String password_new = new_passwordText.getText().toString();
		String email = emailText.getText().toString();
		String location = locationText.getText().toString();
		String bio = bioText.getText().toString();
		
		profileModel.setUserDetails(name, username, password_old, password_new, email, location, bio);
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
