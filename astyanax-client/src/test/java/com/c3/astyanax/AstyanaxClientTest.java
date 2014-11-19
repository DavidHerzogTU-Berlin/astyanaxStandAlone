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
		AstyanaxClient ac1 = new AstyanaxClient();
		if(ac1.init()) {
			HashMap<String, String> values = new HashMap<String, String>();
			values.put("age", "57");
	    	values.put("middlename", "bradley");
	    	values.put("favoritecolor", "blue");
	    	assertTrue(ac1.insert("data","HansBradley", values));
		} else {
			System.out.println("Init failed"); 
		}
	}
}