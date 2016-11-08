package com.ibm.sample.student.redis;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import redis.clients.jedis.Jedis;

@WebServlet("/redis/hget")
public class HGet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	JsonObject output = new JsonObject();
    	
		Jedis client = new RedisConnectionService().getConnection();
    	
		String key = request.getParameter("key");
		String field = request.getParameter("field");
		
    	if(key == null || key.isEmpty())
    		output.addProperty("err", "Please specific a valid key");
    	else if(field != null && !field.isEmpty())
    		output.addProperty("data", client.hget(key, field));
    	//return all fields and value tied to the key
    	else
    		output.add("data", new JsonParser().parse(new Gson().toJson(client.hgetAll(key))));
		
    	client.close();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

    private static final long serialVersionUID = 1L;
}
