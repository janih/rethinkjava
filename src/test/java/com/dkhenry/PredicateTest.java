package com.dkhenry;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.dkhenry.RethinkDB.Function;
import com.dkhenry.RethinkDB.RqlConnection;
import com.dkhenry.RethinkDB.RqlCursor;
import com.dkhenry.RethinkDB.RqlQuery;
import com.dkhenry.RethinkDB.errors.RqlDriverException;

@RunWith(JUnit4.class)
public class PredicateTest {

    @Test(groups={"acceptance"})
    public void testFunction1() throws RqlDriverException {
        RqlConnection r = RqlConnection.connect("localhost",28015);

        RqlQuery query = RqlQuery.eval(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)).map( new Function() {


            @Override
            public RqlQuery apply(RqlQuery.Var var1) {
                return var1.add(1);
            }
        });

        RqlCursor cursor = r.run(query);
        AssertJUnit.assertEquals(Arrays.asList(2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,11.0), cursor.next().getList());


    }
}


