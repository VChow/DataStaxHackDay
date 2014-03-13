package com.example.kudu;

import java.util.Iterator;
import java.util.Set;
import android.util.Log;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;

public final class CassandraHosts {
	private static Cluster cluster;
	static String Host = "kududb.cloudapp.net"; // at least one starting point to talk

	public CassandraHosts() {
	}

	public static String getHost() {
		return (Host);
	}

	public static String[] getHosts(Cluster cluster) {
		if (cluster == null) {
			Log.v("message", "Creating cluster connection");
			cluster = Cluster.builder().withPort(9160).addContactPoint(Host).build();
		}
		Log.v("message", "Cluster Name: "+ cluster.getClusterName());
		Metadata mdata = cluster.getMetadata();
		Set<Host> hosts = mdata.getAllHosts();
		String sHosts[] = new String[hosts.size()];

		Iterator<Host> it = hosts.iterator();
		int i = 0;
		while (it.hasNext()) {
			Host ch = it.next();
			sHosts[i] = (String) ch.getAddress().toString();
			Log.v("message", "Hosts: " + ch.getAddress().toString());
			i++;
		}
		return sHosts;
	}

	public static Cluster getCluster() {
		Log.v("message", "getCluster");
		cluster = Cluster.builder().addContactPoint(Host).build();
		getHosts(cluster);
		//Keyspaces.SetUpKeySpaces(cluster);
		return cluster;
	}
}
