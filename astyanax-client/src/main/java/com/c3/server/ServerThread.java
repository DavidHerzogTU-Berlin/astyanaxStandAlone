package com.c3.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.c3.astyanax.AstyanaxClient;

public class ServerThread extends Thread{
	
	private Socket socket = null;
	public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
    }
    
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine, outputLine;
            AstyanaxClient ac = new AstyanaxClient();
            if(ac.init()) {
            	out.println("AstyanxClient INIT success.");
            	
            	 while ((inputLine = in.readLine()) != null) {
            		 
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
