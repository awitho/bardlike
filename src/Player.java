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
	//private int curHP;
	private String plyClass = "";
	private ArrayList<Item> inventoryItems;
	private ArrayList<Item> equippedItems;
	private int equipLoc;
	private Log log;
	private boolean frozen, dead;

	public Player(SpriteSheet ss, JsonObject data, Log log) {
		super(ss.getSubImage(data.get("sx").getAsInt(), data.get("sy").getAsInt()));
		inventoryItems = new ArrayList<>();
		equippedItems = new ArrayList<>();
		this.log = log;
		plyClass = data.get("name").getAsString();
		for(Map.Entry<String, JsonElement> entry: data.get("stats").getAsJsonObject().entrySet()){
				stats.put(entry.getKey(), entry.getValue().getAsInt());
		}
		revive();
		//addItem(new Item(new ItemDictionary(), getMap(), "Leather Helmet"));
	}

	public void move(Direction dir) {
		if(frozen || dead) { return; }
		Tile curTile = getTile();
		if (curTile == null) { System.out.println("Player.move: Player is not currently in map!"); return; };
		//System.out.println("Player.move: Attempting to move in dir: " + dir);
		Vector vec = Misc.getLocFromDir(curTile.getX(), curTile.getY(), dir);
		Tile tile = getMap().getTile(vec.getX(), vec.getY());
		if (tile == null) { return; }
		
		ArrayList<Entity> foundMobs = tile.findType(Mob.class);
		Mob mob = null;
		if (foundMobs != null) {
			mob = (Mob) foundMobs.get(0);
			mob.use(this, stats.get("str")*2);
		}

		getMap().update();
		if (mob != null && !mob.isDead()) { return; }
		if (!dead)
			setTile(tile);
		
		ArrayList<Entity> foundItems = tile.findType(Item.class);
		if (foundItems != null) { 
			for(int i = 0; i < foundItems.size(); i++) {
				addItem((Item) foundItems.get(i));
			}
		}
	}
	
	public void setLog(Log log) {
		this.log = log;
	}
        
        public void log(String str) {
            log.append(str);
        }
	
	public int getMaxHP() {
		return stats.get("health");
	}
	
	public int getHP() {
		return stats.get("curhp");
	}
	
	public void setHP(int hp) {
		stats.put("curhp", hp);
	}
	
	public void use(Entity ent, int amt) {
		if (dead) { return; }
                Mob mob = (Mob) ent;
		log.append("Your were hit by " + mob.getName() + " for " + amt + " damage!");
		setHP(getHP() - amt);
		if (getHP() <= 0) {
			setHP(0);
			die();
		}
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void die() {
		dead = true;
		log.append("You have died!");
		// Goto game over/menu. && delete save.
	}

	public void revive() {
		dead = false;
		stats.put("curhp", stats.get("end")*10);
		log.append("You have been revived from the dead!");
	}
	
	public void isFrozen(boolean b) {
		frozen = b;
	}
	
	public void addItem(Item i) {
		if(inventoryItems.size() >= 110) {
			log.append("Your inventory is full, you cannot pickup anymore items!");
			return;
		}
		if (i.isNamed()) {
			log.append("You picked up \"" + i.getName() + "\".");
		} else {
			log.append("You picked up a " + i.getName());
		}
		i.setTile(null);
		inventoryItems.add(i);
	}

	public void removeItem(Item i) {
		inventoryItems.remove(i);
	}
	
	public void equipItem(Item i) {
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
			DungeonGenerator.placePlayerInFeasibleLocation(getMap().getTiles(), this, getMap());
		}
		if (container.getInput().isKeyPressed(Input.KEY_R)) {
			revive();
			getMap().regen();
			DungeonGenerator.placePlayerInFeasibleLocation(getMap().getTiles(), this, getMap());
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
