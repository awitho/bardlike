package me.bloodarowman.bardlike.gui;

import org.newdawn.slick.Graphics;


public interface Menu {
	public void setVisible(boolean b);
	public boolean isOpen();
	public void draw(Graphics g);
}
