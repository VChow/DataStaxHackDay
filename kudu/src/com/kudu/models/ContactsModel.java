package com.kudu.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

import android.util.Log;


public class ContactsModel {

	//String url = "http://10.0.2.2:8080/KuduServer/contacts";
	String url = "http://10.0.3.2:8080/KuduServer/contacts";
	String retrieve = "false";
	String add = "false";
	LinkedList<String> temp = new LinkedList<String>();
	
	public LinkedList<String> getContacts(String username) throws IOException, IllegalStateException, JSONException {
		retrieve = "true";
		
		//request
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("retrieve", retrieve));
		params.add(new BasicNameValuePair("username", username));
		
		//response
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
    	JSONArray values = jsonObject.getJSONArray("contactsValues");
    	for(int i = 0; i < values.length(); i++) {
    		temp.add(values.getString(i)); 
    	}
    	retrieve = "true";
		return temp;
	}
	
	public boolean addContact(String contact, String username) throws IOException, IllegalStateException, JSONException {
		add = "true";
		
		//request
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("add", add));
		params.add(new BasicNameValuePair("retrieve", retrieve));
		params.add(new BasicNameValuePair("contact", contact));
		params.add(new BasicNameValuePair("username", username));
		
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
		
		add = "false";
		if(parseResult(returnVal)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean parseResult(String line) throws JSONException 
	{
		JSONObject result = new JSONObject(line);
		if(result.getString("contactAdded").equals("true"))
			return true;
		else
			return false;
	}		
}
