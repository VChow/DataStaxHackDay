package com.kudu.models;

import java.util.Iterator;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class ProfileModel {

	private Cluster cluster;
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	public void updateProfile(String username, String name, UUID uuid, String email, String password, String location, String bio){

		Session session = cluster.connect("kududb");
		String query = "BEGIN BATCH"
				+ "UPDATE users (name, username, email, location, bio) VALUES ("+name+", '"+username+"', '"+email+"', '"+location+"', '"+bio+"'); WHERE iduuid =" +uuid+";"                                            
				+ "UPDATE login (password) VALUES ("+password+");"
				+ "APPLY BATCH;";
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
			
	}
	
	public String[] pullProfile(UUID uuid){
		String[] userDetails = new String[6];
		
		Session session = cluster.connect("kududb");
		String query = "BEGIN BATCH"
				+ "SELECT * from users WHERE iduuid = '" + uuid +"';"
				+ "SELECT password from login WHERE iduuid = '" + uuid +"';"
				+ "APPLY BATCH;";
				
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		
		if(!rs.isExhausted()){
			
			Iterator<Row> it = rs.iterator();
			
			while(it.hasNext()){
				Row r = it.next();
				userDetails[0] = r.getString(0); //username
				userDetails[1] = r.getString(2); //name 				
				userDetails[3] = r.getString(3); //bio
				userDetails[4] = r.getString(4); //email
				userDetails[5] = r.getString(5); //location
				userDetails[2] = r.getString(6); //password
			}
		}
		session.close();
		return userDetails;
	}
}
