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
  public static void main(String [] args) {

    try {
		if (args.length == 1){
			port = Integer.parseInt(args[0]);
		} else {
			System.out.println("No argument set: Using default port 2345");
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