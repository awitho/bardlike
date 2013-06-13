import java.util.ArrayList;
import java.util.HashMap;

/**
 * A basic enemy class, bosses tbi.
 * @author Alex
 */
public class Mob extends Entity {
	private HashMap<String, Integer> stats = new HashMap<>();
	private String name;
	
	public Mob(MobDictionary mobDictionary, String name) {
		super(mobDictionary.getMobImage(name));
		stats.put("health", mobDictionary.getHealth(name));
		stats.put("curhp", stats.get("health"));
		stats.put("defense", mobDictionary.getStat(name, "def"));
		stats.put("damage", mobDictionary.getStat(name, "atk"));
		this.name = name;
	}
	
	public void attack(Entity ent) {
		Player ply = (Player) ent;
		ply.use(stats.get("damage"));
	}
	
	@Override
	public void update() {
		for (Direction dir : Direction.values()) {
			Vector vec = Misc.getLocFromDir(getTile().getX(), getTile().getY(), dir);
			Tile tile = getMap().getTile(vec.getX(), vec.getY());
			if (tile == null) { continue; }
			ArrayList<Entity> players = tile.findType(Player.class);
			if (players != null) { attack(players.get(0)); }
		}
	}
}
