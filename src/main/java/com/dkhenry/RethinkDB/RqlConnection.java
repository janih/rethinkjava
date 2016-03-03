package com.dkhenry.RethinkDB;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dkhenry.RethinkDB.errors.RqlDriverException;
import com.rethinkdb.Ql2.Query;

public class RqlConnection {
	private SocketChannel _sc;    
	private String _hostname;
	private int _port; 
	private boolean _connected;

    //! Flag to indicate if this is a secured connection
    private boolean _secured;

    //! The authorization key for this connection
    private String _authKey;
 
	//! A global counter for the request tokens; 
	private static AtomicLong counter = new AtomicLong(0);

	public RqlConnection() { 
		_connected = false;
        _secured = false;
	}

	public String get_hostname() { return _hostname; }
	public void set_hostname(String hostname) throws RqlDriverException {
        set_hostname2(hostname,true);
    }
    public void set_hostname2(String hostname, boolean reconnect) throws RqlDriverException {
		String ohostname = _hostname;
		_hostname = hostname; 
		if(reconnect && _connected && hostname != ohostname) {
			reconnect();
		}
	}

	public int get_port() { return _port; }
	public void set_port(int port) throws RqlDriverException {
        set_port2(port,true);
    }
    public void set_port2(int port, boolean reconnect) throws RqlDriverException {
		int oport = _port; 
		_port = port; 
		if(reconnect && _connected && oport != port) {
			reconnect();
		}
	}

    public String get_authKey() { return _authKey; }
    public void set_authKey(String authKey) throws RqlDriverException {
        set_authKey2(authKey, true);
    }
    public void set_authKey2(String authKey, boolean reconnect) throws RqlDriverException {
        String okey = _authKey;
        _authKey = authKey;
        _secured = (authKey != null) ;

        if(reconnect && _connected && okey != authKey) {
            reconnect();
        }
    }

	public void close() throws RqlDriverException { 
		if( _connected ) {
			try { 
				_sc.close();
			} catch (IOException ex) { 
				throw new RqlDriverException(ex.getMessage());				
			}
			_connected = false; 
		}
	}
    public RqlCursor run(RqlQuery query) throws RqlDriverException {
        return run(query,null);
    }

    private void sendQueryArray(JSONArray q, long token) throws RqlDriverException {
        String qString = q.toString();
        int qLength = qString.getBytes().length;

		ByteBuffer buffer = ByteBuffer.allocate(qLength + 8 + 4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putLong(token);
		buffer.putInt(qLength);
		buffer.put(qString.getBytes());

		try {
			send_raw(buffer.array());
		} catch (IOException ex) { 
			throw new RqlDriverException(ex.getMessage());
		}
    }

    public RqlCursor run(RqlQuery query, HashMap<String,Object> optargs) throws RqlDriverException {
        JSONArray q = new JSONArray();
		// The format is [QueryType, Term, Optargs]
		q.put(Query.QueryType.START.getNumber());
		q.put(query.build());

        JSONObject oa = new JSONObject();
        if (optargs != null) {
            for(Map.Entry<String,Object> e: optargs.entrySet()) {
                oa.put(e.getKey(), RqlQuery.eval(e.getValue()).build());
            }
        }
        q.put(oa);

        sendQueryArray(q, nextToken());
		Response rsp = get(); 

		// For this version we only support success :-(
		switch(rsp.getType()) {
			case SUCCESS_ATOM:
			case SUCCESS_SEQUENCE:
			case SUCCESS_PARTIAL:
				return new RqlCursor(this, rsp);
			case CLIENT_ERROR:
			case COMPILE_ERROR:
			case RUNTIME_ERROR:
		default:
			throw new RqlDriverException(rsp.toString());							
		}							
	}
	
	public Response get() throws RqlDriverException {
		try {
			return recv_raw();
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RqlDriverException(ex.getMessage());
		}
	}
	
	public Response get_more(long token) throws RqlDriverException {
		// Send the [CONTINUE] query 
		JSONArray q = new JSONArray();
		q.put(Query.QueryType.CONTINUE.getNumber());
		sendQueryArray(q, token);
		try {
	        return recv_raw();
	    } catch (IOException ex) { 
			throw new RqlDriverException(ex.getMessage());
		}
	}
	
	/* Utility functions to make a pretty API */
	public RqlQuery.Table table(Object... args) { 
		RqlQuery.Table rvalue =  new RqlQuery.Table(args);
		return rvalue; 
	}
	
	public RqlTopLevelQuery.DB db(Object... args) {
		RqlTopLevelQuery.DB rvalue = new RqlTopLevelQuery.DB(args);
		return rvalue;
	}
	
	public RqlTopLevelQuery.DbCreate db_create(Object... args) { 
		RqlTopLevelQuery.DbCreate rvalue = new RqlTopLevelQuery.DbCreate(args);
		return rvalue;
	}
	
	public RqlTopLevelQuery.DbDrop db_drop(Object... args) { 
		RqlTopLevelQuery.DbDrop rvalue = new RqlTopLevelQuery.DbDrop(args);
		return rvalue;
	}
	
	public RqlTopLevelQuery.DbList db_list(Object... args) {
		RqlTopLevelQuery.DbList rvalue = new RqlTopLevelQuery.DbList(args);
		return rvalue;
	}

	public RqlTopLevelQuery.Branch branch(Object... args) {
		RqlTopLevelQuery.Branch rvalue = new RqlTopLevelQuery.Branch(args);
		return rvalue;
	}
		
	/* Private methods */ 
	private void reconnect() throws RqlDriverException{ 
		try {
			if( _connected ) {
				_sc.close();
			}
			_sc = SocketChannel.open();
			// Disable Nagle's algorithm for better performance 
		    _sc.setOption(StandardSocketOptions.TCP_NODELAY, true);
			_sc.configureBlocking( true );
			_sc.connect(new InetSocketAddress(_hostname,_port));

            // magic value + auth key length + protocol type
            int requiredSize = 4 + 4 + 4;
            if( _secured ) {
                requiredSize += _authKey.length();
            }
            ByteBuffer buffer = ByteBuffer.allocate(requiredSize);
			buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.putInt(com.rethinkdb.Ql2.VersionDummy.Version.V0_3_VALUE);
            if ( _secured ) {
                buffer.putInt(_authKey.length());
                buffer.put(_authKey.getBytes());
            } else {
                buffer.putInt(0);
            }
            buffer.putInt(com.rethinkdb.Ql2.VersionDummy.Protocol.JSON_VALUE);

			buffer.flip();
			while(buffer.hasRemaining()) { 
				_sc.write(buffer);
			}

            // Get the response string
            ByteBuffer response = ByteBuffer.allocate(4096);
            _sc.read(response);
            String s = new String(response.array(), Charset.forName("UTF-8"));
            if(s.equals("SUCCESS")) {
                throw new RqlDriverException(s);
            }

			_connected = true;
		} catch (IOException ex) { 
			throw new RqlDriverException(ex.getMessage());				
		}

	}

	public long nextToken() { 
		return counter.incrementAndGet();
	}
	
	public void send_raw( byte[] data ) throws IOException { 
		rethink_send(_sc,data); 
	}

	public Response recv_raw() throws IOException { 
		return rethink_recv(_sc); 
	}
	
	public static RqlConnection connect(String hostname, int port) throws RqlDriverException { 
		return connect2(hostname,port,null);
	}

    public static RqlConnection connect(String hostname, int port, String key) throws RqlDriverException {
        return connect2(hostname,port,key);
    }

    private static RqlConnection connect2(String hostname, int port, String key) throws RqlDriverException {
        RqlConnection r = new RqlConnection();
        r.set_hostname2(hostname, false);
        r.set_port2(port, false);
        if( null != key ) {
            r.set_authKey2(key, false);
        }
        r.reconnect();
        return r;
    }

	public static void rethink_send(SocketChannel sc, byte[] data) throws IOException {
		ByteBuffer buffer = ByteBuffer.wrap(data);

		while(buffer.hasRemaining()) { 
			sc.write(buffer); 
		}
	}

	public static Response rethink_recv(SocketChannel sc) throws IOException {
		ByteBuffer header = ByteBuffer.allocate(8 + 4); 
		header.order(ByteOrder.LITTLE_ENDIAN);
		int bytesRead = 0;
		while( bytesRead != 8 + 4 ){ 
			 bytesRead += sc.read(header);
		}
		header.flip();
		long token = header.getLong();
		int len = header.getInt();

		ByteBuffer buf = ByteBuffer.allocate(len);
		bytesRead = 0;
		while( bytesRead != len ){ 
			 bytesRead += sc.read(buf);		
		}
		buf.flip();
		return new Response(token, buf.array());
	}
}
