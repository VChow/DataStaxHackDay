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

public class ProfileModel {
	
	String url = "http://10.0.2.2:8080/KuduServer/profile";
	String name, username, password, email, location, bio;
	String update = null;
	String retrieve = null;
	
	public ProfileModel(){
		
	}
	
	public ProfileModel(String name, String username, String password,
			String email, String location, String bio){
		
	}
	
	//==========================================================
	public void setUserDetails(String name, String username, String password,
			String email, String location, String bio) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.location = location;
		this.bio = bio;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setbio(String bio) {
		this.bio = bio;
	}

    //========================================================== 
	public String getName() {
		return name;
	}
	public String getUsername() {
    	return username; 
    }   
    public String getPassword() { 
    	return password; 
    }   
    public String getEmail() { 
    	return email; 
    }    
    public String getLocation() { 
    	return location; 
    }   
    public String getBio() { 
    	return bio; 
    }
    public String[] getDetails(){
    	String[] userDetails = new String[6];
    	
    	userDetails[0] = name;
    	userDetails[1] = username;
    	userDetails[2] = password;
    	userDetails[3] = email;
    	userDetails[4] = location;
    	userDetails[5] = bio;
    	
    	return userDetails;
    }
    
    //==========================================================
    
    public String[] retrieveProfile() throws IOException, IllegalStateException, JSONException{
    	String[] userProfile = new String[5];   	
    	retrieve = "1";
    	
    	/*
    	 * Get shiz from db and put into array
    	 */
    	
    	retrieve = null;
    	return userProfile;
    }
    
    public boolean updateProfile() throws IOException, IllegalStateException, JSONException
    {
    	update = "1"; //Make it not null.
    	/*
    	 * get variables from app and put in global params.
    	 */
    	HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("update", update));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
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
		update = null; //Reset back to null for next use.
		
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
		
		if(result.getString("profileUpdate").equals("true"))
			return true;
		else
			return false;
	}
    
}
