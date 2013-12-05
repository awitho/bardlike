package me.bloodarowman.bardlike;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Alex
 */
public class GameOverState extends BasicGameState {
	public static final int STATE_ID = 5;
	private Image backImg;

	@Override
	public int getID() {
		return STATE_ID;
	}

	@Override
 public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.drawImage(backImg, 0, 0);
		g.drawString("Game over: Press R to restart.", gc.getWidth() / 2 - 
					(g.getFont().getWidth("Game over: Press R to restart.") / 2)
					,gc.getHeight() / 2 - 
				(g.getFont().getHeight("Game over: Press R to restart.") / 2));
	}
	
	public void setBackImg(Image img) {
		this.backImg = img;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int i) 
				throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_R)) {
			MainGameState s = (MainGameState) sbg.getState(4);
			s.restart();
			s.getPlayer().revive();
			sbg.enterState(4);
		}
	}
}
