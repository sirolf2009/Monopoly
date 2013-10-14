package com.sirolf2009.monopolie.communication.packet;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.sirolf2009.monopolie.Host;
import com.sirolf2009.monopolie.Monopoly;
import com.sirolf2009.monopolie.street.Street;
import com.sirolf2009.monopolie.team.Team;

public class PacketStreetBuy extends Packet {

	public String name;
	public Color buyingTeam;

	public PacketStreetBuy() {}

	public PacketStreetBuy(Street street, Team buyingTeam) {
		this.name = street.name;
		this.buyingTeam = buyingTeam.teamColor;
	}

	@Override
	protected void write(PrintWriter out) {
		out.println(name);
		out.println(buyingTeam.getRGB());
	}

	@Override
	public void receive(BufferedReader in) throws IOException {
		name = in.readLine();
		buyingTeam = new Color(Integer.parseInt(in.readLine()));
	}

	@Override
	public void receivedOnClient(Monopoly monopoly) {
		monopoly.getStreet(name).owningTeam = Monopoly.localTeam;
		monopoly.updateLocalTeamInfo();
		monopoly.updateStreetButtons();
	}
	
	@Override
	public void receivedOnServer(Host host) {
		if(host.getStreet(name).owningTeam == null) {
			host.getClientFromTeamColor(buyingTeam).sender.send(this);
		}
	}
}
