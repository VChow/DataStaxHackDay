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
	String username, password, email, location, bio;
	
	public ProfileModel(){
		
	}
	
	//==========================================================
	public void setUserDetails(String username, String password, String email, String location, String bio){
		this.username = username;
		this.password = password;
		this.email = email;
		this.location = location;
		this.bio = bio;
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
    	String[] userDetails = new String[5];
    	  	
    	userDetails[1] = username;
    	userDetails[2] = password;
    	userDetails[3] = email;
    	userDetails[4] = location;
    	userDetails[5] = bio;
    	
    	return userDetails;
    }
    
    //==========================================================
    
    public boolean pullDetails() throws IOException, IllegalStateException, JSONException
    {
    	HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
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
		
		if(result.getString("update").equals("true"))
			return true;
		else
			return false;
	}
    
}
