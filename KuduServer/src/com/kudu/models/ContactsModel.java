package com.kudu.models;

import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class ContactsModel {

private Cluster cluster;
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	public void retrieveContacts(UUID user_id)
	{
		Session session = cluster.connect("kududb");
		String query = "SELECT "; //all usernames from friends(?) where uuid = user_id
		
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		
		if(!rs.isExhausted()) {
			//add the shit into a JSON
		}
		session.close();
		
		//return JSON thing
	}
	
	public boolean addContact(UUID user_id, String contactname)
	{
		boolean contactAdded = true;
		Session session = cluster.connect("kududb");
		String insertContact = "INSERT INTO"; //friends(?) (contact_name) where uuid = user_id
		
		PreparedStatement statement = session.prepare(insertContact);
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
		
		/*
		 * Check if the contact was added
		 */
		String checkAdded = "SELECT "; //contactname from friends(?) where uuid = user_id
		PreparedStatement statement2 = session.prepare(checkAdded);
		BoundStatement boundStatement2 = new BoundStatement(statement2);
		ResultSet rs = session.execute(boundStatement2);
		
		if(rs.isExhausted())
			contactAdded = false;
		session.close();
		
		return contactAdded;
	}
	
}
