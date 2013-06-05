import java.util.ArrayList;

/**
 * A class for randomly generating coherent dungeons.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class DungeonGenerator {
	private static TileDictionary tileDictionary;
	
	public static void initTileDictionary(TileDictionary tileDictionary) {
		DungeonGenerator.tileDictionary = tileDictionary;
	}
	
	public static void generateRoom(ArrayList<ArrayList<Tile>> tiles, int x, int y, int width, int height) {
		//if (height > tiles.size()) { return; }
		for (int x1 = x; x1 < width; x1++) {
			for (int y1 = y; y1 < height; y1++) {
				if (x1 == 0 || y1 == 0 || x1 == width - 1 || y1 == height - 1) {
					tiles.get(x).add(new Tile(tileDictionary, tileDictionary.getRandomWall(), x1, y1));
				} else {
					tiles.get(x).add(new Tile(tileDictionary, tileDictionary.getRandomNonwall(), x1, y1));
				}
			}
		}
	}

	public static void generateHallway(ArrayList<ArrayList<Tile>> tiles, int x1, int y1, int x2, int y2) {
			ArrayList<PathfindingTile> openList = new ArrayList<>();
			ArrayList<PathfindingTile> closedList = new ArrayList<>();
			PathfindingTile start = new PathfindingTile(null, x1, y1, 0, 0, 0);
			openList.add(start);
			PathfindingTile curLookingTile = start;
			while (true) {
				for (Direction dir : Direction.values()) { // Search in all fours! awuuawduio
					Vector vec = Misc.getLocFromDir(curLookingTile.x, curLookingTile.y, dir);
					try {
						if (vec.getX() < 0 || vec.getX() > tiles.size() || vec.getY() < 0 || vec.getY() > tiles.get(0).size() || tiles.get(vec.getX()).get(vec.getY()).isWall()) { continue; }
					} catch (IndexOutOfBoundsException ex) { }
					//calculate f, g & h here.
					int g = 10;
					int h = 10 * (Math.abs(vec.getX() - x2) + Math.abs(vec.getY() - y2));
					int f = g + h;
					openList.add(new PathfindingTile(curLookingTile, vec.getX(), vec.getY(), f, g, h));
					System.out.println(dir);
					openList.remove(curLookingTile);
					closedList.add(curLookingTile);
				}
				System.out.println(openList);
            }
	}
	
	public static void placePlayerInFeasibleLocation(ArrayList<ArrayList<Tile>> tiles, Player ply) {
		for (int x = 0; x < tiles.size(); x++) {
			for (int y = 0; y < tiles.get(x).size(); y++) {
				if (tiles.get(x).get(y) == null || tiles.get(x).get(y).getName().equalsIgnoreCase("") || tiles.get(x).get(y).isWall() || (int) (Math.random() * 20) - 1 <= 10) { continue; }
				System.out.println("Placing player at: (" + x + ", " + y + ")");
				ply.setTile(tiles.get(x).get(y));
				return;
			}
		}
	}
	
	public static void placeTile(ArrayList<ArrayList<Tile>> tiles, Tile tile, int x, int y) {
		try {
			if (tiles.get(x) == null) { return; }
			tiles.get(x).set(y, tile);
		} catch (Exception ex) {
			Misc.showDialog(ex);
		}
	}

	public static GameMap generateDungeon(int w, int h, TileDictionary tileDictionary) {
		DungeonGenerator.initTileDictionary(tileDictionary);
		GameMap empty = new GameMap(w, h, tileDictionary);
		
		ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
		
		for (int x = 0; x < w; x++) { // Init the array with empty arrays.
			tiles.add(x, new ArrayList<Tile>());
		}
		tiles.trimToSize(); // Efficieny.
		
		generateRoom(tiles, 0, 0, 10, 10);
		System.out.println(tiles);
		generateHallway(tiles, 0, 11, 11, 11);
		/*String[] temp = new String[tileDictionary.size()];
		temp[0] = "Stone";
		temp[1] = "Wood";
		temp[2] = "Glass";
		temp[3] = "Obsidian";
		for(int x = 0; x < w; x++) {
			ArrayList<Tile> tileY = new ArrayList<>();
			for(int y = 0; y < h; y++) {
				//tileY.add(new Tile(tileDictionary, "Wood", x, y).setSeen(true));
				tileY.add(new Tile(tileDictionary, temp[(int) (Math.random() * temp.length - 1)], x, y).setSeen(true).setInLos(Misc.randomBool()));
			}
			tiles.add(tileY);
		}*/
		empty.setTiles(tiles);
		return empty;
	}
}
