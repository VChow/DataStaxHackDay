package com.kudu.models;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class KeyModel {
	private Cluster cluster;
	
	public KeyModel() {}
	
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	
	/*
	 * Inserts your diffie-hellman value into friends table
	 */
	public void insertKey(String username, String friend, String key) {
		Session session = cluster.connect("kududb");
		String query = "INSERT INTO key (username, friend, diffie) VALUES ('"+friend+"', '"+username+"', '"+key+"');";
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
	}
	
	public HashMap<String, String> getKey(String username)
	{
		HashMap<String, String> keys = new HashMap<String, String>();
		Session session = cluster.connect("kududb");
		String query = "SELECT * FROM key WHERE username = \'" + username + "\';";
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		if (rs.isExhausted()) {
			session.close();
			return null;
		} else {
			for(Row row : rs) {
				String friendName = row.getString("friend"); 
				String diffie = row.getString("diffie");
				keys.put(friendName, diffie);
			}
		}
		return keys;
	}
	
}
