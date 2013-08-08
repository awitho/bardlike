package me.bloodarowman.bardlike;

import me.bloodarowman.bardlike.gui.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;
import com.google.gson.JsonObject;
import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.newdawn.slick.Graphics;

/**
 * The Player that you control.
 * @author Bobby Henley
 * @version 1
 */
public class Player extends Entity {
	private static ArrayList<File> moveHooks = Misc.findFilesRecurse("./scripts/hooks/playerMove/", "(.*).lua");

    //TODO: Implement gods
	// private HashMap<String, Integer> godsFavor = new HashMap<String, Integer>();
	private HashMap<String, Integer> stats = new HashMap<String, Integer>();
	private HashMap<String, Integer> bonuses = new HashMap<String, Integer>();
	private ArrayList<Buff> buffs = new ArrayList<Buff>();
	private MainGameState mgs;
	private JsonArray xpTable; // Required exp for each level.
	private String plyClass, mainStat; // Would be part of stats, but it can't be.
	private ArrayList<Item> inventoryItems, equippedItems;
    private Log log;
	private boolean frozen, dead;

    public Player(SpriteSheet ss, JsonObject data, Log log, MainGameState mgs) {
		super(ss.getSubImage(data.get("sx").getAsInt(), data.get("sy").getAsInt()));
		this.log = log;
		this.mgs = mgs;
		inventoryItems = new ArrayList<Item>();
		equippedItems = new ArrayList<Item>();
		xpTable = new GameConfig("exp.json").getArray();
		plyClass = data.get("name").getAsString();

		try {
			mainStat = data.get("mainstat").getAsString();
		} catch (NullPointerException ignored) {}

		for(Map.Entry<String, JsonElement> entry: data.get("stats").getAsJsonObject().entrySet()) {
				stats.put(entry.getKey(), entry.getValue().getAsInt());
        }

        resetBonuses();
        resetLevel();
        revive();

        hookLuaFuncs();

        System.out.println(moveHooks);
	}

    public void hookLuaFuncs() {
        Main.luaThread.getLuaState().newTable();
        Main.luaThread.getLuaState().pushValue(-1);
        Main.luaThread.getLuaState().setGlobal("player");

        try {
            Main.luaThread.getLuaState().pushString("revive");
            Main.luaThread.getLuaState().pushJavaFunction(new JavaFunction(Main.luaThread.getLuaState()) {
                public int execute() throws LuaException {
                    revive();
                    return 0;
                }
            });

            Main.luaThread.getLuaState().pushString("die");
            Main.luaThread.getLuaState().pushJavaFunction(new JavaFunction(Main.luaThread.getLuaState()) {
                public int execute() throws LuaException {
                    die();
                    return 0;
                }
            });

            Main.luaThread.getLuaState().pushString("levelUp");
            Main.luaThread.getLuaState().pushJavaFunction(new JavaFunction(Main.luaThread.getLuaState()) {
                @Override
                public int execute() throws LuaException {
                    levelUp();
                    return 0;
                }
            });

            Main.luaThread.getLuaState().pushString("setXp");
            Main.luaThread.getLuaState().pushJavaFunction(new JavaFunction(Main.luaThread.getLuaState()) {
                @Override
                public int execute() throws LuaException {
                    if (L.getTop() > 1) {
                        setXp((int) Math.round(getParam(2).getNumber()));
                    }
                    return 0;
                }
            });
        } catch (LuaException ex) {
            ex.printStackTrace();
        }

        Main.luaThread.getLuaState().setTable(-9);
    }

	public void move(Direction dir) {
		Misc.LuaExecFileList(moveHooks);

		if(frozen || dead) { return; }
		Tile curTile = getTile();
		if (curTile == null) { log.append("The game has errored while generating the dungeon, please restart the game."); return; }
		Vector vec = Misc.getLocFromDir(curTile.getX(), curTile.getY(), dir);
		Tile tile = getMap().getTile(vec.getX(), vec.getY());
		if (tile == null || tile.isWall()) { return; }
		
		ArrayList<Entity> foundLadders = tile.findType(DownLadder.class);
		if (foundLadders != null) {
			DungeonGenerator.placePlayerInFeasibleLocation(mgs.setLevel(mgs.genNewLevel()), this);
			return;
		}
		
		getMap().update(mgs);
		
		ArrayList<Entity> foundMobs = tile.findType(Mob.class);
		Mob mob = null;
		if (foundMobs != null) {
			mob = (Mob) foundMobs.get(0);
			mob.use(this, getStat("str"));
		}
		
		if (!dead) {
			if (mob != null && !mob.isDead()) {} else {
                mgs.getCam().setTime(400);
				setTile(tile);
			}
		}
		
		getMap().updateAttacks();
		
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
			log.append("You picked up a " + i.getName() + ".");
		}
		i.setTile(null);
		inventoryItems.add(i);
	}

	public void removeItem(Item i) {
		inventoryItems.remove(i);
	}
	
	public void equipItem(Item item) {
		equippedItems.add(item);
		updateBonuses();
	}
	
	public void unequipItem(Item i) {
		if(i == null) { return; }
		equippedItems.remove(i);
		inventoryItems.add(i);
		updateBonuses();
	}
        
	public void updateBonuses() {
		resetBonuses();
		for (int i = 0; i < equippedItems.size(); i++) {
			for (Entry<String, Integer> ele : getEquippedItems().get(i).getStats().entrySet()) {
				System.out.println(ele);
				bonuses.put(ele.getKey(), ele.getValue() + bonuses.get(ele.getKey()));
			}
		}
	}
	
	public void updateBuffs() {
		for (int i = 0; i < buffs.size(); i++) {
			buffs.get(i).update();
		}
	}
	
	public void removeBuff(int id) {
		for (int i = 0; i < buffs.size(); i++) {
			if (buffs.get(i).getID() == id) {
				buffs.set(i, null);
			}
		}
	}
	
	public void clearInventory() {
		equippedItems.clear();
		inventoryItems.clear();
	}

    public ArrayList<Item> getPlayerItems() {
		return inventoryItems;
	}
	
	public ArrayList<Item> getEquippedItems() {
		return equippedItems;
	}
	
	public int getStat(String stat) {
		return stats.get(stat) + bonuses.get(stat);
	}
	
	public HashMap getStats() {
		return stats;
	}

    public void resetBonuses() {
        bonuses.put("end", 0);
        bonuses.put("agi", 0);
        bonuses.put("int", 0);
        bonuses.put("dex", 0);
        bonuses.put("str", 0);
    }

    public void resetLevel() {
        stats.put("xp", 0);
        stats.put("level", 1);
    }
	
	public void levelUp() {
		stats.put("level", getLevel() + 1);
		log.append("You leveled up to level " + getLevel() + "!");
		for (Entry<String, Integer> ele : stats.entrySet()) {
			if (ele.getKey().equalsIgnoreCase("level") || ele.getKey().equalsIgnoreCase("xp") || ele.getKey().equalsIgnoreCase("curhp")) { continue; }
			if (mainStat != null && ele.getKey().equalsIgnoreCase(mainStat)) {
				ele.setValue(ele.getValue() + (int) (Math.random() * 5) + 1);
			} else {
				ele.setValue(ele.getValue() + (int) (Math.random() * 2) + 1);
			}
			log.append(ele.getKey() + " went up to " + ele.getValue());
		}
		stats.put("curhp", getStat("end") * 10);
	}
	
	public void setLevel(int level) {
		stats.put("level", level);
	}
	
	public int getLevel() {
		return stats.get("level");
	}
	
	public int getMaxLevel() {
		return xpTable.size();
	}
	
	public int getXP() {
		return stats.get("xp");
	}
	
	public void addXP(int amount) {
		setXp(getXP() + amount);
	}

	public void setXp(int amount) {
		if (getLevel() >= getMaxLevel()) { return; }
		int requiredXP = xpTable.get(getLevel()).getAsInt();
		if (amount > requiredXP) {
			levelUp();
			setXp(amount - requiredXP);
			return;
		}
		stats.put("xp", amount);
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
			//DungeonGenerator.placePlayerInFeasibleLocation(getMap(), this);
			getMap().update(mgs);
		}
		updateBuffs();
	//	if (container.getInput().isKeyPressed(Input.KEY_V)) {
	//		DungeonGenerator.placePlayerInFeasibleLocation(mgs.setLevel(mgs.genNewLevel()), this);
	//	}
	}
	
	public void draw(Graphics g, int x, int y) {
		if (!getVisible()) { return; }
        setX(x);
        setY(y);
		g.drawImage(getImage(), x, y);
	}
}
