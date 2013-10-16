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
		PacketTeam packetTeam = new PacketTeam();
		packetTeam.receive(in);
		team = packetTeam.getTeam();
		streetName = in.readLine();
	}
	
	public void receivedOnClient(Monopoly monopoly) {
	}
	
	public void receivedOnServer(Host host) {
		Street street = host.getStreet(streetName);
		if(street.owningTeam != null && !street.owningTeam.isSameTeamAs(team)) {
			host.getClientFromTeam(team).team.money -= street.calculateTaxes();
			host.getClientFromTeam(street.owningTeam).team.money += street.calculateTaxes();
			host.updateTeamInfoIfDisplayed(team);
			PacketMoney money = new PacketMoney(host.getClientFromTeam(street.owningTeam).team);
			PacketPayTaxes packet = new PacketPayTaxes(street.calculateTaxes(), street.owningTeam.teamColor.getRGB(), streetName);
			System.out.println("Owner "+host.getClientFromTeam(street.owningTeam).team+" Visitor "+host.getClientFromTeam(team).team);
			host.getClientFromTeam(team).sender.send(packet);
			host.getClientFromTeam(street.owningTeam).sender.send(money);
		}
	}

}
