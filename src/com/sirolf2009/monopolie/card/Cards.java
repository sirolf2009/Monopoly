package com.sirolf2009.monopolie.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sirolf2009.monopolie.Monopoly;
import com.sirolf2009.monopolie.team.Team;

public class Cards {
	
	public List<ICard> cards = new ArrayList<>();
	private Random rand;

	public Cards() {
		rand = new Random();
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.money += 100;
			}
			@Override
			public String description() {
				return "Je team erft €100";
			}
		});
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.money += 200;
			}
			@Override
			public String description() {
				return "Een vergissing van de bank in je teams voordeel, je team ontvangt €200";
			}
		});
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.inJail = true;
				team.setVisitingStreet(null);
				Monopoly.instance.updateStreetButtons();
			}
			@Override
			public String description() {
				return "Ga direct naar de gevangenis";
			}
		});
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.money += 10;
			}
			@Override
			public String description() {
				return "Je team heeft de tweede prijs in een schoonheidswedstrijd gewonnen en ontvangt €10";
			}
		});
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.money -= 50;
			}
			@Override
			public String description() {
				return "Betaal de doktersrekening €50";
			}
		});
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.money -= 50;
			}
			@Override
			public String description() {
				return "Betaal de verzekeringspremie €50";
			}
		});
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.money += 50;
			}
			@Override
			public String description() {
				return "Door verkoop van effecten krijgt je team €50";
			}
		});
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.money += 20;
			}
			@Override
			public String description() {
				return "Restitutie inkomstenbelasting, je team ontvangt €20";
			}
		});
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.money += 100;
			}
			@Override
			public String description() {
				return "Lijfrente vervalt, je team ontvangt €100";
			}
		});
		cards.add(new ICard() {
			@Override
			public void draw(Team team) {
				team.money -= 100;
			}
			@Override
			public String description() {
				return "Betaal het hospitaal €100";
			}
		});
	}
	
	public ICard getRandomCard() {
		return cards.get(rand.nextInt(cards.size()));
	}

}
