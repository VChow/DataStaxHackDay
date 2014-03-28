package com.kudu.activities;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONException;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kudu.models.ConversationOverviewModel;
import com.kudu.models.Session;

public class ConversationOverviewActivityFragment extends ListFragment {
	String username = "";
	private Context context;

	public ConversationOverviewActivityFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.conversation_activity_fragment, container, false);
		context = container.getContext();
		Session session = new Session();
		session = MainActivity.db.checkSessionExists();
		username = session.getUsername();
		setHasOptionsMenu(true);
		displayConversations();
		getActivity().setTitle("Conversations");
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void displayConversations(){
		if(checkInternetConnection()) {
			new Thread(new Runnable() {
				public void run() {
					ConversationOverviewModel com = new ConversationOverviewModel();
					try {
						LinkedList<String> temp = new LinkedList<String>();
						temp = com.getConversations(username);


						int size = temp.size();
						String[] values = new String[size];

						for(int i = 0; i < temp.size(); i++)
							values[i] = temp.get(i);
						setArray(values, size);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public void setArray(String[] array, int size) {
		final String[] values = new String[size];
		for(int i = 0; i < array.length; i++)
		{values[i] = array[i];}
		((Activity)context).runOnUiThread(new Runnable(){
			public void run(){
				//ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_layout,R.id.label, values);
				setListAdapter(adapter);
			}
		});
	}

	public void onListItemClick(ListView lv, View v, int position, long id) {
		String itemSelection = getListView().getItemAtPosition(position).toString();
		Fragment fragment = new ConversationActivityFragment();
		Bundle args = new Bundle();
		args.putString("friendname", itemSelection);
		fragment.setArguments(args);
		fragmentManager(fragment);
		//closeDrawer(position);
	}
	
	private void fragmentManager(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
	}

	/*
	 * Needs changing ***
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.add_contact:
			DialogFragment newFragment = AddContactDialog.newInstance();
			newFragment.show(getFragmentManager(), "dialog");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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