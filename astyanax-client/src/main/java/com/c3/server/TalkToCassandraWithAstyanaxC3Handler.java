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

	@Override
	public boolean init() throws TException {
		astyanaxClient = new AstyanaxClient();
        System.out.println("init: " + Server.hostSelectorStrategy);
		return astyanaxClient.init(Server.discoveryType, Server.connectionPoolType, Server.seeds, Server.maxCons,
            Server.astyPort, Server.hostSelectorStrategy, Server.scoreStrategy, Server.map_size);
	}

	@Override
    public boolean write(String key, Map<String,String> values) throws TException {
    	if (astyanaxClient == null)
    		throw new TException("AstyanaxClient is null. Init needs to be called first.");
    	if (key == null || values == null)
    		throw new TException("key and values are not allowed to be null.");
    	HashMap<String, String> hashMap = 
   							(values instanceof HashMap) 
						      ? (HashMap) values 
						      : new HashMap<String, String>(values);
    	return astyanaxClient.insert(null, key, hashMap);
    }

    @Override
    public Map<String,String> read(String key, Set<String> fields) throws TException {
    	if (astyanaxClient == null)
    		throw new TException("AstyanaxClient is null. Init needs to be called first.");
    	if (key == null)
    		throw new TException("key is not allowed to be null.");
    	HashMap<String, String> result = new HashMap<String, String>();
    	astyanaxClient.read(null, key, fields, result);
    	return result;
    }

}
