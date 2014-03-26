package com.kudu.activities;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.kudu.models.ConversationModel;
import com.kudu.models.ConversationOverviewModel;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kudu.models.*;

public class ConversationActivityFragment extends ListFragment{
	private Context context;
	private ConversationModel conversationModel;
	private LinkedHashMap<String, String> conversation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_conversation, container, false);
		context = container.getContext();
		//setHasOptionsMenu(true);
		Bundle args = getArguments();
		String friendName = args.getString("friendname");
		getActivity().setTitle(friendName);
		conversationModel = new ConversationModel(friendName, "admin");
		new Thread(new Runnable() {
			public void run(){
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
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void displayConversation(){
		if(checkInternetConnection()) {
			new Thread(new Runnable() {
				public void run() {
					//
				}
			}).start();
		}
	}

	public void setArray(String[] array, int size) 
	{
		final String[] values = new String[size];
		for(int i = 0; i < array.length; i++)
		{values[i] = array[i];}
		((Activity)context).runOnUiThread(new Runnable()
		{
			public void run()
			{
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
				setListAdapter(adapter);
			}
		});
	}

	private boolean checkInternetConnection() {
		ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

}
