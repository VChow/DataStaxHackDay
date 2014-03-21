package com.kudu.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileModel {
	
	String url = "http://10.0.2.2:8080/KuduServer/profile";
	String name, username, password_old, password_new, email, location, bio;
	UUID id;
	String update = "false";
	String retrieve = "false";
	
	public ProfileModel(){}
	
	public ProfileModel(String name, String username, String password_old, String password_new,
			String email, String location, String bio, UUID id){
		this.name = name;
		this.username = username;
		this.password_old = password_old;
		this.password_new = password_new;
		this.email = email;
		this.location = location;
		this.bio = bio;
		this.id = id;
	}
	
	public String getName() { return name; }
	public String getUsername() { return username; }   
    public String getPassword_old() { return password_old; }
    public String getPassword_new() { return password_new; }
    public String getEmail() { return email; }    
    public String getLocation() { return location; }   
    public String getBio() { return bio; }
    public UUID getID() { return id; }
	
	public void setName(String name) { this.name = name; }
	public void setUsername(String username) { this.username = username; }
	public void setPassword(String password_old) { this.password_old = password_old; }
	public void setPassword_new(String password_new){ this.password_new = password_new; }
	public void setEmail(String email) { this.email = email; }
	public void setLocation(String location) { this.location = location; }
	public void setbio(String bio) { this.bio = bio; }
	public void setID(UUID id) { this.id = id; }
    
	public boolean updateProfile() throws IOException, IllegalStateException, JSONException
    {
		update = "true";
    	HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("update", update));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password_old", password_old));
		params.add(new BasicNameValuePair("password_new", password_new));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("location", location));
		params.add(new BasicNameValuePair("bio", bio));
		
		HttpResponse response = null;
		httppost.setEntity(new UrlEncodedFormEntity(params));
		response = httpclient.execute(httppost);
		InputStream in = response.getEntity().getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		String returnVal = null;		
		update = "false";
		
		while((line = reader.readLine()) != null){
			returnVal = line;
		}
		
		if(parseResult(returnVal))
			return true;
		else
			return false;
    }
    
    public String[] retrieveProfile() throws IOException, IllegalStateException, JSONException
    {
    	String[] userProfile = new String[5];   	
    	retrieve = "true";
    	
    	HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("retrieve", retrieve));
		
		HttpResponse response = null;
		httppost.setEntity(new UrlEncodedFormEntity(params));
		response = httpclient.execute(httppost);
		InputStream in = response.getEntity().getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		String returnVal = null;		
    	retrieve = "false";
    	
    	while((line = reader.readLine()) != null){
			returnVal = line;
		}
    	return userProfile;
    }
    
    private boolean parseResult(String line) throws JSONException {
		JSONObject result = new JSONObject(line);
		if(result.getString("profileUpdate").equals("true"))
			return true;
		else
			return false;
	}
    
    private boolean parseResult(String[] profile) {
    	JSONArray userProfile = new JSONArray();
    	return true;
    }
}
