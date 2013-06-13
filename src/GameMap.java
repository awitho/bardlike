import org.newdawn.slick.Graphics;

/**
 * A class representing an entire dungeon level of the game.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class GameMap {
	private MobDictionary mobDictionary;
	private int width, height;
	private Tile[][] tiles;

	public GameMap(int w, int h, MobDictionary mobDictionary) {
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
	
	public void update(MainGameState mgs) {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				Tile tile = tiles[x][y];
				if (tile != null) {
					tile.update(mgs);
				}
			}
		}
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
		tiles = DungeonGenerator.generateDungeon(Misc.DUNGEON_SIZE, Misc.DUNGEON_SIZE, mobDictionary).getTiles().clone();
	}
}
