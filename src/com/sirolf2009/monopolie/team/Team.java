package com.sirolf2009.monopolie.team;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sirolf2009.monopolie.street.Street;

public class Team {
	
	public Color teamColor;
	public int money;
	public int draws;
	public boolean inJail;
	public Street visitingStreet;
	
	public Team(Color color) {
		teamColor = color;
		money = 500;
		draws = 5;
		inJail = false;
	}
	
	@Override
	public String toString() {
		return "Team "+getColorName(teamColor);
	}
	
	public boolean isSameTeamAs(Team team) {
		return team.teamColor.equals(teamColor);
	}

	private String getColorName(Color color) {
		if(color.equals(Color.red)) {
			return "Rood";
		} else if (color.equals(Color.blue)) {
			return "Blauw";
		} else if (color.equals(Color.cyan)) {
			return "Cyaan";
		} else if (color.equals(Color.green)) {
			return "Groen";
		} else if (color.equals(Color.magenta)) {
			return "Magenta";
		} else if (color.equals(Color.orange)) {
			return "Oranje";
		} else if (color.equals(Color.pink)) {
			return "Roze";
		} else if (color.equals(Color.yellow)) {
			return "Geel";
		}
		return color.toString();
	}
	
	public List<Street> getOwnedStreets(Collection<Street> streets) {
		List<Street> ownedStreets = new ArrayList<Street>();
		for(Street street : streets) {
			if(street.owningTeam != null && street.owningTeam.isSameTeamAs(this)) {
				ownedStreets.add(street);
			}
		}
		return ownedStreets;
	}
	
}
