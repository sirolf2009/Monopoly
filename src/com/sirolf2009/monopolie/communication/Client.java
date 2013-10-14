package com.sirolf2009.monopolie.communication;

import java.net.Socket;

import com.sirolf2009.monopolie.team.Team;

public class Client implements ICommunicator {
	
	public Socket socket;
	public Sender sender;
	public Receiver receiver;
	public Team team;
	public boolean isConnected;

	public Client(Socket socket) {
		this.socket = socket;
		sender = new Sender(socket);
		receiver = new Receiver(this);
		new Thread(sender, socket.toString() + " sender").start();
		new Thread(receiver, socket.toString() + " receiver").start();
	}

	@Override
	public Sender getSender() {
		return sender;
	}

	@Override
	public Receiver getReceiver() {
		return receiver;
	}

	@Override
	public Socket getSocket() {
		return socket;
	}

	@Override
	public boolean isRemote() {
		return true;
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}

	@Override
	public void disconnect() {
		isConnected = false;
	}

}
