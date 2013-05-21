import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;


public class GameMap {
	private ArrayList<ArrayList<Tile>> tiles; // 2d array list of tiles.
	private SpriteSheet sprites;
	
	public GameMap(int w, int h, TileDictionary tileDictionary) {
		tiles = new ArrayList<>();
		for(int x = 0; x < w; x++) {
			ArrayList<Tile> tileY = new ArrayList<>();
			for(int y = 0; y < h; y++) {
				tileY.add(new Tile(tileDictionary, "Stone").setInLos(true));
			}
			tiles.add(tileY);
		}
	}
	
	public void draw(Graphics g) { // Make it so it only renders near player 3-5 blocks!
		for (int x = 0; x < tiles.size(); x++) {
			ArrayList<Tile> tileX = tiles.get(x);
			if(tileX.isEmpty()) { continue; }
			for (int y = 0; y < tiles.get(x).size(); y++) {
				Tile tile = tileX.get(y);
				if (tile == null || (!tile.getInLos() && !tile.hasSeen()) ) { continue; }
				tile.draw(g, x * tile.getWidth(), y * tile.getHeight());
			}
		}
	}
	
	public void update() {
		
	}
}