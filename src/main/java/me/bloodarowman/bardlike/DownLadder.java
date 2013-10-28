package me.bloodarowman.bardlike;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Alex
 */
public class DownLadder extends Entity {
	public DownLadder() {
		super(Misc.miscImages.get("downladder"));
	}

    @Override
    public void update(GameContainer container, StateBasedGame s, int delta) {return;}
}