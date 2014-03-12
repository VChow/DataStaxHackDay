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
import android.widget.Toast;

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
				
				//Used for testing
				Toast.makeText(MainActivity.this,
						"On Button Click : " + "\n" + 
						btnUsernameStr + "\n" + 
					    btnPasswordStr, Toast.LENGTH_LONG).show();
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
	
}
