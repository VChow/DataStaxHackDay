package com.kudu.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.ListFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kudu.adapters.Item;
import com.kudu.adapters.MessageAdapter;
import com.kudu.adapters.Receiver;
import com.kudu.adapters.Sender;
import com.kudu.models.ConversationModel;
import com.kudu.models.GetMessagesThread;
import com.kudu.models.Session;



public class ConversationActivityFragment extends ListFragment{
	private Context context;
	private ConversationModel conversationModel;
	private LinkedHashMap<String, String> conversation;
	private GetMessagesThread getMessages;
	private String username;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_conversation, container, false);
		context = container.getContext();
		//setHasOptionsMenu(true);
		Bundle args = getArguments();
		String friendName = args.getString("friendname");
		getActivity().setTitle(friendName);
		
		Session session = new Session();
		session = MainActivity.db.checkSessionExists();
		username = session.getUsername();
		
		conversationModel = new ConversationModel(friendName, username);
		
		getMessages = new GetMessagesThread(conversationModel);
		getMessages.start();
		try {
			getMessages.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		conversation = getMessages.getConversation();
		
		List<Item> items = displayConversation(conversation);
		
		
		MessageAdapter adapter = new MessageAdapter(getActivity(), inflater, items);
		setListAdapter(adapter); 
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	private List<Item> displayConversation(LinkedHashMap<String, String> conversation)
	{
		List<Item> items = new ArrayList<Item>();
		items.add(new Sender("hello", "time"));
		items.add(new Receiver("hi", "time two"));
		
		if(conversation != null)
		{
			Iterator it = conversation.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry pair = (Map.Entry)it.next();
				String time = pair.getKey().toString();
				String codedMessage = pair.getValue().toString();

				String[] messageArray = codedMessage.split("\\:", 2);
				String message = messageArray[1];
				String sender = messageArray[0];
				
				long longtime = Long.parseLong(time);
				Date times = new Date(longtime);
				
				String formatTime = String.valueOf(times.getHours()) + ":" + String.valueOf(times.getMinutes());
				
				if(sender.equals(username))
					items.add(new Sender (message, formatTime));
				else
					items.add(new Receiver(message, formatTime));
			}
		}
		return items;
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
