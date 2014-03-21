package com.kudu.models;

import java.util.ArrayList;
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
	
	//public ArrayList<String> pullProfile(UUID uuid){
	public String[] pullProfile(UUID uuid){
		String[] userProfile = new String[6];
		//ArrayList<String> userProfile = new ArrayList<String>();
		
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
				userProfile[0] = r.getString(0); //username
				userProfile[1] = r.getString(2); //name 				
				userProfile[3] = r.getString(3); //bio
				userProfile[4] = r.getString(4); //email
				userProfile[5] = r.getString(5); //location
				userProfile[2] = r.getString(6); //password
				/*userProfile.add(r.getString(0));
				userProfile.add(r.getString(2));
				userProfile.add(r.getString(3));
				userProfile.add(r.getString(4));
				userProfile.add(r.getString(5));
				userProfile.add(r.getString(6));*/
			}
		}
		session.close();
		return userProfile;
	}
}
