package com.c3.astyanax;

import static org.junit.Assert.*;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;
import org.junit.Rule;
import org.junit.Test;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.Before;
import java.util.Iterator;

public class AstyanaxClientTest {
	@Test
	public void insertReadDeleteTest() {
		/**AstyanaxClient ac1 = new AstyanaxClient();
		if(ac1.init()) {
			System.out.println("Init worked");
			HashMap<String, String> values = new HashMap<String, String>();
			values.put("age", "57");
	    	values.put("middlename", "bradley");
	    	values.put("favoritecolor", "blue");
	    	assertTrue(ac1.insert("data","HansBradley", values));
	    	HashMap<String, String> result = new HashMap<String, String>();
	    	assertTrue( ac1.read("data", "HansBradley", null, result));
	    	System.out.println("Results for HansBradley coming: ");
			for (String column : result.keySet()) {
				System.out.println("Column: " + column + " Value: " + result.get(column) );
			}
		} else {
			System.out.println("Init failed"); 
		}**/
	}
}