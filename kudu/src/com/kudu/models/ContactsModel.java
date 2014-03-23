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

import android.util.JsonReader;

public class ContactsModel {

	String url = "http://10.0.2.2:8080/KuduServer/contacts";
	String user_id;
	List contacts = new ArrayList();
	
	String retrieve = "false";
	String insert = "false";
	
	/*public ContactsModel(UUID uuid){
		this.user_id = uuid.toString();
	}*/
	
	public ContactsModel(){
		
	}
	
	public void setUUID(UUID user_id){
		this.user_id = user_id.toString();
	}
	
	public List retrieveContacts() throws IOException, IllegalStateException, JSONException
	{
		retrieve = "true";
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("retrieve", retrieve));
		params.add(new BasicNameValuePair("uuid", user_id));
		
		HttpResponse response = null;
		httppost.setEntity(new UrlEncodedFormEntity(params));
		response = httpclient.execute(httppost);
		InputStream in = response.getEntity().getContent();
		
		/*
		 * http://developer.android.com/reference/android/util/JsonReader.html
		 */
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try 
		{
			return readContactsArray(reader);
		} 
		catch (Exception e){}

		reader.close();				
		retrieve = "false";
		
		return contacts;
	}
	
	
	public List readContactsArray(JsonReader reader) throws IOException {
		List contactsList = new ArrayList();
		
		reader.beginArray();
	     while (reader.hasNext()) {
	       contactsList.add(readContact(reader));
	     }
	     reader.endArray();
		
		return contactsList;
	}
	
	
	public String readContact(JsonReader reader) throws IOException {
		String name = null;
		
		reader.beginObject();
		
		while (reader.hasNext()){
			name = reader.nextName();
		}
		
		reader.endObject();
		
		return name;
	}
	
	public boolean addContact(String contactname) throws IOException, IllegalStateException, JSONException
	{
		insert = "true";
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("insert", insert));
		params.add(new BasicNameValuePair("uuid", user_id));
		params.add(new BasicNameValuePair("contactname", contactname));
			
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
    	
    	if(parseResult(returnVal)) {
			return true;
		}
		else {
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
