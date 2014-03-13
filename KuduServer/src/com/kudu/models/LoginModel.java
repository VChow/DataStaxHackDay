package com.kudu.models;

import com.datastax.driver.core.Cluster;

public class LoginModel {
	
	private Cluster cluster;
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	public boolean checkLogin(String username, String Password)
	{
		return true;
	}

}
