package com.kudu.models;

import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class RegisterModel {

	private Cluster cluster;
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	public boolean checkExistingUser(String username){
		boolean usernameExists = true;
		
		Session session = cluster.connect("kududb");
		PreparedStatement statement = session.prepare("SELECT username from users WHERE username = \'" + username + "\';");
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		
		//If not empty, then the username is already taken
		if (rs.isExhausted()) {
			session.close();
			usernameExists = false;
		}else{
			usernameExists = true;
		}
		
		return usernameExists;
	}
	
	
	
	public boolean addNewUser(String username, String password, String email){
		boolean userAdded = false;
		
		Session session = cluster.connect("kududb");
		
		//Add user to users table
		PreparedStatement statement = session.prepare("INSERT into users (username, password, email) VALUES ('"+ username + "','"+ password + "','" + email + "');");                   
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
		
		//Check if user was added
		PreparedStatement statement2 = session.prepare("SELECT * from users WHERE username = \'" + username + "\';");
		BoundStatement boundStatement2 = new BoundStatement(statement2);
		ResultSet rs = session.execute(boundStatement2);
		
		if(rs.isExhausted()){
			session.close();
			userAdded = true;
		}
			
		
		return userAdded;
	}
	
	
	
	
	
	
}
