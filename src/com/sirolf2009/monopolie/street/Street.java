package com.sirolf2009.monopolie.street;

import java.util.ArrayList;
import java.util.List;

import com.sirolf2009.monopolie.team.Team;

public class Street {
	
	public int x;
	public int y;
	public int houses;
	public int size;
	public String name;
	public Team owningTeam;
	public List<Team> visitingTeams = new ArrayList<Team>();
	
	public int calculateTaxes() {
		return (10+10*size)+10*houses;
	}
	
	public int buyingPrice() {
		return 50+50*size;
	}
	
	public int housePrice() {
		return buyingPrice();
	}
	
	@Override
	public String toString() {
		return "Straat "+name;
	}
}
