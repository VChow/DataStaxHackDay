package com.kudu.activities;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.client.ClientProtocolException;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kudu.adapters.Item;
import com.kudu.adapters.MessageAdapter;
import com.kudu.adapters.Receiver;
import com.kudu.adapters.Sender;
import com.kudu.models.AESEncrypt;
import com.kudu.models.ConversationModel;
import com.kudu.models.GetMessagesThread;
import com.kudu.models.Session;



@SuppressLint("NewApi")
public class ConversationActivityFragment extends ListFragment{
	private Context context;
	private ConversationModel conversationModel;
	private LinkedHashMap<String, String> conversation;
	private GetMessagesThread getMessages;
	private String username;
	private MessageAdapter adapter;
	private EditText messagebox;
	Handler mHandler = new Handler();
	Thread checkMessageThread;
	String friendName;
	volatile boolean activityStopped = false;

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_conversation, container, false);
		context = container.getContext();
		//setHasOptionsMenu(true);
		Bundle args = getArguments();
		friendName = args.getString("friendname");
		getActivity().setTitle(friendName);
		
		Session session = new Session();
		session = MainActivity.db.checkSessionExists();
		username = session.getUsername();
		
		messagebox = (EditText)rootView.findViewById(R.id.message);
		
		conversationModel = new ConversationModel(friendName, username);
		
		updateConversation(inflater);
		
		checkMessageThread = new Thread() {
			@Override
			public void run() {
				while (!activityStopped) {
					try {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								adapter.clear();
								updateConversation(inflater);
								
							}
						});
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		};
		checkMessageThread.start();
		

	    
		
		Button button = (Button) rootView.findViewById(R.id.send_button);
	    button.setOnClickListener(new View.OnClickListener() {
	      @Override
	      public void onClick(View v) {
	        onSendClicked(inflater);
	      }
	    });
		
		return rootView;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		//checkMessageThread.interrupt();
		Log.e("sup", "sup");
		activityStopped = true;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		activityStopped = false;
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	public void onSendClicked(LayoutInflater inflater)
	{
		final String message = messagebox.getText().toString();
		if(!message.equals(""))
		{
			new Thread(new Runnable() {
				public void run(){
					try {
						
						conversationModel.sendMessage(message);
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}}).start();

			messagebox.setText("");

			adapter.clear();
			Calendar cal = Calendar.getInstance();
	    	cal.getTime();
	    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			conversation.put(sdf.format(cal.getTime()),  username + ":" + message);

			updateConversation(inflater);
			
		}
	}
	
	private void updateConversation(LayoutInflater inflater)
	{
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
		
		adapter = new MessageAdapter(getActivity(), inflater, items);
		setListAdapter(adapter); 
	}

	private List<Item> displayConversation(LinkedHashMap<String, String> conversation)
	{
		List<Item> items = new ArrayList<Item>();
		
		
		if(conversation != null)
		{
			Map<String, String> conversationSort = new TreeMap<String, String>();
			conversationSort.putAll(conversation);
			Iterator it = conversationSort.entrySet().iterator();
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
				String minutes;
				if(times.getMinutes() < 10)
					minutes = "0" + String.valueOf(times.getMinutes());
				else
					minutes = String.valueOf(times.getMinutes());
				
				String formatTime = String.valueOf(times.getHours()) + ":" + minutes;
				
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
