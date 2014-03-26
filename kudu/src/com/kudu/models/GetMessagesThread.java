package com.kudu.models;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

public class GetMessagesThread extends Thread {
	LinkedHashMap<String, String> conversation;
	ConversationModel conversationModel;
	
	public GetMessagesThread(ConversationModel convMod)
	{
		conversationModel = convMod;
	}
	
	public LinkedHashMap<String, String> getConversation()
	{
		return conversation;
	}
	
	public void run()
	{
		try {
			conversation = conversationModel.getConversation();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
