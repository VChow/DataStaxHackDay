package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.kudu.lib.*;
import com.kudu.models.ContactsModel;
import com.kudu.models.ConversationModel;

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
    
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if(req.getParameter("retrieve").equals("true"))
			retrieve(req,res);
		else if (req.getParameter("add").equals("true"))
			addContact(req,res);
		else if (req.getParameter("addConv").equals("true"));
			addConversation(req, res);
	}

	public void retrieve(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		final String username = req.getParameter("username");
		
		ContactsModel cm = new ContactsModel();
		cm.setCluster(cluster);
		String[] values = cm.retrieveContacts(username);
		
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("contactsValues", new JSONArray(Arrays.asList(values)));
		out.print(jsonObject);
		out.flush();
	}
	
	public void addContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		final String contact = request.getParameter("contact");
		final String username = request.getParameter("username");
		
		ContactsModel contactsModel = new ContactsModel();
		contactsModel.setCluster(cluster);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		if(contactsModel.addContact(contact, username)) {
			jsonObject.put("contactAdded", "true");	
		} else {
			jsonObject.put("contactAdded", "false");
		}
		out.print(jsonObject);
		out.flush();
	}
	
	private void addConversation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = request.getParameter("username");
		String contact = request.getParameter("contact");
		
		ContactsModel conMod = new ContactsModel();
		conMod.setCluster(cluster);
		
		conMod.addConversation(username, contact);
		
	}
	
}
