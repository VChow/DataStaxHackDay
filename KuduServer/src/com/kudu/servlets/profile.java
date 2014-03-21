package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
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
		final UUID uuid = UUID.randomUUID();
		final String name = request.getParameter("name");
		final String password = request.getParameter("password");
		final String email = request.getParameter("email");
		final String location = request.getParameter("location");
		final String bio = request.getParameter("bio");
		
		ProfileModel profileModel = new ProfileModel();
		profileModel.setCluster(cluster);

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		
		if (request.getParameter("update") != null) {
			profileModel.updateProfile(username, name, uuid, password, email, location, bio);
			jsonObject.put("profileUpdate", true);
			out.print(jsonObject);
			out.flush();
		}
		else if(request.getParameter("retrieve") != null){
			String[] userProfile = new String[6];
			//ArrayList<String> userProfile = new ArrayList<String>();
			
			userProfile = profileModel.pullProfile(uuid);
			//jsonObject.put("profileRetrieve", new JSONArray(Arrays.asList(userProfile)));
			jsonObject.put("profileRetrieve", userProfile);
			out.print(jsonObject);
			out.flush();
		}
	}
	
}
