package com.ibm.sample.student.redis;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

@WebServlet("/redis/get")
public class Get extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	JsonObject output = new JsonObject();
    	
		Jedis client = new RedisConnectionService().getConnection();
    	
		String key = request.getParameter("key");
    	if(key == null || key.equals(""))
    		output.addProperty("err", "Please specific a valid key");
    	else 
    		output.addProperty("data", client.get(key));
		
    	client.close();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

    private static final long serialVersionUID = 1L;
}
