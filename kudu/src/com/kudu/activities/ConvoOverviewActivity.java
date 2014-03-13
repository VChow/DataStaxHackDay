package com.kudu.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.kudu.R;
import com.kuduapp.models.*;

public class ConvoOverviewActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);

	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.main, menu);
	    MenuItem searchItem = menu.findItem(R.id.action_search);
	    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
	    // Configure the search info and add any event listeners
	    
	    return super.onCreateOptionsMenu(menu);
	}*/
	
	/** Called when the user touches the Profile button */
	public void gotoProfile(View view) {
	    // Do something in response to button click
		Intent myIntent = new Intent(ConvoOverviewActivity.this, ProfileActivity.class);
		ConvoOverviewActivity.this.startActivity(myIntent);
	}
	
	/** Called when the user touches the Contacts button */
	public void gotoContacts(View view) {
	    // Do something in response to button click
		Intent myIntent = new Intent(ConvoOverviewActivity.this, ContactsActivity.class);
		ConvoOverviewActivity.this.startActivity(myIntent);
	}
	
	public void pullUserData(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				User user = new User();
				
				
			}
		}).start();
	}
}
