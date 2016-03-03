package com.dkhenry;

import java.util.Arrays;
import java.util.Map;

import org.json.JSONObject;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.dkhenry.RethinkDB.Datum;

public class DatumTest {

	@Test(groups = { "acceptance" })
	public void testDatumNull() throws Exception {
		AssertJUnit.assertEquals(null, Datum.deconstruct((JSONObject) null));
		AssertJUnit.assertEquals(null, Datum.deconstruct(JSONObject.NULL));
		AssertJUnit.assertEquals(null, Datum.deconstruct((Boolean) null));
		AssertJUnit.assertEquals(null, Datum.deconstruct((Double) null));
		AssertJUnit.assertEquals(null, Datum.deconstruct((String) null));
		AssertJUnit.assertEquals(null, Datum.deconstruct((Map) null));
	}

	@Test(groups = { "acceptance" })
	public void testDatumBool() throws Exception {
		AssertJUnit.assertEquals(true, Datum.deconstruct(true));
		AssertJUnit.assertEquals(true, Datum.deconstruct(Boolean.TRUE));
		AssertJUnit.assertEquals(false, Datum.deconstruct(Boolean.FALSE));
	}

	@Test(groups = { "acceptance" })
	public void testDatumNumber() throws Exception {
		AssertJUnit.assertEquals(new Double(1.0), Datum.deconstruct(new Double(1.0)));
		AssertJUnit.assertEquals(1.0, Datum.deconstruct(1.0));
	}

	@Test(groups = { "acceptance" })
	public void testDatumString() throws Exception {
		AssertJUnit.assertEquals("SuperAwesomeTest", Datum.deconstruct("SuperAwesomeTest"));
		AssertJUnit.assertEquals("", Datum.deconstruct(""));
	}

	@Test(groups = { "acceptance" })
	public void testDatumArraySimple() throws Exception {
		Object array = Datum.datum(Arrays.asList("foo", "bar", "1", "2"));
		AssertJUnit.assertEquals(Arrays.asList("foo", "bar", "1", "2"), Datum.deconstruct(array));
	}

	@Test(groups = { "acceptance" })
	public void testDatumArrayMixed() throws Exception {
		Object array = Datum.datum(Arrays.asList(true, 1.0, "SuperAwesomeTest"));
		AssertJUnit.assertEquals(Arrays.asList(true, 1.0, "SuperAwesomeTest"), Datum.deconstruct(array));
	}

}
