package com.dkhenry.RethinkDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.dkhenry.RethinkDB.errors.RqlDriverException;

public class RqlObject {

	private Object _underlying;
	
	public String toString() {
		return _underlying.toString();			
	}
	public RqlObject() {
			
	}
	
	public RqlObject(Object d) {
		_underlying = d;
	}	
	
	public Boolean getBoolean() throws RqlDriverException {
		return as();
	}
	
	public Double getNumber() throws RqlDriverException {
		return as();
	}
	
	public String getString() throws RqlDriverException {
		return as();
	}
	
	public List<Object> getList() throws RqlDriverException {
		if (JSONObject.NULL.equals(_underlying)) {
			return new ArrayList<Object>();
		}
		return as();
	}
	
	public Map<String,Object> getMap() throws RqlDriverException {
		if (JSONObject.NULL.equals(_underlying)) {
			return new HashMap<String,Object>();
		}
		return as();
	}

    public Object get() throws RqlDriverException {
        return as();
    }
	
	@SuppressWarnings("unchecked")
	public <T> T as() throws RqlDriverException {
		return (T) Datum.deconstruct(_underlying);		
	}

	// The next few function will assume this is of type "RObject" 
	@SuppressWarnings("unchecked")
	public <T> T getAs(String key) throws RqlDriverException {
		Object as = as();
		if (as instanceof Map) {
			Map<String,Object> m = (Map<String,Object>)as;
			T val = (T) m.get(key);
			return val;
		}
		else {
			return (T)as;
		}
	}
	
	public <T> T getAsOrElse(String key, T orElse) throws RqlDriverException {
		T rval = getAs(key);
		if(null == rval) { 
			return orElse;
		}
		return rval;		
	}

}
