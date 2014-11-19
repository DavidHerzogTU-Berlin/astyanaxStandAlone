package com.c3.astyanax;

import static com.netflix.astyanax.examples.ModelConstants.*;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.*;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.CqlResult;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.thrift.ThriftColumnFamilyQueryImpl;
import com.netflix.astyanax.query.IndexQuery;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.model.Rows;
import java.net.InetAddress;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Properties;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;
import com.netflix.astyanax.connectionpool.exceptions.OperationException;
import com.netflix.astyanax.connectionpool.Host;

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
	private String latencyScoreStrategy;

	public static final boolean OK = true;
	public static final boolean ERROR = false;

	public boolean init() {
		try {
			ConnectionPoolConfigurationImpl connectionPoolConfig = new ConnectionPoolConfigurationImpl(SCORE_STRATEGY_DEFAULT);
			latencyScoreStrategy = SCORE_STRATEGY_DEFAULT;
			if(latencyScoreStrategy.equals("continuous")) {
				connectionPoolConfig.setLatencyScoreStrategy(new EmaLatencyScoreContinuousStrategyImpl());
			} else {
				if(latencyScoreStrategy.equals("ema"))
					connectionPoolConfig.setLatencyScoreStrategy(new EmaLatencyScoreStrategyImpl(100));
				else
					connectionPoolConfig.setLatencyScoreStrategy(new SmaLatencyScoreStrategyImpl());
			}
			context = new AstyanaxContext.Builder()
					.forCluster("Test Cluster")
					.forKeyspace("usertable")
					.withAstyanaxConfiguration(
							new AstyanaxConfigurationImpl()
									.setDiscoveryType(
											NodeDiscoveryType.valueOf(NODE_DISCOVERY_PROPERTY_DEFAULT))
									.setConnectionPoolType(
											ConnectionPoolType.valueOf(CONNECTION_POOL_PROPERTY_DEFAULT))
									.setDefaultReadConsistencyLevel(
											ConsistencyLevel.valueOf("CL_"+ READ_CONSISTENCY_LEVEL_PROPERTY_DEFAULT))
									.setDefaultWriteConsistencyLevel(
											ConsistencyLevel
													.valueOf("CL_" + WRITE_CONSISTENCY_LEVEL_PROPERTY_DEFAULT))
									.setCqlVersion("3.1.0").setTargetCassandraVersion("2.0"))
					.withConnectionPoolConfiguration(
							connectionPoolConfig
									.setPort(
											Integer.valueOf(PORT_PROPERTY_DEFAULT))
									.setMaxConnsPerHost(
											Integer.valueOf(MAXCONS_PROPERTY_DEFAULT))
									.setSeeds(SEED_PROPERTY_DEFAULT)
									.setHostSelectorStrategy(
											HostSelectorStrategy.valueOf(HOST_SELECTOR_STRATEGY_DEFAULT)))
					.withConnectionPoolMonitor(
							new CountingConnectionPoolMonitor())
					.buildKeyspace(ThriftFamilyFactory.getInstance());
			
			context.start();
			keyspace = context.getEntity();

			EMP_CF = ColumnFamily.newColumnFamily(EMP_CF_NAME,
					StringSerializer.get(), StringSerializer.get());
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return OK;
	}

	public boolean insert(String table, String key,
		HashMap<String, String> values) {
		MutationBatch m = keyspace.prepareMutationBatch();
		try {
			for (Entry<String, String> entry : values.entrySet()) {
				m.withRow(EMP_CF, key)
						.putColumn(entry.getKey(), entry.getValue(),
								null).setTimestamp(System.currentTimeMillis());
			}

			OperationResult<Void> result = m.execute();

		} catch (ConnectionException e) {
			System.out.println(e);
			return ERROR;
		}
		return OK;
	}

	public boolean read(String table, String key, Set<String> fields,
			HashMap<String, String> result) {
		try {
			if (fields == null) {
				final OperationResult<ColumnList<String>> opresult = keyspace
						.prepareQuery(EMP_CF).getKey(key).execute();
				
				ColumnList<String> columns  = opresult.getResult();
				for (String s : columns.getColumnNames()) {
					result.put(s, columns.getColumnByName(s).getStringValue());
				} 
				
			} else {
				final OperationResult<ColumnList<String>> opresult = keyspace
						.prepareQuery(EMP_CF).getKey(key)
						.withColumnSlice(fields).execute();
				
				ColumnList<String> columns  = opresult.getResult();
				for (String s : columns.getColumnNames()) {
					result.put(s, columns.getColumnByName(s).getStringValue());
				} 
			}
			return OK;

		} catch (Throwable e) {
			e.printStackTrace();
			return ERROR;
		}

	}

}