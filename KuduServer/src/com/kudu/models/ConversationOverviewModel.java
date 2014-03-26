package com.kudu.models;

import java.util.UUID;
import java.util.Iterator;
import java.util.LinkedList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class ConversationOverviewModel {

private Cluster cluster;
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	public String[] retrieveConversations(String username)
	{
		LinkedList<String> values = new LinkedList<String>();
		Session session = cluster.connect("kududb");
		String query = "SELECT * FROM friends WHERE username='"+username+"';";
		
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		
		if(!rs.isExhausted()) {
			for(Row row : rs) {
				if(!(row.getUUID("conversation")== null))
				values.add(row.getString("friendname")); 
			}
		}
		session.close();
		return values.toArray(new String[values.size()]);
	}
	
	public boolean addConversation(String username, String friendname)
	{
		boolean conversationAdded = true;
		UUID uuid = UUID.randomUUID();
		Session session = cluster.connect("kududb");
		String insertContact = "INSERT INTO friends (username,friendname,conversation) VALUES ('"+username+"','"+friendname+"','"+uuid+");"; 
		
		PreparedStatement statement = session.prepare(insertContact);
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
		
		/*
		 * Check if the conversation was added
		 */
		String checkAdded = "SELECT * FROM friends WHERE username='"+username+"' AND friend='"+friendname+"';"; 
		PreparedStatement statement2 = session.prepare(checkAdded);
		BoundStatement boundStatement2 = new BoundStatement(statement2);
		ResultSet rs = session.execute(boundStatement2);
		
		if(rs.isExhausted())
			conversationAdded = false;
		session.close();
		
		return conversationAdded;
	}
}
