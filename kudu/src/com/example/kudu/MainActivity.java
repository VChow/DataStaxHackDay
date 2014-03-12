package com.example.kudu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private Button btnLogin;
	private Button btnRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		
		final ImageView imgView;
		
		imgView=(ImageView)findViewById(R.id.logo);
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

	public void setLoginButtonListener(){
		btnLogin = (Button) findViewById(R.id.login);
		btnLogin.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				EditText usernameEditText = (EditText)findViewById(R.id.username);
				EditText passwordEditText = (EditText)findViewById(R.id.password);
				String btnUsernameStr = usernameEditText.getText().toString(); 
				String btnPasswordStr = passwordEditText.getText().toString();

				Login login = new Login();
				login.setUsername(btnUsernameStr);
				login.setPassword(btnPasswordStr);
				
				//Testing Purposes
				Log.d("Debawg", "Username: " + login.getUsername());
				Log.d("Debawg", "Password: " + login.getPassword());
				
				if(validateLogin(btnUsernameStr, btnPasswordStr)){
					Intent myIntent = new Intent(MainActivity.this, ConvoOverviewActivity.class);
					MainActivity.this.startActivity(myIntent);
				}else{
					passwordEditText.setError("Incorrect Username or Password.");
				}			
			}
		});
	}
	
	public void setRegisterButtonListener(){
		btnRegister = (Button) findViewById(R.id.register);
		btnRegister.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
	}
	
	public boolean validateLogin(String username, String password){
		
		boolean isValid = false;
		
			if(username.equals("1")){
				isValid = true;
			}
			
			//Check if Username + Password are inside the Cassandra Login Column Family
			//If yes -> isValid = true
			
		
		return isValid;
	}
}
