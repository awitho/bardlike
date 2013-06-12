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
	private MobDictionary mobDictionary;
	private int width, height;
	private Tile[][] tiles;

	public GameMap(int w, int h, TileDictionary tileDictionary, MobDictionary mobDictionary) {
		this.tileDictionary = tileDictionary;
		this.mobDictionary = mobDictionary;
		width = w;
		height = h;
	}

	public void draw(Graphics g, Player ply, Camera cam) {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				Tile tile = tiles[x][y];
				if (tile != null) {
					tile.draw(g, x * tile.getWidth(), y * tile.getHeight());
				}
			}
		}
	}

	@Deprecated
	public Tile moveEnt(Tile tile, Entity ent, Direction dir) {
		if (tile == null) { return null; }// System.out.println("GameMap.moveEnt: Given tile is null!"); return null; }
		//System.out.println("GameMap.moveEnt: " + ent + " moving in direction:  " + dir);
		Tile newTile = null;
		try {
			if (dir == Direction.LEFT) {
				newTile = tiles[tile.getX() - 1][tile.getY()];
			} else if (dir == Direction.RIGHT) {
				newTile = tiles[tile.getX() + 1][tile.getY()];
			} else if (dir == Direction.UP) {
				newTile = tiles[tile.getX()][tile.getY() - 1];
			} else if (dir == Direction.DOWN) {
				newTile = tiles[tile.getX()][tile.getY() + 1];
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			return null;
		}
		//if (newTile == null || tileDictionary.getTileIsWall(newTile.getName())) { return null; }
		ent.setTile(newTile);
		return newTile;
	}
		
	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= tiles.length || y >= tiles[0].length) { return null; }
		return tiles[x][y];
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public int getScaledWidth() {
		return width * 64;
	}

	public int getScaledHeight() {
		return height * 64;
	}
	
	public void regen() {
		tiles = DungeonGenerator.generateDungeon(24, 24, tileDictionary, mobDictionary).getTiles().clone();
	}

	public void update() {
	}
}
