package com.sirolf2009.monopolie.communication.packet;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.sirolf2009.monopolie.Host;
import com.sirolf2009.monopolie.Monopoly;
import com.sirolf2009.monopolie.team.Team;

public class PacketConnect extends Packet {
	
	public Color teamColor;
	
	public PacketConnect() {}
	
	public PacketConnect(Team team) {
		this.teamColor = team.teamColor;
	}
	
	@Override
	protected void write(PrintWriter out) {
		out.println(teamColor.getRGB());
	}
	
	@Override
	public void receive(BufferedReader in) throws IOException {
		teamColor = new Color(Integer.parseInt(in.readLine()));
	}
	
	@Override
	public void receivedOnClient(Monopoly monopoly) {
	}
	
	@Override
	public void receivedOnServer(Host host) {
	}

}
