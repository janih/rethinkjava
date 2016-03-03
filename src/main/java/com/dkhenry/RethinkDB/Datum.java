package com.dkhenry.RethinkDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dkhenry.RethinkDB.errors.RqlDriverException;

public class Datum {
	/* Datum Constructors */

    public static Object datum() {
        return JSONObject.NULL;
    }
	/* We will specialize for all types defined in the protocol */
    public static Object datum(Boolean b) {    	
    	return b;
    }
    public static Object datum(String s) {
        return s;
    }
    public static Object datum(Double d) {
    	return d;
    }
    
    /* We want to cast all "Numbers" to Doubles */
    public static<T extends Number> Object datum(T n) {
    	return datum(n.doubleValue());
    }

    /* For any type we haven't specialized we are going to cast to a string */ 
    public static <T> Object datum(T t) {
        if( null == t ) {
            return datum();
        } else if(t instanceof Boolean) {
    		return datum((Boolean) t);
    	} else if( Number.class.isAssignableFrom(t.getClass()) ) {
    		return datum((Number) t);
    	} else if( t instanceof List) { 
    		return datum((List) t);
    	} else if( t instanceof Map) {
    		return datum((Map) t);
    	} else {
    		return datum(t.toString());
    	}
    }
    
    // The R Array
    public static <T> Object datum(List<T> a) {
    	JSONArray b = new JSONArray();
    	for(T value: a) {
    		b.put(datum(value));
    	}
    	return b;
    }
    
    // This is the R_OBJECT
    public static <K,V> Object datum(Map<K,V> h){
    	JSONObject b = new JSONObject();
    	for(Entry<K, V> entry: h.entrySet()) {
    		b.put(entry.getKey().toString(), datum(entry.getValue()));
    	}
    	return b;
    }

    public static Object deconstruct(Boolean d) throws RqlDriverException {
        return d;
    }

    public static Object deconstruct(Double d) throws RqlDriverException {
        return d;
    }
    
    public static Object deconstruct(String d) throws RqlDriverException {
        return d;
    }
    
    public static Object deconstruct(Map d) throws RqlDriverException {
        return d;
    }    

    private static List<Object> deconstructList(JSONArray d) throws RqlDriverException {
        List<Object> list = new ArrayList<Object>(); 
		for(int i = 0; i < d.length(); ++i) {
			list.add(deconstruct(d.get(i)));
		}
		return list;
    }

    private static Map<String,Object> deconstructMap(JSONObject d) throws RqlDriverException {
    	Map<String,Object> map = new HashMap<String, Object>();
        Iterator<String> keys = d.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            Object obj = d.get(key);
            map.put(key, deconstruct(obj));
		}
		return map;
    }
    
    public static Object deconstruct(Object d) throws RqlDriverException {
        if (d == null || d == JSONObject.NULL) {
            return null;
        } else if(d instanceof JSONObject) {
            return deconstructMap((JSONObject)d);
        } else if(d instanceof JSONArray) {
            return deconstructList((JSONArray)d);
        } else if(d instanceof Boolean) {
            return deconstruct((Boolean)d);
        } else if(d instanceof Number) {
            return deconstruct(new Double(((Number)d).doubleValue()));
        } else if(d instanceof String) {
            return deconstruct((String)d);
        } else {
            throw new RqlDriverException("Unknown Datum Type " + d.getClass().getName() + " presented for Deconstruction") ;
        }
    }
}
