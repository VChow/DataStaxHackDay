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
		PreparedStatement statement = session.prepare("SELECT iduuid from users WHERE username = \'" + username + "\';");
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet rs = session.execute(boundStatement);
		if (rs.isExhausted()) {
			session.close();
			return false;
		}
		else
		{
			UUID uuid = null;
			for (Row row : rs)
			{
				uuid = row.getUUID("iduuid");
			}

			statement = session.prepare("SELECT iduuid from users WHERE iduuid = \'" + uuid + "\' AND password = \'"
					+ password + "\';");
			boundStatement =  new BoundStatement(statement);
			rs = session.execute(boundStatement);
			if( rs.isExhausted())
			{
				session.close();
				return false;
			}
			else
			{
				session.close();
				return true;
			}
		}
	}

}
