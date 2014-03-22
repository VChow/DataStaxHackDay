package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.kudu.lib.CassandraHosts;
import com.kudu.models.ProfileModel;

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
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		if (req.getParameter("update").equals(("true")))
			update(req, res);
		else if(req.getParameter("retrieve").equals("true"))
			retrieve(req, res);
	}
	
	public void update(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		final String username = req.getParameter("username");
		final String name = req.getParameter("name");
		final String password = req.getParameter("password");
		final String email = req.getParameter("email");
		final String location = req.getParameter("location");
		final String bio = req.getParameter("bio");	
		//final String id = req.getParameter("uuid");
		
		ProfileModel profileModel = new ProfileModel();
		profileModel.setCluster(cluster);

		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		JSONObject jsonObject = new JSONObject();
		//profileModel.updateProfile(username, name, uuid, password, email, location, bio);
		jsonObject.put("profileUpdate", true);
		out.print(jsonObject);
		out.flush();
	}
	
	public void retrieve(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String [] userProfile = new String[6];
		final String username = req.getParameter("username");
		final String id = req.getParameter("id");
		UUID uuidid = UUID.fromString(id);
		
		ProfileModel profileModel = new ProfileModel();
		userProfile = profileModel.getProfile(uuidid, username);
		
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("profileRetrieve", new JSONArray(Arrays.asList(userProfile)));
		jsonObject.put("profileRetrieve", userProfile);
		out.print(jsonObject);
		out.flush();
	}
	
}
