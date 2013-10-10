package com.sirolf2009.monopolie.communication.packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.sirolf2009.monopolie.Host;
import com.sirolf2009.monopolie.Monopoly;
import com.sirolf2009.monopolie.street.Street;
import com.sirolf2009.monopolie.team.Team;

public class PacketStreetVisit extends Packet {
	
	public Team team;
	public String streetName;

	public PacketStreetVisit() {}
	
	public PacketStreetVisit(Team team, Street street) {
		this.team = team;
		this.streetName = street.name;
	}
	
	protected void write(PrintWriter out) {
		PacketTeam packetTeam = new PacketTeam(team);
		packetTeam.write(out);
		out.println(streetName);
	}
	
	public void receive(BufferedReader in) throws IOException {
		System.out.println("receiving street visit");
		PacketTeam packetTeam = new PacketTeam();
		packetTeam.receive(in);
		team = packetTeam.getTeam();
		streetName = in.readLine();
	}
	
	public void receivedOnClient(Monopoly monopoly) {}
	
	public void receivedOnServer(Host host) {
		Street street2 = host.streets.get(streetName);
		if(street2.owningTeam != null)
			System.out.println(street2.owningTeam.isSameTeamAs(team));
		else 
			System.out.println(street2 + " " + team);
		if(street2.owningTeam != null && !street2.owningTeam.isSameTeamAs(team)) {
			team.money -= street2.calculateTaxes();
			PacketPayTaxes packet = new PacketPayTaxes(team, street2);
			host.getClientFromTeam(team).sender.send(packet);
		}
	}

}
