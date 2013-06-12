import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;
import com.google.gson.JsonObject;
import java.util.Map;
import org.newdawn.slick.Graphics;

/**
 * The Player that you control.
 * @author Bobby Henley
 * @version 1
 */
public class Player extends Entity {
	private HashMap<String, Integer> godsFavor = new HashMap<>();
	private HashMap<String, Integer> stats = new HashMap<>();
	private GameMap curMap;
	private String plyClass = "";
	private ArrayList<Item> inventoryItems;
	private ArrayList<Item> equippedItems;
	private int equipLoc;
	private boolean frozen;

	public Player(SpriteSheet ss, JsonObject data, GameMap map) {
		super(ss.getSubImage(data.get("sx").getAsInt(), data.get("sy").getAsInt()));
		curMap = map;
		inventoryItems = new ArrayList<>();
		equippedItems = new ArrayList<>();
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
				addItem((Item) foundItems.get(i));
			}
		}
	}
	
	public void isFrozen(boolean b) {
		frozen = b;
	}
	
	public void addItem(Item i) {
		if(inventoryItems.size() > 100) {
			Misc.showDialog("I'm alredy carrying too much!");
			return;
		}
		System.out.println("Calling addItem from: " +this.getClass());
		i.setTile(null);
		inventoryItems.add(i);
	}

	public void removeItem(Item i) {
		inventoryItems.remove(i);
	}
	
	public void equipItem(Item i) {
		if(i == null) { return; }
		System.out.println(i.getID());
		if((i.getDict().getType(i.getID()) == ItemType.SWORD)||
		(i.getDict().getType(i.getID()) == ItemType.LONGSWORD)||
		(i.getDict().getType(i.getID()) == ItemType.DAGGER)||
		(i.getDict().getType(i.getID()) == ItemType.SPEAR)||
		(i.getDict().getType(i.getID()) == ItemType.MACE)||
		(i.getDict().getType(i.getID()) == ItemType.STAFF)||
		(i.getDict().getType(i.getID()) == ItemType.INSTRUMENT)||
		(i.getDict().getType(i.getID()) == ItemType.WAND)||
		(i.getDict().getType(i.getID()) == ItemType.FLAIL)||
		(i.getDict().getType(i.getID()) == ItemType.HALBERD)) {
			equipLoc = 32;
			System.out.println("Weapon");
		}else if (i.getDict().getType(i.getID()) == ItemType.HEADGEAR) {
			equipLoc = 0;
			System.out.println("Head");
		}else if (i.getDict().getType(i.getID()) == ItemType.TORSO) {
			equipLoc = 64;
			System.out.println("Torso");
		}else if (i.getDict().getType(i.getID()) == ItemType.LEGS) {
			equipLoc = 96;
			System.out.println("Legs");
		}else if (i.getDict().getType(i.getID()) == ItemType.FEET) {
			equipLoc = 128;
			System.out.println("Feet");
		}else if (i.getDict().getType(i.getID()) == ItemType.GAUNTLETS) {
			equipLoc = 160;
			System.out.println("Gauntlets");
		}else if (i.getDict().getType(i.getID()) == ItemType.PAULDRONS) {
			equipLoc = 192;
			System.out.println("Pauldrons");
		}else if (i.getDict().getType(i.getID()) == ItemType.WAIST) {
			equipLoc = 224;
			System.out.println("Waist");
		}else if (i.getDict().getType(i.getID()) == ItemType.CLOAK) {
			equipLoc = 256;
			System.out.println("Cloak");
		}
		equippedItems.add(i);
	}
	
	public void unequipItem(Item i) {
		equippedItems.remove(i);
		inventoryItems.add(i);
	}
	
	public int getEquipLoc() {
		return equipLoc;
	}
	
	public ArrayList<Item> getPlayerItems() {
		return inventoryItems;
	}
	
	public ArrayList<Item> getEquippedItems() {
		return equippedItems;
	}
	
	public int getStat(String stat) {
		return stats.get(stat);
	}
	
	public HashMap getStats() {
		return stats;
	}
	
	public GameMap getMap() {
		return curMap;
	}
	
	public void update(GameContainer container) {
		if (container.getInput().isKeyPressed(Input.KEY_LEFT)) {
			this.move(Direction.LEFT);
		}
		if (container.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			this.move(Direction.RIGHT);
		}
		if (container.getInput().isKeyPressed(Input.KEY_DOWN)) {
			this.move(Direction.DOWN);
		}
		if (container.getInput().isKeyPressed(Input.KEY_UP)) {
			this.move(Direction.UP);
		}
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			DungeonGenerator.placePlayerInFeasibleLocation(getMap().getTiles(), this);
		}
		if (container.getInput().isKeyPressed(Input.KEY_R)) {
			getMap().regen();
			DungeonGenerator.placePlayerInFeasibleLocation(getMap().getTiles(), this);
		}
	}
	
	public void draw(Graphics g, int x, int y) {
		if (!getVisible()) { return; }
		g.drawImage(getImage(), x, y);
	}
	
	/*@Override
	public String toString() {
		return "(Player | class: " + plyClass + ", stats: " + stats + ", tileX: " + getTile().getX() + ", tileY: " + getTile().getY() + ")";
	}*/
}
