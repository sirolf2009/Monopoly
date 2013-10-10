package com.sirolf2009.monopolie.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.sirolf2009.monopolie.communication.packet.Packet;

public class Sender implements Runnable {

	public PrintWriter out;
	private List<Packet> queue;

	public Sender(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream());
			queue = new ArrayList<Packet>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				sendToClient();
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void send(Packet packet) {
		queue.add(packet);
	}

	private synchronized void sendToClient() {
		for(Packet packet : queue) {
			packet.send(out);
			out.flush();
		}
		queue.clear();
	}

}
