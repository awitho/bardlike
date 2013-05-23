import java.util.ArrayList;
import org.newdawn.slick.Color;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;


public class Tile {
	private boolean inLos;
	private boolean playerSaw;
	private boolean isWall;
	private ArrayList<Entity> containedEnts;
	private Image spr;
	private Image overlay;
	
    private static final Color overlayColor = new Color(0, 0, 0, 200);
	
	public Tile(TileDictionary tileDictionary, String tile) {
		if (tile == null) { return; }
		overlay = tileDictionary.getTileImageByName("Overlay");
		isWall = tileDictionary.getTileIsWall(tile); // Ditto. \/
		spr = tileDictionary.getTileImageByName(tile); // Grab a reference for the tile image from the tile dictionary, no need to make our own copy!
	}
     
	public int getWidth() {
		return spr.getWidth();
	}
	
	public int getHeight() {
		return spr.getHeight();
	}
        
	public Tile setSeen(boolean bool) {
		playerSaw = bool;
		return this;
	}
	
	public boolean hasSeen() {
		return playerSaw;
	}
	
	public Tile setInLos(boolean vis) {
		inLos = vis;
		return this;
	}
	
	public boolean getInLos() {
		return inLos;
	}
	
	public void draw(Graphics g, int x, int y) {
		g.drawImage(spr, x, y);
		if (containedEnts != null) {
			for (int i = 0; i < containedEnts.size(); i++) {
				containedEnts.get(i).draw(g);
			}
		}
		if (playerSaw && !inLos) {
			g.drawImage(overlay, x, y);
		}
	}

	public void update() {	
	}
}