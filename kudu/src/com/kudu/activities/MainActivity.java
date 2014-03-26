package com.kudu.activities;
 
import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.kudu.models.DatabaseHelper;
import com.kudu.models.LoginModel;
import com.kudu.models.Session;

public class MainActivity extends Activity {

	static public DatabaseHelper db;
	private Button btnLogin, btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new DatabaseHelper(this);
		final ImageView imgView;

		imgView = (ImageView) findViewById(R.id.logo);
		imgView.setImageResource(R.drawable.login_logo);

		db.createTables();
		
		setLoginButtonListener();
		setRegisterButtonListener();
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * When the user returns to the main activity.
	 */
	/*@Override
	protected void onResume() {
		Log.v("IT", "IT");
		Session sess = new Session();
		sess = db.checkSessionExists();
		
		//Log.v("CHECK", sess.getUsername());
		if((sess.getUsername().equals(null))) {
			Intent myIntent = new Intent(MainActivity.this,
					ConversationOverviewActivity.class);
			MainActivity.this.startActivity(myIntent);
			Log.v("IT", "MIET");
		}
		super.onResume();
	}*/
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
        .setTitle("Really Exit?")
        .setMessage("Are you sure you want to exit?")
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.super.onBackPressed();
			}
		}).create().show();
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
								String uuid = login.checkLogin();
								if(uuid != null) {
									db.getSession(uuid, username);
									Intent myIntent = new Intent(MainActivity.this,
											ConversationOverviewActivity.class);
									MainActivity.this.startActivity(myIntent);
									finish();
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
