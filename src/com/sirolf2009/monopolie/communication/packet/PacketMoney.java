package com.sirolf2009.monopolie.communication.packet;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.sirolf2009.monopolie.Host;
import com.sirolf2009.monopolie.Monopoly;
import com.sirolf2009.monopolie.communication.Client;
import com.sirolf2009.monopolie.team.Team;

public class PacketMoney extends Packet {
	
	public Color teamColor;
	public int money;

	public PacketMoney() {}
	
	public PacketMoney(Team team) {
		teamColor = team.teamColor;
		money = team.money;
	}
	
	@Override
	protected void write(PrintWriter out) {
		out.println(teamColor.getRGB());
		out.println(money);
	}
	
	@Override
	public void receive(BufferedReader in) throws IOException {
		teamColor = new Color(Integer.parseInt(in.readLine()));
		money = Integer.parseInt(in.readLine());
	}
	
	@Override
	public void receivedOnClient(Monopoly monopoly) {
		if(Monopoly.localTeam.teamColor == teamColor) {
			Monopoly.localTeam.money = money;
		}
	}
	
	@Override
	public void receivedOnServer(Host host) {
		for(Client client : host.clients) {
			if(client != null && client.team.teamColor == teamColor) {
				client.team.money = money;
				host.updateTeamInfoIfDisplayed(client.team);
				break;
			}
		}
	}

}
