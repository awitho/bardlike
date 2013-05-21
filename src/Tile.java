import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import org.newdawn.slick.Color;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;


public class Tile {
	private boolean inLos;
	private boolean playerSaw;
	private boolean isWall;
	private ArrayList<Entity> containedEnts;
	private Image spr;
        
        private static final Color overlayColor = new Color(0, 0, 0, 200);
	
	public Tile(TileDictionary tileDictionary, String tile) {
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
                            containedEnts.get(i).render(g);
                    }
                }
		if (playerSaw && !inLos) {
                    g.setColor(overlayColor);
                    g.fillRect(x, y, getWidth(), getHeight());
                }
	}

	public void update() {	
	}
}