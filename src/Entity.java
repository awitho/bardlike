import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Entity {
	public int id;
	private int x, y;
	private Image img;
	private GameMap map;
	private Tile curTile;
	
	public Entity(Image img, GameMap map) {
		this.img = img;
		this.map = map;
		x = 0;
		y = 0;
		curTile = map.getTile(x, y);
		curTile.addEnt(this);
	}
	
	public void draw(Graphics g) {
		g.drawImage(img, x, y);
	}
	
	public void update() {
		
	}
	
	public void setTile(Tile tile) {
		curTile = tile;
	}
	
	public Tile getTile() {
		return curTile;
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
}
