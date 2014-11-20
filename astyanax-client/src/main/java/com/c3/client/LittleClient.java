package com.c3.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class LittleClient {
public static void main(String[] args) throws IOException {
        
	if (args.length != 2) {
        System.err.println(
            "Usage (args): <host name> <port number>");
        System.exit(1);
    }

    String serverIP = args[0];
    int portNumber = Integer.parseInt(args[1]);

        try (
            Socket socket = new Socket(serverIP, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
            System.out.println("Connecting to server...");
            System.out.println("Type your command in the command line after the server responded.");
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);    
                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Me: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about server " + serverIP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                serverIP);
            System.exit(1);
        }
    }
}