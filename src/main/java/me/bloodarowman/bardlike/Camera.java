package me.bloodarowman.bardlike;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A class to control measurements and operations for the camera.
 * @author Alex
 */
public class Camera {
	private Player ply;
	private GameMap map;
	private int x = 0, y = 0;
    private float tX = 0f, tY = 0f;
    private int curTime = 0;

	public Camera(Player ply, GameMap map) {
		this.ply = ply;
		this.map = map;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void translate(GameContainer container, StateBasedGame s, Graphics g, float scale) {
		if (ply == null || map == null) { return; }

		tX = -(ply.getX() - (container.getWidth()*scale)/2) - ply.getImage().getWidth()/2; // Gets the player into the middle of the screen, it's negative as it translate opposite of the x and y.
		tY = -(ply.getY() - (container.getHeight()*scale)/2) - ply.getImage().getHeight()/2;

        x = (int) Misc.easeInOut(curTime, x, tX - x, curTime + 500);
        y = (int) Misc.easeInOut(curTime, y, tY - y, curTime + 500);
		g.translate(x, y); // This offsets drawing, essentially like moving a camera.
        curTime++;
	}

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setTime(int time) {
        curTime = time;
    }

	public int getTileToDir(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			return x/Misc.TARGET_SIZE;
		} else {
			return y/Misc.TARGET_SIZE;
		}
	}
}
