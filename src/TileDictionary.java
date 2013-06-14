import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 * A class to handle all the data for tile types.
 * @version 1
 * @since 5/26/13
 * @author alex
 */
public class TileDictionary {
	private static HashMap<String, Image> imgs;
	private static HashMap<String, Boolean> walls;
	private static LinkedHashMap<Integer, String> themesWalls;
	private static LinkedHashMap<Integer, String> themesFloors;
	private static int dungeonLevel = 0;

	public static void initTileDictionary() {
		walls = new HashMap<>();
		imgs = new HashMap<>();
		themesWalls = new LinkedHashMap<>();
		themesFloors = new LinkedHashMap<>();
		try {
			SpriteSheet sprites = new SpriteSheet("./gfx/tiles.png", 32, 32);
			JsonArray tiles = new GameConfig("tiles.json").getArray();
			JsonObject themes = new GameConfig("themes.json").getObject();
			for (Entry<String, JsonElement> ele : themes.entrySet()) {
				TileDictionary.themesWalls.put(Integer.parseInt(ele.getKey()), ele.getValue().getAsJsonObject().get("walls").getAsString());
				TileDictionary.themesFloors.put(Integer.parseInt(ele.getKey()), ele.getValue().getAsJsonObject().get("floors").getAsString());
			}
			System.out.println(themes + ", " + themesWalls);
			for (int i = 0; i < tiles.size(); i++) {
				JsonObject tile = tiles.get(i).getAsJsonObject();
				imgs.put(tile.get("name").getAsString(), sprites.getSubImage(tile.get("sx").getAsInt(), tile.get("sy").getAsInt()).getScaledCopy(Misc.TARGET_SIZE, Misc.TARGET_SIZE));
				
				Boolean isWall = tile.get("wall").getAsBoolean();
				if (isWall) {
					walls.put(tile.get("name").getAsString(), isWall);
				}
			}
			imgs.put("Overlay", new Image("gfx/tile_overlay.png"));
			imgs.put("Empty", new Image("gfx/tile_overlay.png"));
			walls.put("Overlay", true);
			walls.put("Empty", true);
		} catch (Exception e) {
			Misc.showDialog(e);
			System.out.println(themesWalls + ", " + themesFloors);
			return;
		}
	}

	public static int size() {
		return imgs.size();
	}

	public static boolean getTileIsWall(String name) {
		Boolean isWall = walls.get(name);
		if (isWall != null) { return true; }
		return false;
	}

	public static Image getTileImageByName(String name) {
		return imgs.get(name);
	}
	
	public static void setDungeonLevel(int l) {
		dungeonLevel = l;
	}
	
	public static int getDungeonLevel() {
		return dungeonLevel;
	}
	
	public static String getWallForTheme() {
		for (Entry<Integer, String> ele : themesWalls.entrySet()) {
			if (dungeonLevel < ele.getKey()) { continue; }
			System.out.println(ele.getKey() + ", " + dungeonLevel);
			return ele.getValue();
		}
		return "Stone";
	}
	
	public static String getFloorForTheme() {
		for (Entry<Integer, String> ele : themesFloors.entrySet()) {
			if (dungeonLevel < ele.getKey()) { continue; }
			return ele.getValue();
		}
		return "Wood";
	}

	public static String getRandomWall() {
		for (Map.Entry<String, Boolean> ele : walls.entrySet()) {
			if (ele.getKey().equalsIgnoreCase("Empty") || ele.getKey().equalsIgnoreCase("Overlay") || (int) (Math.random() * 2) - 1 == 0) { continue; }
				return ele.getKey();
		}
		return "Stone";
	}
	
	public static String getRandomNonwall() {
		for (Map.Entry<String, Image> ele : imgs.entrySet()) {
			if (walls.get(ele.getKey()) != null || (int) (Math.random() * 2) - 1 == 0) { continue; }
			return ele.getKey();
		}
		return "Wood";
	}
}
