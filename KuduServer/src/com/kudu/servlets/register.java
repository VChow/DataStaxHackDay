package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;

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
import com.kudu.models.LoginModel;
import com.kudu.models.RegisterModel;

/**
 * Servlet implementation class register
 */

@WebServlet({ "/register", "/register/*"})
public class register extends HttpServlet{
	private Cluster cluster;
	private static final long serialVersionUID = 1L;
	
	/**
     * @see HttpServlet#HttpServlet()
     */
	public register() {
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
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username"); 
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		
		RegisterModel registerModel = new RegisterModel();
		registerModel.setCluster(cluster);
		
		if(!registerModel.checkExistingUser(username)){
			if(registerModel.addNewUser(username, password, email)){
				System.out.println("User: " + username + " was successfully registered.");
			}
		}
		
		//What do this even do???
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		//if(loginModel.checkLogin(username, password))
			jsonObject.put("register", "true");
		//else
		//	jsonObject.put("login", "false");
		
		out.print(jsonObject);
		out.flush();
	}
}
