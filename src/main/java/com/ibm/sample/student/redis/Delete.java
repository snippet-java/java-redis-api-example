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

@WebServlet(urlPatterns = {"/redis/del", "/redis/hdel"})
public class Delete extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	JsonObject output = new JsonObject();
    	
		Jedis client = new RedisConnectionService().getConnection();
    	
		String key = request.getParameter("key");
		String field = request.getParameter("field");
		
    	if(key == null || key.equals(""))
    		output.addProperty("err", "Please specific a valid key");
    	else if(field != null && !field.isEmpty()) {
    		client.hdel(key, field);
    		output.addProperty("out", "Hashed Field and value deleted");
    	}
    	else {
    		client.del(key);
    		output.addProperty("out", "value deleted");
    	}
    		
		
    	client.close();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

    private static final long serialVersionUID = 1L;
}
