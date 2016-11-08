package com.ibm.sample.student.redis;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

@WebServlet("/redis/hset")
public class HSet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	JsonObject output = new JsonObject();
		Jedis client = new RedisConnectionService().getConnection();
    	
    	ArrayList<String> params =  Collections.list(request.getParameterNames());
    	if(params.size() <= 1)
    		output.addProperty("err", "Please specify valid key & field=value data");
    	else if (params.size() == 2){

			String key = "";
			String field = "";
			String value = "";
    		
    		for(String param : params) {
    			if(param.equalsIgnoreCase("key"))
    					key = request.getParameter(param);
    			else {
    				field = param;
    				value = request.getParameter(param);
    			}
    		}
System.out.println(key + " - " + field + " - " + value);
			client.hset(key, field, value);
    		output.addProperty("out", "Success created key, hashed field and value");
    	}
    	else {

			String key = "";
    		Map<String, String> fieldValueMap = new HashMap<String, String>();
    		
    		for(String param : params) {
    			if(param.equalsIgnoreCase("key"))
    					key = param;
    			else {
    				String field = param;
    				String value = request.getParameter(param);
    				fieldValueMap.put(field, value);
    			}
    		}

			client.hmset(key, fieldValueMap);
    		output.addProperty("out", "Success created key, with set of hashed fields and values");
    	}
    	client.close();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(output);
    }

    private static final long serialVersionUID = 1L;
}
