package com.kuduapp.models;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

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
		//instantiates httpclient to make request
	    DefaultHttpClient httpclient = new DefaultHttpClient();

	    //url with the post data
	    HttpPost httpost = new HttpPost("http://10.0.2.2:8080/KuduServer/login");
	    
	    JSONObject json = new JSONObject();
	    try {
			json.accumulate("username", username);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    StringEntity se = null;
		try {
			se = new StringEntity(json.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    httpost.setEntity(se);
	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");
	    

	    //Handles what is returned from the page 
	    ResponseHandler responseHandler = new BasicResponseHandler();
	    try {
	    	HttpResponse httpResponse = httpclient.execute(httpost, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
