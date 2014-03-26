package com.kudu.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
		GetConversationIDThread getConvID = new GetConversationIDThread(username, friendName);
		getConvID.start();
		try{
			getConvID.join();
		} catch (InterruptedException e)
		{}
		this.conversationID = getConvID.getConversationID();
		
		Log.e("convID", conversationID);
		//this.convKey = db.getKey(conversationID);
	}
	
	public String getConversationID() throws ClientProtocolException, IOException, JSONException
	{
		String conversationID;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("friendID", friendName));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("getID", "true"));

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
		
		JSONObject json = new JSONObject(sb.toString());
		conversationID = json.getString("convID");
		
		return conversationID;
	}

	public LinkedHashMap<String, String> getConversation() throws ClientProtocolException, IOException, JSONException
	{
		LinkedHashMap<String, String> conversation = new LinkedHashMap<String, String>();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("convID", conversationID));

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
		if(!jsonText.isEmpty())
		{
			Log.d("text", jsonText);
			conversation = parseJSON(jsonText);

		}
		return conversation;
	}

	public LinkedHashMap<String, String> parseJSON(String jsonText)
	{
		LinkedHashMap<String, String> conversation = new LinkedHashMap<String, String>();
		String[] entries = jsonText.split(",");
		entries[0] = entries[0].substring(1);
		entries[entries.length-1] = entries[entries.length-1].substring(0, entries[entries.length-1].length()-1);

		for(int i = 0; i< entries.length; i++)
		{
			String[] subEntries = entries[i].split(":", 2);
			for (int j = 0; j < subEntries.length; j++)
			{
				subEntries[j] = subEntries[j].substring(1);
				subEntries[j] = subEntries[j].substring(0, subEntries[j].length()-1);
			}
			conversation.put(subEntries[0], subEntries[1]);
		}

		return conversation;
	}

	public void sendMessage(String message) throws ClientProtocolException, IOException
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("message", message));
		params.add(new BasicNameValuePair("convID", conversationID));

		httppost.setEntity(new UrlEncodedFormEntity(params));
		httpclient.execute(httppost);
		
	}
}
