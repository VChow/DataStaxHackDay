package com.kudu.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterModel {

	String url = "http://10.0.2.2:8080/KuduServer/register";
	String username, password, email;
	
	public RegisterModel(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public boolean addNewUser() throws IOException, IllegalStateException, JSONException
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("email", email));

		HttpResponse response = null;
		httppost.setEntity(new UrlEncodedFormEntity(params));
		response = httpclient.execute(httppost);
		InputStream in = response.getEntity().getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		String returnVal = null;
		while((line = reader.readLine()) != null){
			returnVal = line;
		}
		
		if(parseResult(returnVal))
			return true;
		else
			return false;
	}
	
	private boolean parseResult(String line) throws JSONException 
	{
		JSONObject result = new JSONObject(line);
		
		if(result.getString("register").equals("true"))
			return true;
		else
			return false;
	}
}