package com.c3.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import com.c3.astyanax.AstyanaxClient;

public class ServerThread extends Thread{
	
	private Socket socket = null;
	public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
    }
	
    private HashMap<String, String> makeReadValues(String[] inputCommand) {
    	HashMap<String, String> values = new HashMap<String, String>();
    	for(int i = 2; i < inputCommand.length; i++) {
    		String[] valuePair = inputCommand[i].split("§");
    		if (valuePair.length == 2)
    			values.put(valuePair[0], valuePair[1]);
    		else 
    			System.out.println("false value pair: " +  valuePair);
    	}
    	return values;
    }
    
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine;
            AstyanaxClient ac = new AstyanaxClient();
            if(ac.init()) {
            	out.println("AstyanxClient INIT success.");
            	
            	 while ((inputLine = in.readLine()) != null) {
            		 String [] inputCommand = inputLine.split("\\|");
            		 if(inputCommand.length >= 2 ) {
            			 if (inputCommand[0].equals("read")) {
            				HashMap<String, String> result = new HashMap<String, String>();
            				if( ac.read(null, inputCommand[1], null, result)) {
            					String readResult = "";
            					for (String column : result.keySet()) {
            						readResult = readResult + "Column: " + column + " Value: " + result.get(column) + " | ";
            					}
            					out.println("read:success: result: " + readResult);
            				}
            					
            				else 
            					out.println("read:failor");
            			 } else {
            				 if (inputCommand[0].equals("write")) {
            					 if( ac.insert(null, inputCommand[1], makeReadValues(inputCommand)))
                					out.println("write:success");
            					 else 
                					out.println("write:failor");
                			 }
            			 }
            			 
            		 } else {
            			 out.println("Wrong pattern use: read/write;yourKey;valuePair-valuePair");
            		 }
                     if (inputLine.equals("close"))
                         break;
                 }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
