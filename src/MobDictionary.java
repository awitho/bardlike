import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 * A class that holds all of the mobs.
 * 
 * @author Bobby Henley
 * @version 1
 */

public class MobDictionary {
	private SpriteSheet mobSprites;
	private HashMap<String, Image> mobImages;
	private JsonArray mobArray;
	private JsonObject mob;
	
	public MobDictionary() {
		mobImages = new HashMap<>();
		try {
			mobSprites = new SpriteSheet("./gfx/mobs.png", 32, 32);
			mobArray = new GameConfig("mobs.json").getArray();
		} catch (SlickException e) {
			Misc.showDialog(e);
		}
		
		for(int i = 0; i < mobArray.size(); i++) {
			mob = mobArray.get(i).getAsJsonObject();
			mobImages.put(mob.get("name").getAsString(), mobSprites.getSubImage(mob.get("sx").getAsInt(), mob.get("sy").getAsInt()).getScaledCopy(Misc.TARGET_SIZE, Misc.TARGET_SIZE));
		}
	}
	
	public Image getMobImage(String name) {
		return mobImages.get(name);
	}
	
	public int getHealth() {
		return mob.get("health").getAsInt();
	}
	
	public Mob getRandomMob() {
		for (int i = 0; i < mobArray.size(); i++) {
			if (mobArray.get(i) == null || (int) (Math.random() * mobArray.size()) == 0) { continue; }
			return new Mob(this, mobArray.get(i).getAsJsonObject().get("name").getAsString());
		}
		return null;
	}
}
