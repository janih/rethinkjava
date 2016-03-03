package com.dkhenry.RethinkDB;

import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rethinkdb.Ql2;

public class Response {
	public static final String RESPONSE = "r"; 
	public static final String TYPE = "t";
	private JSONArray r;
	private Integer t;
	private long token;
	
	public Response(long token, byte[] array) {
		this.token = token;
		String response = new String(array, Charset.forName("UTF-8"));
		JSONObject obj = new JSONObject(response);
		r = (JSONArray)obj.get(RESPONSE);
		t = (Integer)obj.get(TYPE);
	}
	
	public long getToken() {
		return token;
	}

	public Ql2.Response.ResponseType getType() {
		return Ql2.Response.ResponseType.valueOf(t);
	}
	
	public int getResponseCount() {
		return r.length();
	}
	
	public Object getResponse(int index) {
		if (r.length() > 0) {
			Object obj = r.get(index);
			if (JSONObject.NULL.equals(obj)) {
				return obj;
			}
            else if (obj instanceof JSONObject) {
				return (JSONObject)obj;
			}
			else if (obj instanceof JSONArray) {
				return (JSONArray)obj;
			}
			else if (obj instanceof Integer) {
				return (Integer)obj;
			}
			else if (obj instanceof Long) {
				return (Long)obj;
			}
			else if (obj instanceof Double) {
				return (Double)obj;
			}
			else if (obj instanceof Boolean) {
				return (Boolean)obj;
			}
			else if (obj instanceof String) {
				return (String)obj;
			}
		}
		return r;
	}
}
