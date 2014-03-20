package com.kudu.activities;


import java.io.IOException;

import org.json.JSONException;

import com.kudu.models.ProfileModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends Activity{

	private Button btnUpdate;
	public final EditText nameText = (EditText) findViewById(R.id.txt_name);
	public final EditText usernameText = (EditText) findViewById(R.id.txt_username);
	public final EditText emailText = (EditText) findViewById(R.id.txt_email);
	public final EditText old_passwordText = (EditText) findViewById(R.id.txt_password_old);
	public final EditText new_passwordText = (EditText) findViewById(R.id.txt_password_new);
	public final EditText locationText = (EditText) findViewById(R.id.txt_location);
	public final EditText bioText = (EditText) findViewById(R.id.txt_biography);
	
	public String username = usernameText.getText().toString();
	public String name = nameText.getText().toString();
	public String password_old = old_passwordText.getText().toString();
	public String password_new = new_passwordText.getText().toString();
	public String email = emailText.getText().toString();
	public String location = locationText.getText().toString();
	public String bio = bioText.getText().toString();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		if(checkInternetConnection()){
			pullDetails();
		}else{
			Toast.makeText(ProfileActivity.this,"Unable to retrieve details",Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void pullDetails(){
		String username= "", name= "", email= "", location= "", bio = "";
		//Pull deets from the DB and fill in respective textViews				
	}
	
	public void setUpdateButtonListener() {
		btnUpdate = (Button) findViewById(R.id.update);
		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(checkInternetConnection()){
					new Thread(new Runnable(){
						public void run(){
							/*final EditText nameText = (EditText) findViewById(R.id.txt_name);
							final EditText usernameText = (EditText) findViewById(R.id.txt_username);
							final EditText emailText = (EditText) findViewById(R.id.txt_email);
							final EditText old_passwordText = (EditText) findViewById(R.id.txt_password_old);
							final EditText new_passwordText = (EditText) findViewById(R.id.txt_password_new);
							final EditText locationText = (EditText) findViewById(R.id.txt_location);
							final EditText bioText = (EditText) findViewById(R.id.txt_biography);
							
							String username = usernameText.getText().toString();
							String name = nameText.getText().toString();
							String password_old = old_passwordText.getText().toString();
							String password_new = new_passwordText.getText().toString();
							String email = emailText.getText().toString();
							String location = locationText.getText().toString();
							String bio = bioText.getText().toString();*/
							
							try{
								ProfileModel profileModel = new ProfileModel();
								if(profileModel.updateDetails()){
									//Doesn't actually check if profile was successfully updated.
									Toast.makeText(ProfileActivity.this,
											"Profile Updated", Toast.LENGTH_LONG).show();
								}else
								{
									Toast.makeText(ProfileActivity.this,
											"An error occured while updating your profile", Toast.LENGTH_LONG).show();
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
	
	private boolean checkInternetConnection() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
}
