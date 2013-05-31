import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * A generic entity class, represents everything that's not a tile.
 * @sicne 5/26/13
 * @version 1
 * @author Alex
 */
public class Entity {
	private static int iid = 1;
	private int id;
	private int x, y;
	private boolean visible = false;
	private Image img;
	private GameMap map;
	private Tile curTile;

	public Entity(Image img, GameMap map) {
		id = iid;
		iid++;
		this.img = img;
		this.map = map;
	}

	public void draw(Graphics g, int x, int y) {
		if(!visible) { return; }
			g.drawImage(img, x, y);
	}

	public void update() {
		
	}

	public void setTile(Tile tile) {
		visible = true;
		curTile = tile;
		if(curTile == null) { visible = false; return; }
		x = tile.getX() * Misc.TargetSize;
		y = tile.getY() * Misc.TargetSize;
	}

	public Tile getTile() {
		return curTile;
	}
	
	public Image getImage() {
		return img;
	}

	public GameMap getMap() {
		return map;
	}

	public void setImage(Image img) {
		this.img = img;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Entity: " + id + " (" + x + ", " + y + ")";
	}
}
