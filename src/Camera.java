import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * A class to control measurements and operations for the camera.
 * @author Alex
 */
public class Camera {
	private Player ply;
	private GameMap map;
	private int x, y = 0;
	
	public Camera(Player ply, GameMap map) {
		this.ply = ply;
		this.map = map;
		System.out.println(ply + " " + map);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void translate(Graphics g, GameContainer gc) {
		if (ply == null || map == null) { return; }
		x = -(ply.getX() - gc.getWidth()/2) - 32;
		y = -(ply.getY() - gc.getHeight()/2) - 32;
		g.translate(x, y);
		
		/*
		//If statements here for testing purposes, make a method for it later.
		//Makes it so the map doesn't go out of screen bounds (A bit buggy atm).
		if(transY > 0 && transX + map.getScaledWidth() <= container.getWidth()) {
			transX = -container.getWidth();
			transY = 0;
		} else if (transX + map.getScaledWidth() <= container.getWidth() && transY + map.getScaledHeight() <= container.getHeight()) {
			transX = -container.getWidth();
			transY = -container.getHeight();
		} else if (transX > 0 && transY + map.getScaledWidth() <= container.getHeight()) {
			transX = 0;
			transY = -container.getHeight();
		} else if (transY + map.getScaledHeight() <= container.getHeight()) {
			transY = -container.getHeight();
		} else if (transX + map.getScaledWidth() <= container.getWidth()) {
			transX = -container.getWidth();
		} else if(transX > 0) {
			transX = 0;
		} else if (transY > 0) {
			transY = 0;
		} else if (transX > 0 && transY > 0) {
			transX = 0;
			transY = 0;
		}
		*/
	}
	
	public int getTileToDir(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			return x/Misc.TargetSize;
		} else {
			return y/Misc.TargetSize;
		}
	}
}
