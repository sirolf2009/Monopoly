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
	public Color team;

	public PacketStreetBuy() {}

	public PacketStreetBuy(Street street, Team buyingTeam) {
		this.name = street.name;
		this.team = buyingTeam.teamColor;
	}

	@Override
	protected void write(PrintWriter out) {
		out.println(name);
		out.println(team.getRGB());
	}

	@Override
	public void receive(BufferedReader in) throws IOException {
		name = in.readLine();
		team = new Color(Integer.parseInt(in.readLine()));
	}

	@Override
	public void receivedOnClient(Monopoly monopoly) {
		monopoly.getStreet(name).owningTeam = Monopoly.localTeam;
		monopoly.updateLocalTeamInfo();
		monopoly.updateStreetButtons();
	}
	
	@Override
	public void receivedOnServer(Host host) {
		if(host.getStreet(name).owningTeam == null && host.getStreet(name).buyingPrice() < host.getClientFromTeamColor(team).team.money) {
			host.getClientFromTeamColor(team).team.money -= host.getStreet(name).buyingPrice();
			host.getClientFromTeamColor(team).sender.send(new PacketMoney(host.getClientFromTeamColor(team).team));
			team = host.getStreet(name).owningTeam.teamColor;
			host.getClientFromTeamColor(team).sender.send(this);
		}
	}
}
