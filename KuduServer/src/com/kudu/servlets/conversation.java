package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
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
import com.kudu.lib.CassandraHosts;
import com.kudu.models.*;

/**
 * Servlet implementation class conversation
 */
@WebServlet("/conversation")
public class conversation extends HttpServlet {
	private Cluster cluster;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public conversation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		cluster = CassandraHosts.getCluster();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ConversationModel convModel = new ConversationModel();
		convModel.setCluster(cluster);

		String friendID = request.getParameter("friendID");
		String username = request.getParameter("username");

		LinkedHashMap<String, String> conversation = convModel.getConversation(friendID, username);

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		
		if(conversation != null)
			jsonObject.putAll(conversation);

		out.print(jsonObject);
		out.flush();

	}

}
