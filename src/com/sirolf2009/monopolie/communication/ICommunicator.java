package com.sirolf2009.monopolie.communication;

import java.net.Socket;

public interface ICommunicator {
	
	public Sender getSender();
	public Receiver getReceiver();
	public Socket getSocket();
	public boolean isRemote();
	public boolean isConnected();
	public void disconnect();

}
