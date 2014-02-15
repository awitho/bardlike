package me.bloodarowman.bardlike.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;


public interface Menu {
	public void setVisible(boolean b);
	public boolean isOpen();
	public void draw(GameContainer container, StateBasedGame s, Graphics g);
}
