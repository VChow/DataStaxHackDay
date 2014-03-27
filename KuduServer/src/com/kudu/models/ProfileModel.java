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
	public void setCluster(Cluster cluster) { 
		this.cluster = cluster; 
	}
	
	public void updateProfile(String username, String name, UUID uuid, String password, String email, String location, String bio) {
		Session session = cluster.connect("kududb");
		String query = "UPDATE users SET name='"+name+"', email='"+email+"', location='"+location+"', bio='"+bio+"' WHERE username='"+username+"' AND iduuid="+uuid+";";
		String query2 = "UPDATE login SET password='"+password+"' WHERE iduuid="+uuid+";";
		
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		session.execute(boundStatement);
		
		PreparedStatement statement2 = session.prepare(query2);
		BoundStatement boundStatement2 = new BoundStatement(statement2);
		session.execute(boundStatement2);
		
		session.close();
	}
	 
	public String[] getProfile(String id, String username) {
		String [] userProfile = new String[7];
		Session session = cluster.connect("kududb");
		String query = "SELECT * FROM users WHERE username = '" + username +"';";
				
		PreparedStatement statement = session.prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		
		if(!rs.isExhausted()){
			Iterator<Row> it = rs.iterator();
			while(it.hasNext()){
				Row r = it.next();
				userProfile[0] = r.getString(0); //username
				UUID temp = r.getUUID(1);
				userProfile[1] = temp.toString(); //id 
				userProfile[2] = r.getString(2); //name
				userProfile[3] = r.getString(3); //email
				userProfile[4] = r.getString(5); //bio
				userProfile[5] = r.getString(6); //location
			}
		}
		
		UUID uuid = UUID.fromString(id);
		String query1 = "SELECT * FROM login WHERE iduuid="+uuid+";";
		PreparedStatement statement1 = session.prepare(query1);
		BoundStatement boundStatement1 = new BoundStatement(statement1);
		ResultSet res = session.execute(boundStatement1);
		
		if(!res.isExhausted()){
			Iterator<Row> it = rs.iterator();
			while(it.hasNext()){
				Row r = it.next();
				userProfile[6] = r.getString(2); //password
			}
		}
		session.close();
		return userProfile;
	}
}
