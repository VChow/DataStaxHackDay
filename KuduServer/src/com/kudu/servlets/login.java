package com.kudu.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.kudu.lib.CassandraHosts;
import com.kudu.models.LoginModel;

/**
 * Servlet implementation class login
 */
@WebServlet({ "/login", "/login/*" })
public class login extends HttpServlet {
	private Cluster cluster;
	private static final long serialVersionUID = 1L;
       
    public login() {
        super();
    }
    
	public void init(ServletConfig config) throws ServletException {
		cluster = CassandraHosts.getCluster();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username"); 
		String password = request.getParameter("password");
		
		LoginModel loginModel = new LoginModel();
		loginModel.setCluster(cluster);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		if(loginModel.checkLogin(username, password))
			jsonObject.put("login", "true");
		else
			jsonObject.put("login", "false");
		
		out.print(jsonObject);
		out.flush();
	}
}
