package com.sirolf2009.monopolie.communication.packet;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.sirolf2009.monopolie.Host;
import com.sirolf2009.monopolie.Monopoly;
import com.sirolf2009.monopolie.team.Team;

public class PacketTeam extends Packet {

	public Color teamColor;
	public int money;
	public int draws;
	public boolean inJail;

	public PacketTeam() {}

	public PacketTeam(Team team) {
		this.teamColor = team.teamColor;
		this.money = team.money;
		this.draws = team.draws;
		this.inJail = team.inJail;
	}

	@Override
	protected void write(PrintWriter out) {
		out.println(teamColor.getRGB());
		out.println(money);
		out.println(draws);
		out.println(inJail);
	}

	@Override
	public void receive(BufferedReader in) throws IOException {
		teamColor = new Color(Integer.parseInt(in.readLine()));
		money = Integer.parseInt(in.readLine());
		draws = Integer.parseInt(in.readLine());
		inJail = Boolean.parseBoolean(in.readLine());
	}
	
	@Override
	public void receivedOnClient(Monopoly client) {
		if(teamColor.equals(Monopoly.localTeam.teamColor)) {
			if(Monopoly.localTeam.inJail != inJail) {
				Monopoly.localTeam = getTeam();
				client.updateStreetButtons();
			} else {
				Monopoly.localTeam = getTeam();
			}
			client.updateLocalTeamInfo();
		} else {
			System.err.println("Team was send to wrong Client! "+teamColor+" was send to "+Monopoly.localTeam.teamColor);
		}
	}
	
	public void receivedOnServer(Host host) {
		for(int i = 0; i < host.clients.length; i++) {
			if(host.clients[i].team == null) {
				host.clients[i].team = new Team(teamColor);
			}
			if(host.clients[i].team.teamColor.equals(teamColor)) {
				host.clients[i].team.money = money;
				host.clients[i].team.draws = draws;
				host.clients[i].team.inJail = inJail;
				host.updateTeamInfoIfDisplayed(host.clients[i].team);
				break;
			}
		}
	}
	
	public Team getTeam() {
		Team team = new Team(teamColor);
		team.money = money;
		team.draws = draws;
		team.inJail = inJail;
		return team;
	}

}
