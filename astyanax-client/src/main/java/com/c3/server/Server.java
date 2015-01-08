package com.c3.server;

import org.apache.astyanax.thrift.TalkToCassandraWithAstyanaxC3;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class Server {
	public static TalkToCassandraWithAstyanaxC3Handler handler;

  public static TalkToCassandraWithAstyanaxC3.Processor processor;
  private static int port = 2345;
  public static String discoveryType = null;
  public static String connectionPoolType = null;
  public static String seeds = null;
  public static String maxCons = null;
  public static String astyPort = null;
  public static String hostSelectorStrategy = null;
  public static String scoreStrategy = null;
  public static String map_size;
  public static void main(String [] args) {

    try {
		if (args.length == 1){
			port = Integer.parseInt(args[0]);
		} else {
			if (args.length > 1) {
				port = Integer.parseInt(args[0]);
				String valueOfArg = null;
				for (int i = 1; i < args.length; i++) {
					if(args[i].contains("discoveryType=")){
						discoveryType = args[i].split("=")[1];
					}
					if(args[i].contains("connectionPoolType=")){
						connectionPoolType = args[i].split("=")[1];
					}
					if(args[i].contains("seeds=")){
						seeds = args[i].split("=")[1];
					}
					if(args[i].contains("maxCons=")){
						maxCons = args[i].split("=")[1];
					}
					if(args[i].contains("astyPort=")){
						astyPort = args[i].split("=")[1];
					}
					if(args[i].contains("hostSelectorStrategy=")){
						hostSelectorStrategy = args[i].split("=")[1];
					}
					if(args[i].contains("scoreStrategy=")){
						scoreStrategy = args[i].split("=")[1];
					}
				}
			} else {
				System.out.println("No argument set: Using default port 2345");
			}
			
		}

		handler = new TalkToCassandraWithAstyanaxC3Handler();
		processor = new TalkToCassandraWithAstyanaxC3.Processor(handler);

		Runnable simple = new Runnable() {
			public void run() {
				simple(processor);
			}
		};      

		new Thread(simple).start();
		} catch (Exception x) {
			x.printStackTrace();
		}
  }

  public static void simple(TalkToCassandraWithAstyanaxC3.Processor processor) {
    try {
      TServerTransport serverTransport = new TServerSocket(port);
      TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

      System.out.println("Server is running and listening...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}