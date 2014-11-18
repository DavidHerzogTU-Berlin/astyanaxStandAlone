package com.c3.astyanax;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.AstyanaxContext;

public class AstyanaxClient {

	private ColumnFamily<String, String> EMP_CF;
	private static final String EMP_CF_NAME = "data";

	public static final String READ_CONSISTENCY_LEVEL_PROPERTY = "cassandra.readconsistencylevel";
	public static final String READ_CONSISTENCY_LEVEL_PROPERTY_DEFAULT = "ONE";

	public static final String WRITE_CONSISTENCY_LEVEL_PROPERTY = "cassandra.writeconsistencylevel";
	public static final String WRITE_CONSISTENCY_LEVEL_PROPERTY_DEFAULT = "ONE";

	public static final String NODE_DISCOVERY_PROPERTY = "discoveryType";
	public static final String NODE_DISCOVERY_PROPERTY_DEFAULT = "RING_DESCRIBE";

	public static final String CONNECTION_POOL_PROPERTY = "connectionPoolType";
	public static final String CONNECTION_POOL_PROPERTY_DEFAULT = "TOKEN_AWARE";

	public static final String SEED_PROPERTY = "seeds";
	public static final String SEED_PROPERTY_DEFAULT = "127.0.0.1:9160,127.0.0.2:9160,127.0.0.3:9160";

	public static final String MAXCONS_PROPERTY = "maxCons";
	public static final String MAXCONS_PROPERTY_DEFAULT = "100";

	public static final String PORT_PROPERTY = "port";
	public static final String PORT_PROPERTY_DEFAULT = "9160";
	
	public static final String HOST_SELECTOR_STRATEGY = "hostSelectorStrategy";
	public static final String HOST_SELECTOR_STRATEGY_DEFAULT = "ROUND_ROBIN";
	
	public static final String SCORE_STRATEGY = "scoreStrategy";
	public static final String SCORE_STRATEGY_DEFAULT = "continuous";
	
	public static final String MAP_SIZE = "map_size";
	public static final String MAP_SIZE_DEFAULT = "crash: map_size needs to be set";

	private static AstyanaxContext<Keyspace> context;
	private static Keyspace keyspace;

	public void init() {

	}
}