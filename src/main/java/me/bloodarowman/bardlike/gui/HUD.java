package me.bloodarowman.bardlike.gui;

import me.bloodarowman.bardlike.Player;
import me.bloodarowman.bardlike.TileDictionary;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alex
 */
public class HUD implements Menu {
	private boolean visible = false;
	private Player player;
	
	public HUD(Player ply) {
		this.player = ply;
	}

	@Override
	public void setVisible(boolean b) {
		visible = b;
	}

	@Override
	public boolean isOpen() {
		return visible;
	}

	public void draw(GameContainer container, StateBasedGame s, Graphics g) {
		g.setFont(Log.LOG_FONT);
		String stuff = "HP: " + player.getHP() + " | LVL: " + player.getLevel() + " | XP: " + player.getXP() + " | DLVL: " + TileDictionary.getDungeonLevel();
		int width = g.getFont().getWidth(stuff);
		int height = g.getFont().getHeight(stuff);

		g.pushTransform();
		g.translate(container.getWidth() - g.getFont().getWidth(stuff) - 5, container.getHeight() - height - 5);
		g.setColor(Color.black);
		g.drawString(stuff, -2, 0);
		g.drawString(stuff, 2, 0);
		g.drawString(stuff, 0, -2);
		g.drawString(stuff, 0, 2);

		g.setColor(Color.white);
		g.drawString(stuff, 0, 0);
		g.popTransform();
	}
}
