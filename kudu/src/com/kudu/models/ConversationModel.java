package com.kudu.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ConversationModel {

	//String url = "http://10.0.2.2:8080/KuduServer/conversation";
	String url = "http://10.0.3.2:8080/KuduServer/conversation";
	String conversationID, friendName, username, convKey;
	
	public ConversationModel(String friendName, String username){
		this.friendName = friendName;
		this.username = username;
		//this.convKey = db.getKey(conversationID);
	}
	
	public LinkedHashMap<String, String> getConversation() throws ClientProtocolException, IOException, JSONException
	{
		LinkedHashMap<String, String> conversation = new LinkedHashMap<String, String>();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("friendID", friendName));
		params.add(new BasicNameValuePair("username", username));

		HttpResponse response = null;
		httppost.setEntity(new UrlEncodedFormEntity(params));
		response = httpclient.execute(httppost);
		InputStream in = response.getEntity().getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp = reader.read()) != -1)
		{
			sb.append((char)cp);
		}
		in.close();
		
		String jsonText = sb.toString();
		
		JSONObject jOb = new JSONObject(jsonText);
		
		Iterator<?> keys = jOb.keys(); 
		while (keys.hasNext())
		{
			String key = (String)keys.next();
			String value = jOb.getString(key);
			Log.e(key, value);
			conversation.put(key, value);
			
		}
		
		return conversation;
	}
}
