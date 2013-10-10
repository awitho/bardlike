package me.bloodarowman.bardlike;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A basic enemy class, attempts to assault the player.
 * @author Alex
 */
public class Mob extends Entity {
	private static final int EXP_MULT = 5;
	private HashMap<String, Integer> stats = new HashMap<String, Integer>();
	private String name;
	private boolean dead, moved = false;
        
	public Mob(MobDictionary mobDictionary, String name) {
		super(mobDictionary.getMobImage(name));
		stats.put("health", mobDictionary.getHealth(name));
		stats.put("curhp", stats.get("health"));
		stats.put("defense", mobDictionary.getStat(name, "def"));
		stats.put("damage", mobDictionary.getStat(name, "atk"));
		stats.put("spawnchance", mobDictionary.getSpawnChance(name));
		this.name = name;
	}
        
	public String getName() {
		return name;
	}
	
	public int getSpawnChance() {
		return stats.get("spawnchance");
	}
	
	public void attack(Entity ent) {
		Player ply = (Player) ent;
		ply.use(this, stats.get("damage"));
	}
        
	public void use(Entity ent, int amt) {
		Player ply = (Player) ent;
		ply.log("You hit " + getName() + " for " + amt + " damage.");
		stats.put("curhp", stats.get("curhp") - amt);
		if (stats.get("curhp") <= 0) {
			die(ent);
		}
	}
        
	public boolean isDead() {
		return dead;
	}
        
	public void die(Entity ent) {
		Player ply = (Player) ent;
		ply.log("You killed " + getName() + ", gaining " + stats.get("damage")*EXP_MULT + "xp!");
		ply.addXP(stats.get("damage")*EXP_MULT);
		dead = true;
		setTile(null);
	}
	
	public void setMoved(boolean bool) {
		moved = bool;
	}
        
	@Override
	public void update(MainGameState mgs) {
		if (moved) { return; }
		if ((Math.abs(getTile().getX() - mgs.getPlayer().getTile().getX()) + Math.abs(getTile().getY() - mgs.getPlayer().getTile().getY())) <= 8) {
			ArrayList<PathfindingTile> tiles = Misc.pathfindTo(getMap(), getTile().getX(), getTile().getY(), mgs.getPlayer().getTile().getX(), mgs.getPlayer().getTile().getY());
			if (tiles == null || tiles.size() == 1) { return; }
			PathfindingTile tile = tiles.get(tiles.size() - 1); // Get last tile in path.
			while (true) {
				if (tile.getParent().getParent() == null) {
					Tile tile2 = getMap().getTile(tile.getX(), tile.getY());
					ArrayList<Entity> ents = tile2.findType(Player.class);
					ArrayList<Entity> ents2 = tile2.findType(Mob.class);
					if (ents != null || ents2 != null) { break; }
					setTile(tile2);
					moved = true;
					break;
				}
				tile = tile.getParent(); // This causes us to iterate backwards until we reach the root tile!
			}
		}
	}

	@Override
	public void updateAttacks() {
		if (moved) { return; }
		for (Direction dir : Direction.values()) {
			Vector vec = Misc.getLocFromDir(getTile().getX(), getTile().getY(), dir);
			Tile tile = getMap().getTile(vec.getX(), vec.getY());
			if (tile == null) { continue; }
			ArrayList<Entity> players = tile.findType(Player.class);
			if (players != null) { attack(players.get(0)); }
		}
	}
}
