import java.util.ArrayList;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

/**
 * A class representing an entire dungeon level of the game.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class GameMap {
	private TileDictionary tileDictionary;
	private int width, height;
	private ArrayList<ArrayList<Tile>> tiles; // 2d array list of tiles.
	private SpriteSheet sprites;

	public GameMap(int w, int h, TileDictionary tileDictionary) {
		this.tileDictionary = tileDictionary;
		width = w;
		height = h;
		tiles = DungeonGenerator.generateDungeon(w, h, tileDictionary);
	}

	public void draw(Graphics g) { // Make it so it only renders near player 3-5 blocks!
		for (int x = 0; x < tiles.size(); x++) {
			ArrayList<Tile> tileX = tiles.get(x);
			if(tileX.isEmpty()) { continue; }
			for (int y = 0; y < tiles.get(x).size(); y++) {
				Tile tile = tileX.get(y);
				if (tile == null) { continue; }
			//	if (tile == null || (!tile.getInLos() && !tile.hasSeen()) ) { continue; }
				tile.draw(g, x * tile.getWidth(), y * tile.getHeight());
			}
		}
	}

	public void moveEnt(Tile tile, Entity ent, Direction dir) {
		System.out.println("(" + ent + ") Moving from: " + tile + " to " + dir);
		Tile newTile = null;
		try {
			if (dir == Direction.LEFT) {
				ArrayList<Tile> tileX = tiles.get(tile.getX() - 1);
				if (tileX == null) { return; }
				newTile = tileX.get(tile.getY());
			} else if (dir == Direction.RIGHT) {
				ArrayList<Tile> tileX = tiles.get(tile.getX() + 1);
				if (tileX == null) { return; }
				newTile = tileX.get(tile.getY());
			} else if (dir == Direction.UP) {
				newTile = tiles.get(tile.getX()).get(tile.getY() - 1);
			} else if (dir == Direction.DOWN) {
				newTile = tiles.get(tile.getX()).get(tile.getY() + 1);
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			return;
		}
		if (newTile == null || tileDictionary.getTileIsWall(newTile.getName())) { return; }
		tile.removeEnt(ent);
		newTile.addEnt(ent);
		ent.setTile(newTile);
	}

	public Tile getTile(int x, int y) {
		return tiles.get(x).get(y);
	}

	public int getScaledWidth() {
		return width * 64;
	}

	public int getScaledHeight() {
		return height * 64;
	}

	public void update() {
		
	}
}
