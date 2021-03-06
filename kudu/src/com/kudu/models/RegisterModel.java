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

	//String url = "http://10.0.2.2:8080/KuduServer/register";
	//String url = "http://10.0.3.2:8080/KuduServer/register";
	String url = "http://kududb.cloudapp.net:8080/KuduServer/register";
	String username, password, email;
	
	public RegisterModel(String username, String password, String email) {
		this.username = username;
		try {
			this.password = ShaThis.getSha(password);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.email = email;
	}
	
	public String addNewUser() throws IOException, IllegalStateException, JSONException
	{
		String uuid = null;
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
		
		JSONObject result = new JSONObject(returnVal);
		uuid = result.getString("register");
		return uuid;
	}
}