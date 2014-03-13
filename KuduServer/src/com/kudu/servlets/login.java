package com.kudu.servlets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Servlet implementation class login
 */
@WebServlet({ "/login", "/login/*" })
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("fucks");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//JSONObject loginJSON = new JSONObject(request.getParameter("cmd"));
		
		HttpEntity entity = new InputStreamEntity(request.getInputStream(),  
                request.getContentLength());  
		DataInputStream stream = new DataInputStream(entity.getContent());
		
		//int len = stream.readInt();
	    //byte[] data = new byte[len];
	    
	    String s = stream.toString();
	    
	    JSONObject json = new JSONObject(s);
		
		
//        ObjectInputStream ois = new ObjectInputStream(entity.getContent());  
//        System.out.println("Trying to read Object");  
//        Object o = null;
//        try {
//			o = ois.readObject();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//        
//        JSONObject json = (JSONObject)o;
		
		System.out.println("fucks");
	}

}
