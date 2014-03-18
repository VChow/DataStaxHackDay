package com.kudu.models;

import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class ContactsModel {

private Cluster cluster;
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
}
