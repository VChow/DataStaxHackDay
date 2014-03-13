package com.kuduapp.models;

import android.content.Intent;

import com.example.kudu.MainActivity;
import com.example.kudu.RegisterActivity;

public class LoginModel {
	

	String username, password;
	
	public LoginModel(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	public String encryptPassword()
	{
		password = password;
		
		return password;
	}
	public boolean checkLogin()
	{
		
		return true;
	}
}
