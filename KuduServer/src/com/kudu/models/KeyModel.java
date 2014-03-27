package com.kudu.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class KeyModel {
	private Cluster cluster;
	
	public KeyModel() {}
	
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	
	public void insertKey(String username, String friend, String key) {
		Session session = cluster.connect("kududb");
		String query = "INSERT INTO key (username, friend, diffie) VALUES ('"+friend+"', '"+username+"', '"+key+"');";
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
	}
	
}
