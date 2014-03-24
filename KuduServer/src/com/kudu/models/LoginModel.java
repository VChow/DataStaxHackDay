package com.kudu.models;

import java.util.Iterator;
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
	
	public String checkLogin(String username, String password)
	{
		String serverPassword = null;
		UUID uuid = null;
		Session session = cluster.connect("kududb");
		String query1 = "SELECT iduuid FROM users WHERE username=\'" + username
				+ "\';";
		PreparedStatement statement = session.prepare(query1);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		if (rs.isExhausted()) {
			session.close();
			return null;
		} else {
			for (Row row : rs) {
				uuid = row.getUUID("iduuid");
			}

			String query2 = "SELECT * FROM login WHERE iduuid=" + uuid + " ;";
			statement = session.prepare(query2);
			boundStatement = new BoundStatement(statement);
			rs = session.execute(boundStatement);
			if (rs.isExhausted()) {
				session.close();
				return null;
			} else {
				Iterator<Row> it = rs.iterator();
				while(it.hasNext()){
					Row r = it.next();
					serverPassword = r.getString("password"); //password
				}
				session.close();
				if(serverPassword.equals(password))
					return uuid.toString();
				else {
					return null;
				}
			}
		}
	}

}
