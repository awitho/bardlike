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
	private boolean frozen;

	public Player(SpriteSheet ss, JsonObject data, GameMap map) {
		super(ss.getSubImage(data.get("sx").getAsInt(), data.get("sy").getAsInt()), map);
		inventoryItems = new ArrayList<>();
		plyClass = data.get("name").getAsString();
		for(Map.Entry<String, JsonElement> entry: data.get("stats").getAsJsonObject().entrySet()){
				stats.put(entry.getKey(), entry.getValue().getAsInt());
		}
		//addItem(new Item(new ItemDictionary(), getMap(), "Leather Helmet"));
	}

	public void move(Direction dir) {
		if(!frozen) {
			Tile curTile = getTile();
			if (curTile == null) { System.out.println("Player.move: Player is not currently in map!"); return; };
			System.out.println("Player.move: Attempting to move in dir: " + dir);
			Tile tile = getMap().moveEnt(curTile, this, dir);

			if(tile == null) { return; }
			ArrayList<Entity> foundItems = tile.findType(Item.class);
			
			if(foundItems == null) { return; }
			
			for(int i = 0; i < foundItems.size(); i++) {
				foundItems.get(i).setTile(null);
				this.getTile().removeEnt(foundItems.get(i));
				addItem((Item) foundItems.get(i));
			}
		}
	}
	
	public void isFrozen(boolean b) {
		frozen = b;
	}
	
	public void addItem(Item i) {
		if(inventoryItems.size() >= 100) {
			Misc.showDialog("I'm alredy carrying too much!");
			return;
		}
		System.out.println("Calling addItem from: " +this.getClass());
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
	
	@Override
	public String toString() {
		return "(Player | class: " + plyClass + ", stats: " + stats + ", tileX: " + getTile().getX() + ", tileY: " + getTile().getY() + ")";
	}
}
