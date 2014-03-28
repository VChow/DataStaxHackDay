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
	//String url = "http://10.0.3.2:8080/KuduServer/profile";
	String url = "http://kududb.cloudapp.net:8080/KuduServer/profile";
	String name, username, password, email, location, bio, id;
	String update = "false";
	String retrieve = "false";
	
	public ProfileModel(){}
	
	public ProfileModel(String name, String username, String password, 
			String email, String location, String bio, String id){
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.location = location;
		this.bio = bio;
		this.id = id;
	}
	
	public String getName() { return name; }
	public String getUsername() { return username; }   
    public String getPassword() { return password; }
    public String getEmail() { return email; }    
    public String getLocation() { return location; }   
    public String getBio() { return bio; }
    public String getID() { return id; }
	
	public void setName(String name) { this.name = name; }
	public void setUsername(String username) { this.username = username; }
	public void setEmail(String email) { this.email = email; }
	public void setLocation(String location) { this.location = location; }
	public void setBio(String bio) { this.bio = bio; }
	public void setID(String id) { this.id = id; }
	public void setPassword(String password) { this.password = password; }
    
	public boolean updateProfile() throws IOException, IllegalStateException, JSONException
    {
		update = "true";
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
		params.add(new BasicNameValuePair("id", id));
		
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
		
		if(parseResult(returnVal)) {
			update = "false";
			return true;
		} else {
			update = "false";
			return false;
		}
    }
    
    public void retrieveProfile() throws IOException, IllegalStateException, JSONException
    {	  	
    	retrieve = "true";   	
    	HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("retrieve", retrieve));
		params.add(new BasicNameValuePair("update", update));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("id", id));
		
		HttpResponse response = null;
		httppost.setEntity(new UrlEncodedFormEntity(params));
		response = httpclient.execute(httppost);
		InputStream in = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		String line = null;
		StringBuilder builder = new StringBuilder();	
    	
    	while((line = reader.readLine()) != null){
			builder.append(line);
		}
    	
    	JSONObject jsonObject = new JSONObject(builder.toString());
    	JSONArray userDetails = jsonObject.getJSONArray("profileRetrieve");
    	setUsername(userDetails.getString(0));
    	System.out.println(getUsername());
    	setID(userDetails.getString(1));
    	setName(userDetails.getString(2));
    	setEmail(userDetails.getString(3));
    	setBio(userDetails.getString(4));
    	setLocation(userDetails.getString(5));
    	setPassword(userDetails.getString(6));
    	retrieve = "false";
    }
    
    private boolean parseResult(String line) throws JSONException {
		JSONObject result = new JSONObject(line);
		if(result.getString("update").equals("true"))
			return true;
		else
			return false;
	}
}
