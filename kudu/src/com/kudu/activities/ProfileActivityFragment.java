package com.kudu.activities;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONException;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kudu.models.ProfileModel;

public class ProfileActivityFragment extends Fragment {

    private Button btnUpdate;
    private EditText nameText, usernameText, emailText, oldPasswordText, newPasswordText, locText, bioText;

    public ProfileActivityFragment() {}
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.profile_activity_fragment,container, false);
		
		btnUpdate = (Button) rootView.findViewById(R.id.update_btn);
		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				if(checkInternetConnection()) {
					new Thread(new Runnable() {
						public void run() {
							updateOnClick(rootView);
						}
					}).start();
				}
			}
		});
		return rootView;
	}
	
	public void updateOnClick(View view) {
		nameText = (EditText) view.findViewById(R.id.txt_name);
		usernameText = (EditText) view.findViewById(R.id.txt_username);
		emailText = (EditText) view.findViewById(R.id.txt_email);
		oldPasswordText = (EditText) view.findViewById(R.id.txt_password_old);
		newPasswordText = (EditText) view.findViewById(R.id.txt_password_new);
		locText = (EditText) view.findViewById(R.id.txt_location);
		bioText = (EditText) view.findViewById(R.id.txt_biography);
		//UUID uuid;

		String name = nameText.getText().toString();
		String username = usernameText.getText().toString();
		String email = emailText.getText().toString();
		String old_Password = oldPasswordText.getText().toString();
		String new_Password = newPasswordText.getText().toString();
		String location = locText.getText().toString();
		String bio = bioText.getText().toString();

		/*ProfileModel profileModel = new ProfileModel(name, username,
				old_Password, new_Password, email, location, bio, uuid);
		try {
			profileModel.updateProfile();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
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
