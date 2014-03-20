package com.kudu.models;

import java.util.UUID;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class ProfileModel {

	private Cluster cluster;
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	public void updateProfile(String[] userDetails, UUID uuid){

		Session session = cluster.connect("kududb");
		String query = "BEGIN BATCH"
				+ "UPDATE users (name, username, email, location, bio) VALUES ("+userDetails[0]+", '"+userDetails[1]+"', '"+userDetails[3]+"', '"+userDetails[4]+"', '"+userDetails[5]+"'); WHERE iduuid =" +uuid+";"                                            
				+ "UPDATE login (password) VALUES ("+userDetails[2]+");"
				+ "APPLY BATCH;";
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
			
	}
	
	public String[] pullProfile(){
		String[] userDetails = new String[6];
		
		return userDetails;
	}
}
