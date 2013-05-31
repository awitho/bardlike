import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import com.google.gson.JsonObject;
import java.util.Map;

/**
 * The Player that you control.
 * @author Bobby Henley
 * @version 1
 */
public class Player extends Entity {
	private HashMap<String, Integer> godsFavor = new HashMap<>();
	private HashMap<String, Integer> stats = new HashMap<>();
	private String plyClass = "";
	private ArrayList<Item> inventoryItems;
	private boolean held;

	public Player(SpriteSheet ss, JsonObject data, GameMap map) {
		super(ss.getSubImage(data.get("sx").getAsInt(), data.get("sy").getAsInt()), map);
		inventoryItems = new ArrayList<Item>();
		plyClass = data.get("name").getAsString();
		for(Map.Entry<String, JsonElement> entry: data.get("stats").getAsJsonObject().entrySet()){
				stats.put(entry.getKey(), entry.getValue().getAsInt());
		}
	}

	public void move(Direction dir) {
		if(!held) {
			System.out.println("Attempting to move in dir: " + dir);
			Tile tile = getMap().moveEnt(getTile(), this, dir);
			
			if(tile == null) { return; }
			ArrayList<Entity> foundItems = tile.findType(Item.class);
			
			if(foundItems == null) { return; }
			
			for(int i = 0; i < foundItems.size(); i++) {
				foundItems.get(i).setTile(null);
				addItem((Item) foundItems.get(i));
			}
		}
	}
	
	public void isHeld(boolean b) {
		held = b;
	}
	
	public void addItem(Item i) {
		inventoryItems.add(i);
	}
	
	public void removeItem(Item i) {
		inventoryItems.remove(i);
	}
	
	public ArrayList<Item> getPlayerItems() {
		return inventoryItems;
	}
	
	public int getStat(String stat) {
		return stats.get(stat);
	}
	
	public HashMap getStats() {
		return stats;
	}
	
	public String toString() {
		return plyClass + ": " + stats;
	}
}
