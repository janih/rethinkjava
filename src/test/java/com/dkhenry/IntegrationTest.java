package com.dkhenry;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.dkhenry.RethinkDB.RqlConnection;
import com.dkhenry.RethinkDB.RqlCursor;
import com.dkhenry.RethinkDB.RqlObject;
import com.dkhenry.RethinkDB.errors.RqlDriverException;

public class IntegrationTest {
	public static final String DB_SERVER_HOST = "localhost";
	public static final int DB_SERVER_PORT = 28015;
	public static final String ATTR_GENERATED_KEYS = "generated_keys";
	public static final String ATTR_REPLACED = "replaced";
	public static final String ATTR_UNCHANGED = "unchanged";
	public static final String ATTR_SKIPPED = "skipped";
	public static final String ATTR_ERRORS = "errors";
	public static final String ATTR_DBS_CREATED = "dbs_created";
	public static final String ATTR_TABLES_CREATED = "tables_created";
	public static final String ATTR_CREATED = "created";
	public static final String ATTR_DELETED = "deleted";
	
	@Test(groups={"acceptance"})
	public void createAndListDb() throws RqlDriverException {		 
		SecureRandom random = new SecureRandom();
		String database = new BigInteger(130, random).toString(32);
		RqlConnection r = RqlConnection.connect(DB_SERVER_HOST, DB_SERVER_PORT);
		RqlCursor cursor = r.run(r.db_create(database));
		RqlObject obj = cursor.next();					
		assert Double.valueOf(1.0).equals(obj.getAs(ATTR_DBS_CREATED)) : "Database was not created successfully ";
		cursor = r.run(r.db_list());
		obj = cursor.next();
		boolean found = false;
		for(Object o: obj.getList()) {
			if( database.equals(o)) { 
				found = true;
				break; 
			}				
		}
		assert found == true : "Database was not able to be listed";
		cursor = r.run(r.db_drop(database));
		obj = cursor.next(); 
		assert Double.valueOf(1.0).equals(obj.getAs("dbs_dropped")) : "Database was not dropped successfully ";
		r.close();
	}

	@Test(groups={"acceptance"})
	public void createAndListTable() throws RqlDriverException { 
		SecureRandom random = new SecureRandom();
		String database = new BigInteger(130, random).toString(32);
		String table = new BigInteger(130, random).toString(32);
		RqlConnection r = RqlConnection.connect(DB_SERVER_HOST,DB_SERVER_PORT);
		r.run(r.db_create(database));
		RqlCursor cursor = r.run(r.db(database).table_create(table));
		assert Double.valueOf(1.0).equals(cursor.next().getAs(ATTR_TABLES_CREATED)) : "Table was not created successfully ";		
		cursor = r.run(r.db(database).table_list());
		boolean found = false; 
		for(Object o: cursor.next().getList()) { 
			if(table.equals(o)) {
				found = true; 
				break; 
			}
		}
		assert found == true : "Table was not able to be listed";
		cursor = r.run(r.db(database).table_drop(table));
		assert Double.valueOf(1.0).equals(cursor.next().getAs("tables_dropped")) : "Table was not dropped successfully ";		
		r.run(r.db_drop(database)); 
		r.close();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	@Test(groups={"acceptance"})
	public void insertAndRetrieveData() throws RqlDriverException {
		SecureRandom random = new SecureRandom();
		String database = new BigInteger(130, random).toString(32);
		String table = new BigInteger(130, random).toString(32);
		RqlConnection r = RqlConnection.connect(DB_SERVER_HOST,DB_SERVER_PORT);
		r.run(r.db_create(database));
		r.run(r.db(database).table_create(table));
		
		RqlCursor cursor = r.run(r.db(database).table(table).insert( Arrays.asList(				
				new HashMap() {{ put("name","Worf");put("show","Star Trek TNG"); }},
			    new HashMap() {{ put("name","Data");put("show","Star Trek TNG"); }},
			    new HashMap() {{ put("name","William Adama");put("show","Battlestar Galactica"); }}, 
			    new HashMap() {{ put("name","Homer Simpson");put("show","The Simpsons"); }}
		)));
		assert Double.valueOf(4.0).equals(cursor.next().getAs("inserted")) : "Error inserting Data into Database";
		cursor = r.run(r.db(database).table(table).filter(new HashMap() {{ put("show","Star Trek TNG"); }}));
		// We Expect Two results 
		int count = 0; 		
		for(RqlObject o: cursor) {
			Map<String,Object> m = o.getMap(); 
			assert m.containsKey("name") : "Data that came back was malformed (missing \"name\")";
			assert m.containsKey("show") : "Data that came back was malformed (missing \"show\")";
			assert "Star Trek TNG".equals(m.get("show")): "Data that came back was just plain wrong (\"show\" was not \"Star Trek TNG\")";
			count++; 		
		}
		cursor = r.run(r.db(database).table(table).filter(new HashMap() {{ put("name","donald duck");put("show","Disney show"); }}).is_empty());
		Boolean isEmpty = null;
		for(RqlObject o: cursor) {
			isEmpty = o.getBoolean();
		}		
		assert isEmpty == true : "Failed at verifying query result set is empty.";
		
		cursor = r.run(r.db(database).table(table).count());
		double rowCount = 0;
		for(RqlObject o: cursor) {
			rowCount = o.getNumber();
		}		
		assert rowCount == 4.0 : "Failed at getting the correct row count.";	

        cursor = r.run(r.db(database).table(table).filter(new HashMap() {{ put("name", "Worf"); }}).update(new HashMap() {{ put("show", "Star Trek Deep Space Nine"); }}));
        assert Double.valueOf(1.0).equals(cursor.next().getAs("replaced")) : "Error updating Data in Database";
        cursor = r.run(r.db(database).table(table).filter(new HashMap() {{ put("name","Worf"); }}));
        for(RqlObject o: cursor) {
            Map<String,Object> m = o.getMap();
            assert m.containsKey("name") : "Data that came back was malformed (missing \"name\")";
            assert m.containsKey("show") : "Data that came back was malformed (missing \"show\")";
            assert "Star Trek Deep Space Nine".equals(m.get("show")) : "Data that came back was just plain wrong (\"show\" was not \"Star Trek Deep Space Nine\")";
        }

        r.run(r.db(database).table_drop(table));
        r.run(r.db_drop(database));		
		r.close();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	@Test(groups={"acceptance"})
	public void insertAndRetrieveSingleRow() throws RqlDriverException {
		SecureRandom random = new SecureRandom();
		String database = new BigInteger(130, random).toString(32);
		String table = new BigInteger(130, random).toString(32);
		RqlConnection r = RqlConnection.connect(DB_SERVER_HOST,DB_SERVER_PORT);
		r.run(r.db_create(database));
		r.run(r.db(database).table_create(table));
		
		RqlCursor cursor = r.run(r.db(database).table(table).insert( Arrays.asList(				
				new HashMap() {{ put("name","Worf");put("show","Star Trek TNG"); }},
			    new HashMap() {{ put("name","Data");put("show","Star Trek TNG"); }},
			    new HashMap() {{ put("name","William Adama");put("show","Battlestar Galactica"); }}, 
			    new HashMap() {{ put("name","Homer Simpson");put("show","The Simpsons"); }}
		        ),
                new HashMap() {{ put("durability","hard"); put("return_changes",false);  put("conflict","update"); }}
         ));
		
		List generatedKeys = cursor.next().getAs("generated_keys");
		
		Iterator it = generatedKeys.iterator();
		
		while (it.hasNext()){
			cursor = r.run(r.db(database).table(table).get(it.next()));
			for(RqlObject o: cursor) {
				assert o.toString() != null : "Failed to get single row";
			}				
		}
		
        r.run(r.db(database).table_drop(table));
        r.run(r.db_drop(database));		
		r.close();
	}


    @SuppressWarnings({ "unchecked", "rawtypes", "serial" })
    @Test(groups={"benchmark"})
    public void largeDataSetTest() throws RqlDriverException {
        final double rowsPerIteration = 8192.0;
        final double numberOfIterations = 256.0;
        final SecureRandom random = new SecureRandom();
        String database = new BigInteger(130, random).toString(32);
        String table = new BigInteger(130, random).toString(32);
        RqlConnection r = RqlConnection.connect(DB_SERVER_HOST,DB_SERVER_PORT);
        r.run(r.db_create(database));
        r.run(r.db(database).table_create(table));

        for( long j =0 ; j < numberOfIterations ; j++ ) {
            List<Object> l = new ArrayList<Object>();
            System.out.println("Inserting Rows...") ;
            for( long i = 0 ; i < rowsPerIteration ; i++ ) {
                final long id = (i* (long)rowsPerIteration)+j;
                l.add(
                        new HashMap() {{
                            put("id",BigInteger.valueOf(id).toString(32) );
                            put("name",new BigInteger(64,random).toString(32));
                        }}
                );
            }
            RqlCursor cursor = r.run(r.db(database).table(table).insert( l ));
            RqlObject obj = cursor.next();
            Double result = obj.getAs("inserted");
            assert result == rowsPerIteration : "Error inserting Data into Database on iteration " + j + " (" + result + " did not equal " + rowsPerIteration +")";
        }
        RqlCursor cursor = r.run(r.db(database).table(table).count());
        assert Double.valueOf(numberOfIterations*rowsPerIteration).equals(cursor.next().getNumber()) : "Error getting large row count";

        cursor = r.run(r.db(database).table(table));
        long rowCount = 0;
        for(RqlObject o: cursor) {
            rowCount++;
        }
        assert rowCount == (rowsPerIteration * numberOfIterations) : "Error fetching all rows in large dataset";
        System.out.println("We got " + rowCount + " results back ");

        r.run(r.db(database).table_drop(table));
        r.run(r.db_drop(database));
        r.close();
    }
    
    @Test(groups={"acceptance"})
    public void smokeTest() throws RqlDriverException {
		RqlConnection r = RqlConnection.connect(DB_SERVER_HOST, DB_SERVER_PORT);
		
		SecureRandom random = new SecureRandom();
		String dbName = new BigInteger(130, random).toString(32);
		String tableName = new BigInteger(130, random).toString(32);
		
		// create db
		RqlCursor c = r.run(r.db_create(dbName));
		Double count = c.next().getAs(ATTR_DBS_CREATED);
		assert Double.valueOf(1).equals(count) : "Error getting created dbs count";
		
		// loop databases
		c = r.run(r.db_list());
		RqlObject obj = c.next();
		boolean found = false;
		for(Object o: obj.getList()) {
			if( dbName.equals(o)) { 
				found = true;
				break; 
			}				
		}
		assert found : "db not found";
		
		// create table
		c = r.run(r.db(dbName).table_create(tableName));
		count = c.next().getAs(ATTR_TABLES_CREATED);
		assert Double.valueOf(1).equals(count) : "Error getting created table count";
		
		// insert data
		for (int i = 0; i < 20; i++) {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("integer", i);
			values.put("date", System.currentTimeMillis());
			values.put("double", 1.2+i);
			values.put("string", ("valuevaluevalue"+i));
			c = r.run(r.db(dbName).table(tableName).insert(values));
			List<String> ids = c.next().getAs(ATTR_GENERATED_KEYS);
			assert ids.size() > 0 : "Error getting generated keys";
		}
		
		// count data
		c = r.run(r.db(dbName).table(tableName).count());
		for(RqlObject o: c) {
			count = o.getNumber();
		}
		assert count == 20 : "Error getting correct row count";
		
		// try paging data
		c = r.run(r.db(dbName).table(tableName).skip(0).limit(10));
		int skipCount = 0;
		for(RqlObject o : c) {
			skipCount++;
		}
		assert skipCount == 10 : "skipCount incorrect";
		
		// fetch data
		c = r.run(r.db(dbName).table(tableName));
		String id = null;
		for(RqlObject o : c) {
			id = o.getAs("id");
			assert id != null : "id not found";
			Double intVal = o.getAs("integer");
			assert intVal != null : "intVal not found";
			Double dateVal = o.getAs("date");
			assert dateVal != null : "dateVal not found";
			Double doubleVal = o.getAs("double");
			assert doubleVal != null : "doubleVal not found";
			String stringVal = o.getAs("string");
			assert stringVal != null : "stringVal not found";
		}
		
		// update data
		Map<String, Object> updateValues = new HashMap<String, Object>();
		updateValues.put("integer", 2);
		updateValues.put("date", System.currentTimeMillis());
		updateValues.put("double", 1.4);
		updateValues.put("string", "valueupdate");
		c = r.run(r.db(dbName).table(tableName).get(id).update(updateValues));
		
		// fetch updated data
		c = r.run(r.db(dbName).table(tableName));
		for(RqlObject o : c) {
			assert o != null : "updated object is null";
		}
		
		// test getting data with bogus id
		c = r.run(r.db(dbName).table(tableName).get("dummyid"));
		for(RqlObject o: c) {
			Map<String,Object> m = o.getMap();
			assert m != null : "should return empty map";
		}
		
		// delete data
		c = r.run(r.db(dbName).table(tableName).get(id).delete());
		for(RqlObject o: c) {
			double deleted = o.getAs(ATTR_DELETED);
			assert deleted == 1.0 : "should delete one item";
		}
		
		// verify data deleted
		c = r.run(r.db(dbName).table(tableName).count());
		for(RqlObject o: c) {
			count = o.getNumber();
		}
		assert count == 19 : "Error getting correct row count";
		
		// drop db and close connection
		r.run(r.db_drop(dbName));
		r.close();
    }
    
}
