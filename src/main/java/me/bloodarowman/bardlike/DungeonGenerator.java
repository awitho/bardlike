package me.bloodarowman.bardlike;

import java.util.ArrayList;

/**
 * A class for randomly generating coherent dungeons.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class DungeonGenerator {
	private static MobDictionary mobDictionary;
	private static ArrayList<Tile> wallsToBe;

	public static void initMobDictionary(MobDictionary mobDictionary) {
		DungeonGenerator.mobDictionary = mobDictionary;
	}
	
	public static Room generateRoom(Tile[][] tiles, int x, int y, int width, int height) {
		if (x + width > tiles.length || y + height > tiles[0].length) { return null; }
		for (int x1 = x; x1 < (x + width); x1++) {
			for (int y1 = y; y1 < (y + height); y1++) {
				if (tiles[x1][y1] == null || tiles[x1][y1].isReal()) { return null; }
			}
		}
		for (int x1 = x; x1 < (x + width); x1++) {
			for (int y1 = y; y1 < (y + height); y1++) {
				Tile tile;
				if (x1 == x || y1 == y || x1 == (x + width) - 1 || y1 == (y + height) - 1) {
					tile = new Tile(TileDictionary.getWallForTheme(), x1, y1);
				} else {
					tile = new Tile(TileDictionary.getFloorForTheme(), x1, y1);
				}
				tiles[x1][y1] = tile; 
			}
		}
		return new Room(tiles, x, y, width, height);
	}

	public static boolean generateHallway(Tile[][] tiles, int x1, int y1, int x2, int y2) {
			ArrayList<PathfindingTile> openList = new ArrayList<PathfindingTile>();
			ArrayList<PathfindingTile> closedList = new ArrayList<PathfindingTile>();
			
			PathfindingTile curLookingTile = new PathfindingTile(null, x1, y1, 0, 0, 0);
			openList.add(curLookingTile);
			
			int added = 0; // May not be needed?
			
			outerloop: // Loop label.
			while (true) {
				innerloop: // Loop label.
				for (Direction dir : Direction.values()) { // Find adj tiles to starting point.
					Vector vec = Misc.getLocFromDir(curLookingTile.getX(), curLookingTile.getY(), dir);
					
					if (vec.getX() == x2 && vec.getY() == y2) { closedList.add(new PathfindingTile(curLookingTile, x2, y2, 0, 0, 0)); break outerloop; }
					
					for (int i = 0; i < closedList.size(); i++) {
						if (vec.getX() == closedList.get(i).getX() && vec.getY() == closedList.get(i).getY()) {
							continue innerloop;
						}
					}
					
					for (int i = 0; i < openList.size(); i++) {
						if (vec.getX() == openList.get(i).getX() && vec.getY() == openList.get(i).getY()) {
							continue innerloop; // Possibly, might want to remove?
						}
					}
					
					try {
						if (vec.getX() < 0 || vec.getX() > tiles.length || vec.getY() < 0 || vec.getY() > tiles[0].length || (tiles[vec.getX()][vec.getY()].isWall() && tiles[vec.getX()][vec.getY()].isReal())) { continue; }
					} catch (IndexOutOfBoundsException ex) { }

					//calculate f, g & h here.
					double g = 10.0;
					double h = Double.MAX_VALUE;

					double xDistance = Math.abs(vec.getX() - x2); // Diagonol Shortcut Huerisitic
					double yDistance = Math.abs(vec.getY() - y2);
					if (xDistance > yDistance) {
						h = 14.0 * yDistance + 10.0 * (xDistance-yDistance);
					} else {
						h = 14.0 * xDistance + 10.0 * (yDistance-xDistance);
					}

					openList.add(new PathfindingTile(curLookingTile, vec.getX(), vec.getY(), g + h, g, h));
					added++;
				}
				if (added == 0) { closedList.clear(); return false; }
				if (openList.isEmpty()) { closedList.clear(); return false; }
				
				openList.remove(curLookingTile); // Add starting tile to closed list.
				closedList.add(curLookingTile);
				
				int lowest = 0;
				for (int i = 0; i < openList.size(); i++) { // Find lowest cost tile. (Will pick last tile if some are the same.)
					if (openList.get(i).getF() < openList.get(lowest).getF()) {
						lowest = i;
					}
				}
				
				curLookingTile = openList.get(lowest); // Set new tile to starting point, remove from open, add to closed.
				closedList.add(curLookingTile);
				openList.remove(lowest);
            }
			
			PathfindingTile tile = closedList.get(closedList.size() - 1); // Get last tile in path.
			while (true) {
				DungeonGenerator.placeTile(tiles, new Tile(TileDictionary.getFloorForTheme(), tile.getX(), tile.getY()));
				for (Direction dir : Direction.values()) {
					Vector vec = Misc.getLocFromDir(tile.getX(), tile.getY(), dir);
					try {
						Tile wall = tiles[vec.getX()][vec.getY()]; // Location of tobe wall, get tile there!
					} catch (ArrayIndexOutOfBoundsException ex) { continue; }
					wallsToBe.add(new Tile(TileDictionary.getWallForTheme(), vec.getX(), vec.getY()));
				}
				if (tile.getParent() == null) { break; }
				tile = tile.getParent(); // This causes us to iterate backwards until we reach the root tile!
			}
			return true;
	}
	
	public static void generateHallwayWalls(Tile[][] tiles) {
		for (Tile tile : wallsToBe) {
			Tile wall = tiles[tile.getX()][tile.getY()];
			if (wall.isReal()) { continue; }
			DungeonGenerator.placeTile(tiles, tile);
		}
	}
	
	public static void generateRooms(Tile[][] tiles, ArrayList<Room> rooms) {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles.length; y++) {
				if ((int) (Math.random() * 100) + 1 >= 95) {
					int width = (int) (Math.random() * 10) + 5;
					int height = (int) (Math.random() * 10) + 5;
					Room room = generateRoom(tiles, Misc.clamp((int) (Math.random() * tiles.length - width) + 1, 0, 10000), Misc.clamp((int) (Math.random() * tiles.length - width) + 1, 0, 10000), width, height);
					if (room == null) { continue; }
					rooms.add(room);
				}
			}
		}
	}
	
	public static void generateHallways(Tile[][] tiles, ArrayList<Room> rooms) {
		generateHallways(tiles, rooms, 0);
	}
	
	public static void generateHallways(Tile[][] tiles, ArrayList<Room> rooms, int startPoint) {
		if (startPoint > rooms.size() - 1) { return; }
		int count = 0;
		for (int i = startPoint; i < rooms.size(); i++) {
			if (count > 20) { rooms.get(i).delete(); count = 0; continue; }
			for (int j = 0; j < (int) (Math.random() * 3) + 1; j++) {
				Vector vec = rooms.get(i).getRandomWall();
				Vector vec2 = rooms.get((int) (Math.random() * rooms.size())).getRandomWall();
				if (vec == null || vec2 == null || (vec2.getX() == vec.getX() && vec2.getY() == vec2.getY())) { i = i - 1; break; }
				if (generateHallway(tiles, vec.getX(), vec.getY(), vec2.getX(), vec2.getY())) { rooms.get(i).addHallway(); }
				if (rooms.get(i).getConnections() <= 0) { i = i - 1; break; }
			}
			count++;
		}
	}
	
	public static void placePlayerInFeasibleLocation(GameMap map, Player ply) {
		Tile[][] tiles = map.getTiles();
		for (int x = 0; x < tiles.length; x++) {
			if ((int) (Math.random() * 100) + 1 <= 90) { continue; }
			for (int y = 0; y < tiles[0].length; y++) {
				if (tiles[x][y] == null || tiles[x][y].getName().equalsIgnoreCase("") || tiles[x][y].isWall() || (int) (Math.random() * 100) + 1 <= 80) { continue; }
				ply.setMap(map);
				ply.setTile(tiles[x][y]);
				return;
			}
		}
		try {
			placePlayerInFeasibleLocation(map, ply);
		} catch (StackOverflowError ex) {
			return;
		}
	}
	
	public static void placeItems(Tile[][] tiles, int w, int h, GameMap map) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if((int) (Math.random() * 100) + 1 <= 1) {
					Item item = ItemDictionary.getRandomItem();
					if (item == null || !tiles[x][y].isReal() || tiles[x][y].isWall()) { continue; }
					ArrayList<Entity> foundEnts = tiles[x][y].findType(Entity.class);
					if (foundEnts != null) { continue; }
					item.setMap(map);
					item.setTile(tiles[x][y]);
				}
			}
		}
	}
	
	public static void placeEntityRandomly(Tile[][] tiles, int w, int h, Entity ent) {
		if (ent == null) { return; }
		boolean placed = false;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if ((int) (Math.random() * 100) <= 3) {
					if (!tiles[x][y].isReal() || tiles[x][y].isWall()) { continue; }
					ArrayList<Entity> foundEnts = tiles[x][y].findType(Entity.class);
					if (foundEnts != null) { continue; }
					ent.setTile(tiles[x][y]);
					placed = true;
				}
			}
		}
		if (!placed) { placeEntityRandomly(tiles, w, h, ent); }
	}
	
	public static void placeMobs(Tile[][] tiles, int w, int h, GameMap map) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if((int) (Math.random() * 100) + 1 <= 4) {
					Mob mob = mobDictionary.getRandomMob();
					if (mob == null || !tiles[x][y].isReal() || tiles[x][y].isWall() || (int) (Math.random() * 100) >= (mob.getSpawnChance() + (TileDictionary.getDungeonLevel() * 2))) { continue; }
					ArrayList<Entity> foundEnts = tiles[x][y].findType(Entity.class);
					if (foundEnts != null) { continue; }
					mob.setMap(map);
					mob.setTile(tiles[x][y]);
				}
			}
		}
	}
	
	public static void placeTile(Tile[][] tiles, Tile tile) {
			if (tile.getX() < 0 || tile.getY() < 0 || tile.getName().trim().equalsIgnoreCase("") || tile.getX() >= tiles.length || tile.getY() >= tiles[0].length) { return; }
			tiles[tile.getX()][tile.getY()] = tile;
	}

	public static GameMap generateDungeon(int w, int h, MobDictionary mobDictionary) {
		DungeonGenerator.initMobDictionary(mobDictionary);
		wallsToBe = new ArrayList<Tile>();
		GameMap empty = new GameMap(w, h, TileDictionary.getDungeonLevel(), mobDictionary);
		
		Tile[][] tiles = new Tile[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				tiles[x][y] = new Tile("Empty", x, y); // Layout an entire dungeon full of "Empty" tiles.
			}
		}
		
		ArrayList<Room> rooms = new ArrayList<Room>();
		
		generateRooms(tiles, rooms);
		generateHallways(tiles, rooms);
		generateHallwayWalls(tiles);
		
		placeItems(tiles, w, h, empty);
		placeMobs(tiles, w, h, empty);
		
		placeEntityRandomly(tiles, Misc.DUNGEON_SIZE, Misc.DUNGEON_SIZE, new DownLadder()); // Places a ladder to next floor down.
		
		empty.setTiles(tiles);
		return empty;
	}
}
