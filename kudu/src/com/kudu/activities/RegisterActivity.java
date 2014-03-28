package com.kudu.activities;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kudu.models.RegisterModel;
import com.kudu.models.Validation;

public class RegisterActivity extends Activity {

	private Button btnRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		final ImageView imgView;
		
		imgView=(ImageView)findViewById(R.id.logo);
		imgView.setImageResource(R.drawable.login_logo);
		
		setRegisterButtonListener();
	}
	
	public void setRegisterButtonListener(){
		btnRegister = (Button) findViewById(R.id.register);
		btnRegister.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				if (checkInternetConnection()) {
					new Thread(new Runnable() {
						public void run() {
							final EditText usernameEditText = (EditText) findViewById(R.id.username);
							final EditText emailEditText = (EditText) findViewById(R.id.email);
							final EditText password1EditText = (EditText) findViewById(R.id.password_1);
							final EditText password2EditText = (EditText) findViewById(R.id.password_2);
							String username = usernameEditText.getText().toString();
							String email = emailEditText.getText().toString();
							String password_1 = password1EditText.getText().toString();
							String password_2 = password2EditText.getText().toString();
							
							Validation v = new Validation(RegisterActivity.this);
							if(v.validate(usernameEditText) && (v.validate(emailEditText) 
									&& v.validate(password1EditText) && v.validate(password2EditText))) {
							
								if (password_1.equals(password_2)) {
									RegisterModel newUser = new RegisterModel(
											username, password_1, email);
									try {
										String uuid = newUser.addNewUser();
										Log.v("FUCK",uuid);
										if(!(uuid.equals("null"))) {
											MainActivity.db.insertSession(uuid, username);
											Intent myIntent = new Intent(RegisterActivity.this,
												ConversationOverviewActivity.class);
											RegisterActivity.this.startActivity(myIntent);
										} else {
											RegisterActivity.this.runOnUiThread(new Runnable(){
												public void run(){
													usernameEditText.setError("Username already exists");
												}
											});
										}
									} catch (IllegalStateException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									} catch (JSONException e) {
										e.printStackTrace();
									}
								} else {
									RegisterActivity.this.runOnUiThread(new Runnable(){
										public void run(){
											password2EditText.setError("The two password's do not match.");
										}
									});	
								}
							}
						}
					}).start();
				} else {
					Toast.makeText(RegisterActivity.this,
							"No Internet Connection", Toast.LENGTH_LONG).show();
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
