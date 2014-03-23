package com.kudu.activities;


import android.app.ListFragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ContactsActivityFragment extends ListFragment {
	String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2","shit", "fuck", "shithouse", "twatend"};
    
	public ContactsActivityFragment() {}
	private UUID user_id;
	public List contactsList = new ArrayList<String>();
	public ContactsModel userContacts= new ContactsModel();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_activity_fragment, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        return rootView;
    }
	
    /*
     * Get a List of contacts for the user
     */
	public void retrieveContacts() {
		//ContactsModel userContacts = new ContactsModel(user_id);
		 
		try 
		{
			contactsList = userContacts.retrieveContacts();
		} 
		catch (IllegalStateException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		catch (JSONException e) {e.printStackTrace();}
		
	}
	
	/*
	 * Add a new contact to user's contact list
	 */
	public void addContact() {
		//Once a contact is added, refresh the page?
	}

	/*
	 * For each item in List, add a new clickable thing to UI
	 */
	public void populateContactsScreen() {
		
		for(int i = 0; i < contactsList.size(); i++){
			//Read something about using an adapter
			//to add new objects to UI
		}
	}
	
	private boolean checkInternetConnection() {
		ConnectivityManager conMgr = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

}
