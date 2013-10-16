package com.sirolf2009.monopolie.communication.packet;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.jar.Attributes.Name;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.sirolf2009.monopolie.Host;
import com.sirolf2009.monopolie.Monopoly;
import com.sirolf2009.monopolie.team.Team;

public class PacketPayTaxes extends Packet {

	/*public Team team;
	public Street street;*/

	public int taxes;
	public int teamColor;

	public PacketTeam packetTeam;
	public PacketStreet packetStreet;
	public String street;

	public PacketPayTaxes() {}

	public PacketPayTaxes(int taxes, int teamColor, String street) {
		this.taxes = taxes;
		this.teamColor = teamColor;
		this.street = street;
	}

	protected void write(PrintWriter out) {
		out.println(taxes);
		out.println(teamColor);
		out.println(street);
	}

	public void receive(BufferedReader in) throws IOException {
		taxes = Integer.valueOf(in.readLine());
		teamColor = Integer.valueOf(in.readLine());
		street = in.readLine();
	}

	public void receivedOnClient(final Monopoly monopoly) {
		System.out.println("paying taxes");
		//packetTeam.receivedOnClient(monopoly);
		Monopoly.localTeam.money -= taxes;
		final JPopupMenu popup = new JPopupMenu();
		popup.add(new JLabel("Deze straat is gekocht door "+Team.getColorName(new Color(teamColor))+"."));
		popup.add(new JLabel("Je team verliest "+taxes+" euro."));
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
		monopoly.streets.get(street).owningTeam=monopoly.getTeamFromColor(new Color(teamColor));
		monopoly.updateLocalTeamInfo();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				monopoly.updateStreetButtons();
			}
		});
	}

	public void receivedOnServer(Host host) {}

}
