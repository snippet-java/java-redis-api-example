package com.ibm.sample.student.redis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import redis.clients.jedis.Jedis;

public class RedisConnectionService {
	
	protected Jedis getConnection() {
		JsonObject credentials   = getCredentials();
	
		String hostname = credentials.get("hostname").getAsString();
		String password = credentials.get("password").getAsString();
		int port = credentials.get("port").getAsInt();
		
		Jedis client = new Jedis(hostname, port);
		client.auth(password);
		return client;
	}
	
	protected JsonObject getCredentials() {
	    //for local deployment
	    if(System.getenv("VCAP_SERVICES") == null || System.getenv("VCAP_SERVICES").isEmpty()) {
	    	return readPropertiesFile();
	    }

	    //for bluemix deployment
	    else {
			JsonParser parser = new JsonParser();
		    JsonObject allServices = parser.parse(System.getenv("VCAP_SERVICES")).getAsJsonObject();
			return ((JsonObject)allServices.getAsJsonArray("rediscloud").get(0)).getAsJsonObject("credentials");
	    }
		
	
	}
	
	/**
	 * For local deployment. To retrieve cloudant username and password from credential.properties
	 * @return credential JsonObject
	 */
	private JsonObject readPropertiesFile() {
		Properties prop = new Properties();
		InputStream input = null;
		JsonObject credentialsJson = new JsonObject();
		try {
			//read from current directory
			input = new FileInputStream("credential.properties");
			// load a properties file
			prop.load(input);

			// get the username and pasword from properties file
			credentialsJson.addProperty("hostname", prop.getProperty("hostname"));
			credentialsJson.addProperty("password", prop.getProperty("password"));
			credentialsJson.addProperty("port", prop.getProperty("port"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return credentialsJson;
	}
}
