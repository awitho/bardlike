import java.util.ArrayList;
import java.util.HashMap;

/**
 * A basic enemy class, bosses tbi.
 * @author Alex
 */
public class Mob extends Entity {
	private HashMap<String, Integer> stats = new HashMap<>();
	private String name;
	private boolean dead = false;
        
	public Mob(MobDictionary mobDictionary, String name) {
		super(mobDictionary.getMobImage(name));
		stats.put("health", mobDictionary.getHealth(name));
		stats.put("curhp", stats.get("health"));
		stats.put("defense", mobDictionary.getStat(name, "def"));
		stats.put("damage", mobDictionary.getStat(name, "atk"));
		this.name = name;
	}
        
        public String getName() {
            return name;
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
			ply.log("You killed " + getName() + "!");
            dead = true;
            setTile(null);
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
