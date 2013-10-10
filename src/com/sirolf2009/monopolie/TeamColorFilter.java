package com.sirolf2009.monopolie;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

public class TeamColorFilter extends RGBImageFilter {

	JStreetButton button;
	
	public TeamColorFilter(JStreetButton button) {
		this.button = button;
	}

	@Override
	public int filterRGB(int x, int y, int rgb) {
		if(rgb == Color.white.getRGB() && Monopoly.localTeam.inJail)
			return new Color(150, 150, 150).getRGB();
		if(rgb == Color.white.getRGB() && button.street.owningTeam != null)
			return button.street.owningTeam.teamColor.getRGB();
		return rgb;
	}

}
