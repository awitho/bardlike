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
	
	public void translate(Graphics g, GameContainer gc) {
		if (ply == null || map == null) { return; }
		x = -(ply.getX() - gc.getWidth()/2) - 32;
		y = -(ply.getY() - gc.getHeight()/2) - 32;
		g.translate(x, y);
	}
	
	public int getTileToDir(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			return x/Misc.TargetSize;
		} else {
			return y/Misc.TargetSize;
		}
	}
}
