package com.kudu.models;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;

public class ConversationModel {

	private Cluster cluster;

	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}

	public LinkedHashMap<String, String> getConversation(String conversationID)
	{
		LinkedHashMap<String, String> conversation = new LinkedHashMap<String, String>();

		Session session = cluster.connect("kududb");
		String query1 = "SELECT * FROM conversation WHERE conversationuuid=" + conversationID
				+ " LIMIT 100;";
		PreparedStatement statement = session.prepare(query1);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		if (rs.isExhausted()) {
			session.close();
			return null;
		} else {
			UUID uuid = null;
			String message;
			for (Row row : rs) {
				uuid = row.getUUID("idtimeuuid");
				long time = UUIDs.unixTimestamp(uuid);
				message = row.getString("message");
				conversation.put(String.valueOf(time), message);
			}
			session.close();
			return conversation;
		}


	}
	
	public void addMessage(String username, String conversationID, String message)
	{
		UUID uuid = UUIDs.timeBased();
		Session session = cluster.connect("kududb");
		String query1 = "INSERT INTO conversation (conversationuuid, idtimeuuid, message) VALUES (" + 
		conversationID + ", " + uuid + ", \'" + username + ":" + message + "\');";
		PreparedStatement statement = session.prepare(query1);
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
	}

	public String getConversationID(String friendID, String username)
	{
		Session session = cluster.connect("kududb");
		String query1 = "SELECT conversation FROM friends WHERE username=\'" + username
				+ "\' AND friendname=\'" + friendID + "\';";
		PreparedStatement statement = session.prepare(query1);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		if (rs.isExhausted()) {
			session.close();
			return null;
		} else {
			UUID uuid = null;
			for (Row row : rs) {
				uuid = row.getUUID("conversation");
			}
			session.close();
			return uuid.toString();
		}
	}
}
