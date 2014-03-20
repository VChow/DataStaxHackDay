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

import android.util.Log;

public class LoginModel {
	
	String url = "http://10.0.2.2:8080/KuduServer/login";
	String username, password;
	
	public LoginModel(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	public String encryptPassword()
	{
		password = "#fuckTom";
		return password;
	}
	
	public boolean checkLogin() throws IOException, IllegalStateException, JSONException
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));

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
		if(result.getString("login").equals("true"))
			return true;
		else
			return false;
	}
}
