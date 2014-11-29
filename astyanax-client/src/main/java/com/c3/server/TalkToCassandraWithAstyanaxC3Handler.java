package com.c3.server;

import org.apache.thrift.TException;
import java.util.Set;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;
import com.c3.astyanax.AstyanaxClient;
import org.apache.astyanax.thrift.TalkToCassandraWithAstyanaxC3;

public class TalkToCassandraWithAstyanaxC3Handler implements TalkToCassandraWithAstyanaxC3.Iface {

	private AstyanaxClient astyanaxClient; 

	public void TalkToCassandraWithAstyanaxC3Handler() {
		astyanaxClient = new AstyanaxClient();
	}

	@Override
	public boolean init() throws TException {
		return astyanaxClient.init();
	}

	@Override
    public boolean write(String key, Map<String,String> values) throws TException {
    	HashMap<String, String> hashMap = 
   							(values instanceof HashMap) 
						      ? (HashMap) values 
						      : new HashMap<String, String>(values);
    	return astyanaxClient.insert(null, key, hashMap);
    }

    @Override
    public Map<String,String> read(String key, Set<String> fields) throws TException {
    	HashMap<String, String> result = new HashMap<String, String>();
    	astyanaxClient.read(null, key, fields, result);
    	return result;
    }

}
