package com.kudu.models;

import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class LoginModel {
	
	private Cluster cluster;
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	public boolean checkLogin(String username, String password)
	{
		Session session = cluster.connect("kududb");
		String query1 = "SELECT iduuid FROM users WHERE username=\'"+username+"\';";
		PreparedStatement statement = session.prepare(query1);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		if (rs.isExhausted()) {
			session.close();
			return false;
		} else {
			UUID uuid = null;
			for (Row row : rs) {
				uuid = row.getUUID("iduuid");
			}

			String query2 = "SELECT password FROM login WHERE iduuid="+uuid+" AND password='"+password+"'";
			statement = session.prepare(query2);
			boundStatement =  new BoundStatement(statement);
			rs = session.execute(boundStatement);
			if(rs.isExhausted()) {
				session.close();
				return false;
			} else {
				session.close();
				return true;
			}
		}
	}

}
