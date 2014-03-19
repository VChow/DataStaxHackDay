package com.kudu.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.kudu.models.*;

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
				EditText usernameEditText = (EditText)findViewById(R.id.username);
				EditText emailEditText = (EditText)findViewById(R.id.email);
				EditText password1EditText = (EditText)findViewById(R.id.password_1);
				EditText password2EditText = (EditText)findViewById(R.id.password_2);
				String username = usernameEditText.getText().toString();
				String email = emailEditText.getText().toString();
				String password_1 = password1EditText.getText().toString();
				String password_2 = password2EditText.getText().toString();
			
				if(password_1.equals(password_2)) {
					RegisterModel newUser = new RegisterModel(username, password_1, email);
					
					Intent myIntent = new Intent(RegisterActivity.this,
							ConversationOverviewActivity.class);
					RegisterActivity.this.startActivity(myIntent);
				}
				else {
					password2EditText.setError("The two password's do not match.");
				}	
			}
		});
	}
}
