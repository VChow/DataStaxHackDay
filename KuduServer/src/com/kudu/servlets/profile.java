package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.kudu.lib.CassandraHosts;
import com.kudu.models.ProfileModel;
import com.kudu.models.RegisterModel;

@WebServlet({ "/profile", "/profile/*"})
public class profile extends HttpServlet{
	private Cluster cluster;
	private static final long serialVersionUID = 1L;
	
	public profile() {
        super();
    }
	
	public void init(ServletConfig config) throws ServletException {
		cluster = CassandraHosts.getCluster();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String username = request.getParameter("username"); 
		final String password = request.getParameter("password");
		final String email = request.getParameter("email");
		final UUID uuid = UUID.randomUUID();
		
		ProfileModel profileModel = new ProfileModel();
		profileModel.setCluster(cluster);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		if(!profileModel.checkExistingUsers(username)){
			if(profileModel.addNewUser(username, password, email, uuid))
				jsonObject.put("register", "true");
			else
				jsonObject.put("register", "false");
		}
		else
			jsonObject.put("register", "false");
		out.print(jsonObject);
		out.flush();
	}
}
