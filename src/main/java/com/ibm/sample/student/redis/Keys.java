package com.ibm.sample.student.redis;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import redis.clients.jedis.Jedis;

@WebServlet("/redis/keys")
public class Keys extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	JsonObject output = new JsonObject();
    	
    	Jedis client = new RedisConnectionService().getConnection();
    	java.util.Set<String> keySet =  client.keys("*");
    	JsonArray keyArray = new JsonParser().parse(new Gson().toJson(keySet)).getAsJsonArray();
    	output.add("data", keyArray);
    	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

}
