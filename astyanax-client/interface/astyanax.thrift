namespace java org.apache.astyanax.thrift

service TalkToCassandraWithAstyanaxC3
{
	bool init(),
	bool write(1:string key, 2:map<string, string> values),
	map<string, string> read(1:string key, 2:set<string> fields),
}