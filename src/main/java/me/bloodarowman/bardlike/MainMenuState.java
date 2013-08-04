package me.bloodarowman.bardlike;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The Main Menu for the game.
 * @author Bobby Henley
 * @since 5/26/13
 * @version 1
 */
public class MainMenuState extends BasicGameState {
	private String menu;
	private Image titleImg;
	private Image backImg;
	public static TrueTypeFont font = 
			new TrueTypeFont(new java.awt.Font("Arial", 1, 20), true);

	@Override
	public void init(GameContainer container, StateBasedGame s) {
		container.setShowFPS(false);
		this.menu = "What Do? [ (P)lay | (H)elp | (Esc)ape ]";
		try {
			this.titleImg = new Image("./gfx/title.png");
			this.backImg = new Image("./gfx/mainback.png")
					.getScaledCopy(container.getWidth(), container.getHeight());
		} catch (Exception e) {
			Misc.showDialog(e);
			return;
		}
		
		GameOverState g = (GameOverState) s.getState(5);
		g.setBackImg(backImg);
		HelpMenuState h = (HelpMenuState) s.getState(3);
		h.setBackImg(backImg);
	}

	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g)
			throws SlickException {
		// Background
		g.drawImage(backImg, 0, 0);

		// Icon
		titleImg.setRotation(Misc.lerp(titleImg.getRotation(), 
				(float) Math.sin(System.currentTimeMillis()/750)*20, 0.01f));
		g.drawImage(titleImg, container.getWidth()/2 - 
				titleImg.getWidth()/2, container.getHeight()/2 - 
				titleImg.getHeight()/2);

		// Main menu text
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(menu, container.getWidth()/2 - (font.getWidth(menu))/2, 
				titleImg.getHeight() + (container.getHeight()/2 - 
				titleImg.getHeight()/2));
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta) 
			throws SlickException {
		if(s.getCurrentState() == s.getState(1)) {
			if(container.getInput().isKeyPressed(Input.KEY_P)) {
				s.enterState(2); // Jump to main game state.
			} else if (container.getInput().isKeyPressed(Input.KEY_H)) {
				s.enterState(3);
			} else if (container.getInput().isKeyPressed(Input.KEY_Q) ||
					container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
				container.exit();
			}
		}
		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public int getID() {
		return 1;
	}
}
