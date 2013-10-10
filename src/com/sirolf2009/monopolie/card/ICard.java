package com.sirolf2009.monopolie.card;

import com.sirolf2009.monopolie.team.Team;

public interface ICard {
	
	public String description();
	
	public void draw(Team team);
}
