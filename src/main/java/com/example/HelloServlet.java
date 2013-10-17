package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {

	private static Connection getConnection() throws URISyntaxException, SQLException {
	    URI dbUri = new URI(System.getenv("CLOUDCONNECT_URL"));

	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

	    return DriverManager.getConnection(dbUrl, username, password);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        
        resp.setContentType("text/html");
        Connection conn = null;
        try {
        	conn = getConnection();
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery("select name from account");
        	out.write("Accounts:<b>".getBytes());
        	while(rs.next()) {
        		String name = rs.getString("name");
        		out.write(name.getBytes());
        		out.write("<b>".getBytes());
        	}
        } catch (SQLException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        } catch (URISyntaxException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        } finally {
        	if(conn != null) {        		
        		try {
	                conn.close();
                } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
                }
        	}
        }
        
        out.flush();
        out.close();
    }
    
}
