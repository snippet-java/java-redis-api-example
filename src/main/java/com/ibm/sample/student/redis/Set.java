package com.ibm.sample.student.redis;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

@WebServlet("/redis/set")
public class Set extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	JsonObject output = new JsonObject();
    	
		Jedis client = new RedisConnectionService().getConnection();
    	
    	ArrayList<String> paramKeys =  Collections.list(request.getParameterNames());
    	if(paramKeys.size() <= 0)
    		output.addProperty("err", "Please specify valid key=value param");
    	else if (paramKeys.size() == 1){
    		
    		String key = paramKeys.get(0);
			String value = request.getParameter(key);
			client.set(key, value);
    		output.addProperty("out", "Success created key and value");
    	}
    	else {
    		
    		String[] keyValues = new String[paramKeys.size() * 2];
    		int count = 0;
    		for(String key : paramKeys) {
    			String value = request.getParameter(key);
    			keyValues[count++] = key;
    			keyValues[count++] = value;
    		}
    		client.mset(keyValues);
    		output.addProperty("out", "Success created set of keys and values");
    	}
    	client.close();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

    private static final long serialVersionUID = 1L;
}
