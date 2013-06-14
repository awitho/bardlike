import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that makes the Items for the game.
 * 
 * @author Bobby Henley
 * @version 1
 */
public class Item extends Entity {
	private ItemDictionary itemDictionary;
	private String id;
	private String name;
    private ItemType type;
	private WeaponType weaponType;
	private HashMap<String, Integer> stats = new HashMap<>();
	
	public Item(String name) {
		super(ItemDictionary.getImage(name));
		this.id = name;
		this.type = ItemDictionary.getType(name);
		this.weaponType = ItemDictionary.getWeaponType(name);
		if ((int) (Math.random() * 100) <=45) {
			if (weaponType != null) {
				this.name = NameGenerator.generateName(weaponType.toString());
			} else {
				this.name = NameGenerator.generateName(type.toString());
			}
		}
		for (Map.Entry<String, JsonElement> ele : ItemDictionary.getStats(name)
					.entrySet()) {
			stats.put(ele.getKey(), ele.getValue().getAsInt());
		}
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name != null ? name : id;
	}
	
	public HashMap<String, Integer> getStats() {
		return stats;
	}
	
	public boolean isNamed() {
		return this.name != null;
	}
	
	public ItemType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "(Item | name: " + getName() + ", stats: " + stats + ", image: "
				+ getImage() + ", type: " + type + ")";
	}
}
