package com.kudu.models;

import java.util.UUID;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class RegisterModel {

	private Cluster cluster;
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	public boolean checkExistingUsers(String username) {
		boolean userExists = false;
		Session session = cluster.connect("kududb");
		String query = "SELECT username FROM users WHERE username='"+username+"';";
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		if (!rs.isExhausted()) {
			session.close();
			userExists = true;
		}
		return userExists;
	}
	
	public boolean addNewUser(String username, String password, String email, UUID uuid){
		boolean userAdded = true;
		Session session = cluster.connect("kududb");
		
		String insertDetails = "INSERT INTO users (iduuid, username, email, password) VALUES ("+uuid+", '"+username+"', '"+email+"', '"+password+"');";
		PreparedStatement statement = session.prepare(insertDetails);                  
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
		
		String checkDetails = "SELECT username FROM users WHERE username='"+username+"';";
		PreparedStatement statement2 = session.prepare(checkDetails);
		BoundStatement boundStatement2 = new BoundStatement(statement2);
		ResultSet rs = session.execute(boundStatement2);
		
		if(rs.isExhausted())
			userAdded = false;
		session.close();
		return userAdded;
	}
}
