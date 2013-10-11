package com.sirolf2009.monopolie;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import com.sirolf2009.monopolie.communication.packet.PacketMoney;
import com.sirolf2009.monopolie.communication.packet.PacketStreet;
import com.sirolf2009.monopolie.communication.packet.PacketStreetVisit;
import com.sirolf2009.monopolie.street.Street;
import com.sirolf2009.monopolie.team.Team;

public class JStreetButton extends JButton {

	private static final long serialVersionUID = 7474905125118555176L;
	ImageIcon dot;
	ImageIcon square;
	Street street;
	JPopupMenu popup;
	JLabel streetName;
	JLabel houses;
	JLabel owningTeam;
	JLabel visitingTeams;
	JButton moveTeam;
	JButton buyStreet;
	JButton buyHouse;
	boolean hasPopupInitialized = false;
	TeamColorFilter filter;

	public JStreetButton(Street street) {
		this.street = street;
		addActionListener(new ShowPopup(this));
		filter = new TeamColorFilter(this);
	}

	public void initPopup() {
		try {
			dot = new ImageIcon(ImageIO.read(new File(getClass().getClassLoader().getResource("images/dot.png").toURI())));
			square = new ImageIcon(ImageIO.read(new File(getClass().getClassLoader().getResource("images/square.png").toURI())));
			popup = new JPopupMenu();
			popup.setLayout(null);
			streetName = new JLabel("Straat: "+street.name);
			houses = new JLabel("Huizen: "+street.houses);
			owningTeam = new JLabel("Bezitter: " + (street.owningTeam != null ? street.owningTeam+"" : "niemand"));
			String visitors = "";
			for(Team team : street.visitingTeams) {
				visitors = visitors+team+"\n";
			}
			visitingTeams = new JLabel("Bezoekers: "+visitors);
			moveTeam = new JButton("Verplaats hierheen");
			moveTeam.addActionListener(new MoveTeam(this));
			buyStreet = new JButton("Koop de straat €"+street.buyingPrice());
			buyStreet.addActionListener(new BuyStreet(this));
			buyHouse = new JButton("Koop een huis €"+street.housePrice());
			buyHouse.addActionListener(new BuyHouse(this));
			popup.add(streetName);
			popup.add(houses);
			popup.add(owningTeam);
			if(Monopoly.localTeam.getVisitingStreet() != street && !Monopoly.localTeam.inJail)
				popup.add(moveTeam);
			if(street.owningTeam == null && street.visitingTeams.contains(Monopoly.localTeam) && !Monopoly.localTeam.inJail)
				popup.add(buyStreet);
			if(street.owningTeam == Monopoly.localTeam)
				popup.add(buyHouse);
			hasPopupInitialized = true;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public JStreetButton(Icon arg0) {
		super(arg0);
	}

	public JStreetButton(String arg0) {
		super(arg0);
	}

	public JStreetButton(Action arg0) {
		super(arg0);
	}

	public JStreetButton(String arg0, Icon arg1) {
		super(arg0, arg1);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(!hasPopupInitialized) {
			initPopup();
		}
		//TODO Wonderklauwpad en peur
		FilteredImageSource producer = new FilteredImageSource(dot.getImage().getSource(), filter);
		for(Team team : street.visitingTeams) {
			if(team.teamColor.equals(Color.red)) {
				System.out.println(team + " " + Monopoly.localTeam);
			}
			if(team.isSameTeamAs(Monopoly.localTeam)) {
				producer = new FilteredImageSource(square.getImage().getSource(), filter);
				Image img = createImage(producer);
				g.drawImage(img, 0, 0, null);
				return;
			}
		}
		Image img = createImage(producer);
		g.drawImage(img, 0, 0, null);
	}
}
class ShowPopup implements ActionListener {
	JStreetButton button;
	public ShowPopup(JStreetButton button) {
		this.button = button;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getID() == 1001) {
			button.initPopup();
			button.popup.show((Component) e.getSource(), 0, 0);
		}
	}
}
class BuyStreet implements ActionListener {
	JStreetButton button;
	public BuyStreet(JStreetButton button) {
		this.button = button;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getID() == 1001) {
			if(Monopoly.localTeam.inJail)
				return;
			button.initPopup();
			if(Monopoly.localTeam.money >= button.street.buyingPrice() && button.street.owningTeam == null && button.street.visitingTeams.contains(Monopoly.localTeam)) {
				button.street.owningTeam = Monopoly.localTeam;
				Monopoly.localTeam.money -= button.street.buyingPrice();
				Monopoly.instance.updateLocalTeamInfo();
				Monopoly.instance.getSender().send(new PacketStreet(button.street));
				Monopoly.instance.getSender().send(new PacketMoney(Monopoly.localTeam));
				button.popup.setVisible(false);
			}
		}
	}
}
class MoveTeam implements ActionListener {
	JStreetButton button;
	public MoveTeam(JStreetButton button) {
		this.button = button;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getID() == 1001) {
			if(Monopoly.localTeam.inJail)
				return;
			button.initPopup();
			JStreetButton buttonOld = null;
			if(Monopoly.localTeam.getVisitingStreet() != null) {
				buttonOld = Monopoly.instance.streetButtons.get(Monopoly.localTeam.getVisitingStreet().name);
				Monopoly.localTeam.getVisitingStreet().visitingTeams.remove(Monopoly.localTeam);
			}
			button.street.visitingTeams.add(Monopoly.localTeam);
			Monopoly.localTeam.setVisitingStreet(button.street);
			System.out.println("local team is now visiting " + Monopoly.localTeam.getVisitingStreet());
			button.street.visitingTeams.add(Monopoly.localTeam);
			Monopoly.instance.getSender().send(new PacketStreetVisit(Monopoly.localTeam, button.street));
			button.popup.setVisible(false);
			if(buttonOld != null) {
				buttonOld.repaint();
			}
			Monopoly.instance.updateLocalTeamInfo();
			Monopoly.instance.updateStreetButtons();
		}
	}
}
class BuyHouse implements ActionListener {
	JStreetButton button;
	public BuyHouse(JStreetButton button) {
		this.button = button;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getID() == 1001) {
			button.initPopup();
			if(Monopoly.localTeam.money >= button.street.housePrice()) {
				button.street.houses++;
				Monopoly.localTeam.money -= button.street.housePrice();
				Monopoly.instance.updateLocalTeamInfo();
				Monopoly.instance.getSender().send(new PacketStreet(button.street));
				Monopoly.instance.getSender().send(new PacketMoney(Monopoly.localTeam));
				button.popup.setVisible(false);
			}
		}
	}
}