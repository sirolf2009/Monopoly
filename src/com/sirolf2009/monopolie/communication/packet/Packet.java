package com.sirolf2009.monopolie.communication.packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.sirolf2009.monopolie.Host;
import com.sirolf2009.monopolie.Monopoly;

public class Packet {

	public static Map<Integer, Class<?>> packetsIDtoPacket = new HashMap<Integer, Class<?>>();
	public static Map<Class<?>, Integer> packetsPackettoID = new HashMap<Class<?>, Integer>();

	public Packet() {}
	
	public void send(PrintWriter out) {
		out.println(packetsPackettoID.get(this.getClass()));
		write(out);
		out.flush();
	}
	
	protected void write(PrintWriter out) {}
	public void receive(BufferedReader in) throws IOException {}
	
	public void receivedOnClient(Monopoly monopoly) {}
	public void receivedOnServer(Host host) {}
	
	public static void registerPacket(int ID, Class<?> packet) {
		packetsIDtoPacket.put(ID, packet);
		packetsPackettoID.put(packet, ID);
	}
	
	static {
		registerPacket(0, PacketTeam.class);
		registerPacket(1, PacketStreet.class);
		registerPacket(2, PacketMoney.class);
		registerPacket(3, PacketStreetVisit.class);
		registerPacket(4, PacketStreetBuy.class);
		registerPacket(5, PacketPayTaxes.class);
	}

}
