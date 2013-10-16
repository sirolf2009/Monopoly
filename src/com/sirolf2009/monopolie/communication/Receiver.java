package com.sirolf2009.monopolie.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

import com.sirolf2009.monopolie.Host;
import com.sirolf2009.monopolie.Monopoly;
import com.sirolf2009.monopolie.communication.packet.Packet;

public class Receiver implements Runnable {

	BufferedReader in;
	ICommunicator communicator;

	public Receiver(ICommunicator communicator) {
		try {
			this.communicator = communicator;
			in = new BufferedReader(new InputStreamReader(communicator.getSocket().getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				int packetID = Integer.parseInt(in.readLine());
				if(Packet.packetsIDtoPacket.get(packetID) == null) {
					System.err.println("Unknown packet ID: "+packetID);
				} else {
					Packet packet = (Packet) Packet.packetsIDtoPacket.get(packetID).newInstance();
					packet.receive(in);
					if(!communicator.isRemote()) {
						packet.receivedOnClient(Monopoly.instance);
					} else {
						packet.receivedOnServer(Host.instance);
					}
				}
				Thread.sleep(20);
			} catch (SocketException e) {
				System.err.println("Connection has been lost.");
				e.printStackTrace();
				if(!communicator.isRemote()) {
					System.exit(0);
				} else {
					System.err.println(((Client)communicator).team);
					communicator.disconnect();
					Host.instance.lstClients.repaint();
				}
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
