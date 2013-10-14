package com.sirolf2009.monopolie.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.sirolf2009.monopolie.Host;

public class Connector implements Runnable {

	private boolean isRemote;
	
	private ServerSocket serverSocket;

	public Connector(Socket socket) {
		isRemote = false;
	}

	public Connector(ServerSocket serverSocket) {
		isRemote = true;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		if(isRemote) {
			while(true) {
				try {
					Client newClient = new Client(serverSocket.accept());
					for(Client client : Host.instance.clients) {
						if(client.isConnected) {
							//TODO Team is already taken
						} else if(client.team == null) {
							//client has not yet connected
							
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
