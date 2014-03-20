package com.kudu.activities;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kudu.models.LoginModel;

public class MainActivity extends Activity {

	private Button btnLogin, btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ImageView imgView;

		imgView = (ImageView) findViewById(R.id.logo);
		imgView.setImageResource(R.drawable.login_logo);

		setLoginButtonListener();
		setRegisterButtonListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void setLoginButtonListener() {
		btnLogin = (Button) findViewById(R.id.login);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkInternetConnection()) {
					new Thread(new Runnable() {
						public void run(){
							final EditText usernameEditText = (EditText) findViewById(R.id.username);
							final EditText passwordEditText = (EditText) findViewById(R.id.password);
							String username = usernameEditText.getText().toString();
							String password = passwordEditText.getText().toString();

							LoginModel login = new LoginModel(username, password);

							try {
								if(login.checkLogin()) {
									Intent myIntent = new Intent(MainActivity.this,
											ConversationOverviewActivity.class);
									MainActivity.this.startActivity(myIntent);
								}
								else {
									MainActivity.this.runOnUiThread(new Runnable(){
									    public void run(){
									    	passwordEditText.setError("Incorrect username or password.");
									    	usernameEditText.setError("Incorrect username or password");
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
						}
					}).start();
				} else {
					Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void setRegisterButtonListener() {
		btnRegister = (Button) findViewById(R.id.register);
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this,
						RegisterActivity.class);
				MainActivity.this.startActivity(myIntent);
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
