package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.kudu.lib.CassandraHosts;
import com.kudu.models.KeyModel;

/**
 * Servlet implementation class key
 */
@WebServlet("/key")
public class key extends HttpServlet {
	private Cluster cluster;
	private static final long serialVersionUID = 1L;
       
    public key() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
		cluster = CassandraHosts.getCluster();
	}
    
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		KeyModel km = new KeyModel();
		if(req.getParameter("send").equals(("true"))) 
		{
			String username = req.getParameter("username");
			String friend = req.getParameter("friend");
			String key = req.getParameter("key");
			km.setCluster(cluster);
			km.insertKey(username, friend, key);
		} 
		else if(req.getParameter("retrieve").equals(("true"))) 
		{
			String username = req.getParameter("username");
			km.setCluster(cluster);
			HashMap<String, String> keys = km.getKey(username);
			
			res.setContentType("application/json");
			PrintWriter out = res.getWriter();
			JSONObject jsonObject = new JSONObject();

			if(keys != null)
			{
				jsonObject.putAll(keys);
				out.print(jsonObject);
			}
			out.flush();
		}
	}
}
