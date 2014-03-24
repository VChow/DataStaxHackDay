package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

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
import com.kudu.models.ConversationOverviewModel;

@WebServlet({"/conversationOverview", "/conversationOverview/*"})
public class conversationOverview extends HttpServlet {
	private Cluster cluster;
	private static final long serialVersionUID = 1L;

    public conversationOverview() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void init(ServletConfig config) throws ServletException {
		cluster = CassandraHosts.getCluster();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("retrieve").equals("true"))
			retrieve(request,response);
		else if(request.getParameter("insert").equals("true"))
			insert(request,response);
	}

	public void retrieve(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		final String username = req.getParameter("username");

		ConversationOverviewModel com = new ConversationOverviewModel();
		com.setCluster(cluster);
		String[] values = com.retrieveConversations(username);
		
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("conversationValues", new JSONArray(Arrays.asList(values)));
		out.print(jsonObject);
		out.flush();
	}
	
	public void insert(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		final String username = req.getParameter("username");
		final String friendname = req.getParameter("friendname");
		
		ConversationOverviewModel com = new ConversationOverviewModel();
		com.setCluster(cluster);
			
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		JSONObject jsonObject = new JSONObject();
		if(com.addConversation(username, friendname)){
			jsonObject.put("conversationAdded", "true");
		}
		else{
			jsonObject.put("conversationAdded", "false");
		}
		
		out.print(jsonObject);
		out.flush();
	}
}
