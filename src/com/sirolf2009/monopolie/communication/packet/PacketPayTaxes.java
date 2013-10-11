package com.sirolf2009.monopolie.communication.packet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import com.sirolf2009.monopolie.Host;
import com.sirolf2009.monopolie.Monopoly;
import com.sirolf2009.monopolie.street.Street;
import com.sirolf2009.monopolie.team.Team;

public class PacketPayTaxes extends Packet {
	
	public Team team;
	public Street street;

	public PacketTeam packetTeam;
	public PacketStreet packetStreet;

	public PacketPayTaxes() {}
	
	public PacketPayTaxes(Team team, Street street) {
		this.team = team;
		this.street = street;
	}
	
	protected void write(PrintWriter out) {
		packetTeam = new PacketTeam(team);
		packetTeam.write(out);
		packetStreet = new PacketStreet(street);
		packetStreet.write(out);
	}
	
	public void receive(BufferedReader in) throws IOException {
		packetTeam = new PacketTeam();
		packetTeam.receive(in);
		packetStreet = new PacketStreet();
		packetStreet.receive(in);
	}
	
	public void receivedOnClient(Monopoly monopoly) {
		System.out.println("paying taxes");
		packetTeam.receivedOnClient(monopoly);
		packetStreet.receivedOnClient(monopoly);
		final JPopupMenu popup = new JPopupMenu();
		popup.add(new JLabel("Deze straat is gekocht door "+packetStreet.owningTeam+"."));
		popup.add(new JLabel("Je team verliest "+packetStreet.getStreet().calculateTaxes()+" euro."));
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.setVisible(false);
			}
		});
		popup.setLocation(400, 300);
		popup.add(ok);
		popup.setVisible(true);
		monopoly.localTeam.setVisitingStreet(monopoly.getStreet(packetStreet.name));
		//monopoly.getStreet(packetStreet.name).visitingTeams.add(Monopoly.localTeam);
		//monopoly.updateStreetButtons();
	}
	
	public void receivedOnServer(Host host) {}

}
