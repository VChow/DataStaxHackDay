package com.example.kudu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private Button btnRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		final ImageView imgView;
		
		imgView=(ImageView)findViewById(R.id.logo);
		imgView.setImageResource(R.drawable.login_logo);
	}
	
	public void setRegisterButtonListener(){
		btnRegister = (Button) findViewById(R.id.register);
		btnRegister.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText usernameEditText = (EditText)findViewById(R.id.username);
			EditText emailEditText = (EditText)findViewById(R.id.email);
			EditText password1EditText = (EditText)findViewById(R.id.password_1);
			EditText password2EditText = (EditText)findViewById(R.id.password_2);
			String btnUsernameStr = usernameEditText.getText().toString();
			String btnEmailStr = emailEditText.getText().toString();
			String btnPassword1Str = password1EditText.getText().toString();
			String btnPassword2Str = password2EditText.getText().toString();
		}
		});
	}
}