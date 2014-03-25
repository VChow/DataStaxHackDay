package com.kudu.activities;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kudu.models.ConversationModel;

public class ConversationActivity extends Activity{

	private static final String INTENT_KEY_PARAM_A = "Friendname";
	private String friendname;
	private ConversationModel conversationModel;
	private LinkedHashMap<String, String> conversation;

	public static void startActivity(Context context, String name){

		Log.d("#fuckTom", "Friendname: " + name);

		// Build extras with passed in parameters
		Bundle extras = new Bundle();
		extras.putString(INTENT_KEY_PARAM_A, name);

		// Create and start intent for this activity
		Intent intent = new Intent(context, ConversationActivity.class);
		intent.putExtras(extras);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);

		// Extract parameters
		Bundle extras = getIntent().getExtras();
		friendname = extras.getString(INTENT_KEY_PARAM_A);
		conversationModel = new ConversationModel("mark", "admin");

		new Thread(new Runnable() {
			public void run(){
				Log.e("text", "Shit house");
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
		}).start();
		
		//display conversation
	}
}