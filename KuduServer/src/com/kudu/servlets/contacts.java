package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.kudu.lib.*;
import com.kudu.models.ContactsModel;

@WebServlet({"/contacts", "/contacts/*"})
public class contacts extends HttpServlet {
	private Cluster cluster;
	private static final long serialVersionUID = 1L;
       
    public contacts() {
        super();
    }

	public void init(ServletConfig config) throws ServletException {
		cluster = CassandraHosts.getCluster();
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("retrieve").equals("true"))
			retrieve(request,response);
		else if (request.getParameter("insert").equals("true"))
			insert(request,response);
	}

	public void retrieve(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		final String user_id = request.getParameter("user_id");
		UUID uuid = null;
		uuid = uuid.fromString(user_id);
		
		ContactsModel contactsModel = new ContactsModel();
		contactsModel.setCluster(cluster);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		
		/*
		 * Should return a JSONArray? or JSONObject
		 */
		contactsModel.retrieveContacts(uuid);
		out.print(jsonObject);
		out.flush();
	}
	
	public void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		final String user_id = request.getParameter("uuid");
		final String contactname = request.getParameter("contactname");
		UUID uuid = null;
		uuid = uuid.fromString(user_id);
		
		ContactsModel contactsModel = new ContactsModel();
		contactsModel.setCluster(cluster);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		if(contactsModel.addContact(uuid, contactname)){
			jsonObject.put("contactAdded", "true");		
		}
		else
		{
			jsonObject.put("contactAdded", "false");
		}
		
		out.print(jsonObject);
		out.flush();
	}
	
}
