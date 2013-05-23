
import java.util.ArrayList;

/**
 *
 * @author alex
 */
public class DungeonGenerator {
	public static void generateRoom() {
	}
	
	public static void generateHallway(int x1, int y1, int x2, int y2) {
		
	}
	
	public static ArrayList<ArrayList<Tile>> generateDungeon(int w, int h, TileDictionary tileDictionary) {
		ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
		String[] temp = new String[tileDictionary.size()];
        temp[0] = "Stone";
        temp[1] = "Wood";
        temp[2] = "Glass";
        temp[3] = "Obsidian";
		for(int x = 0; x < w; x++) {
			ArrayList<Tile> tileY = new ArrayList<>();
			for(int y = 0; y < h; y++) {
				tileY.add(new Tile(tileDictionary, temp[(int) (Math.random() * temp.length - 1)]).setSeen(true).setInLos(Misc.randomBool()));
			}
			tiles.add(tileY);
		}
		return tiles;
	}
}
