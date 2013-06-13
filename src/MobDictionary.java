import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map.Entry;
/**
 * A class that holds all of the mobs.
 * 
 * @author Bobby Henley
 * @version 1
 */

public class MobDictionary {
	private SpriteSheet mobSprites;
	private HashMap<String, Image> mobImages;
	private HashMap<String, Integer> mobMaxHP;
	private HashMap<String, HashMap<String, Integer>> stats;
	private JsonArray mobArray;
	
	public MobDictionary() {
		mobImages = new HashMap<>();
		mobMaxHP = new HashMap<>();
		stats = new HashMap<>();
		try {
			mobSprites = new SpriteSheet("./gfx/mobs.png", 32, 32);
			mobArray = new GameConfig("mobs.json").getArray();
		} catch (SlickException e) {
			Misc.showDialog(e);
		}
		
		for(int i = 0; i < mobArray.size(); i++) {
			JsonObject mob = mobArray.get(i).getAsJsonObject();
			mobImages.put(mob.get("name").getAsString(), mobSprites.getSubImage(mob.get("sx").getAsInt(), mob.get("sy").getAsInt()).getScaledCopy(Misc.TARGET_SIZE, Misc.TARGET_SIZE));
			mobMaxHP.put(mob.get("name").getAsString(), mob.get("health").getAsInt());
			HashMap<String, Integer> tempStats = new HashMap<>();
			JsonObject jStats = mob.get("stats").getAsJsonObject();
			for (Entry<String, JsonElement> ele : jStats.entrySet()) {
				tempStats.put(ele.getKey(), ele.getValue().getAsInt());
			}
			stats.put(mob.get("name").getAsString(), tempStats);
			
			System.out.println(stats);
		}
	}
	
	public Image getMobImage(String name) {
		return mobImages.get(name);
	}
	
	public int getHealth(String name) {
		return mobMaxHP.get(name);
	}
	
	public HashMap<String, Integer> getStats(String name) {
		return stats.get(name);
	}
	
	public int getStat(String name, String statName) {
		return stats.get(name).get(statName);
	}
	
	public Mob getRandomMob() {
		for (int i = 0; i < mobArray.size(); i++) {
			if (mobArray.get(i) == null || (int) (Math.random() * mobArray.size()) == 0) { continue; }
			return new Mob(this, mobArray.get(i).getAsJsonObject().get("name").getAsString());
		}
		return null;
	}
}
