package com.kudu.activities;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.http.client.ClientProtocolException;
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

import com.kudu.models.ContactsModel;
import com.kudu.models.Session;

public class ContactsActivityFragment extends ListFragment {
    String username = "admin";
    private Context context;
	public ContactsActivityFragment() {}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_activity_fragment, container, false);
        context = container.getContext();
		Session session = new Session();
		session = MainActivity.db.checkSessionExists();
		username = session.getUsername();
        setHasOptionsMenu(true);
        displayContacts();
        return rootView;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	inflater.inflate(R.menu.action_bar_icons_contacts, menu);
    }
    
    private void displayContacts() {
    	if(checkInternetConnection()) {
			new Thread(new Runnable() {
				public void run() {
					ContactsModel cm = new ContactsModel();
					try {  
						LinkedList<String> temp = new LinkedList<String>();
						temp = cm.getContacts(username);
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
    		values[i] = array[i];
    	((Activity)context).runOnUiThread(new Runnable(){
		    public void run(){
		    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
		    	setListAdapter(adapter);
		    }
		});
    }
    
    public void onListItemClick(ListView lv, View v, int position, long id) {
    	final String itemSelection = getListView().getItemAtPosition(position).toString();
    	final ContactsModel cm = new ContactsModel();
    	new Thread(new Runnable (){
    		public void run()
    		{
    			try {
    				cm.startConversation(username, itemSelection);
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
    	
    	
    	Fragment fragment = new ConversationOverviewActivityFragment();
		fragmentManager(fragment);
		//closeDrawer(position);
	}
	
	private void fragmentManager(Fragment fragment) {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	}
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
