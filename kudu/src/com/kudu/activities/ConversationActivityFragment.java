package com.kudu.activities;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONException;

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

import com.kudu.models.ConversationOverviewModel;

public class ConversationActivityFragment extends ListFragment {
String username = "admin";
private Context context;

    public ConversationActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conversation_activity_fragment, container, false);
        context = container.getContext();
        setHasOptionsMenu(true);
        displayConversations();
        return rootView;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     super.onCreateOptionsMenu(menu, inflater);
     inflater.inflate(R.menu.action_bar_icons_contacts, menu);
    }
    
    private void displayConversations(){
if(checkInternetConnection()) {
new Thread(new Runnable() {
public void run() {
ConversationOverviewModel com = new ConversationOverviewModel();
try {
LinkedList<String> temp = new LinkedList<String>();
Log.d("fuck", "About to get conversation list");
temp = com.getConversations(username);

Log.d("fuck", "Contents of temp: "+temp.get(0));
Log.d("fuck", "Got Conversations List");


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
     {values[i] = array[i];
     Log.d("fuck", ""+values[i]);}
     ((Activity)context).runOnUiThread(new Runnable(){
public void run(){
ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
setListAdapter(adapter);
}
});
    }

public void onListItemClick(ListView lv, View v, int position, long id) {
     String itemSelection = getListView().getItemAtPosition(position).toString();
     //new addDialog(getActivity(), itemSelection);
     Intent startConversation = new Intent(getActivity(), ConversationActivity.class);
     startConversation.putExtra("Friendname", "mark");
     startActivity(startConversation);
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