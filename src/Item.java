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
	private String id;
	private String name;
    private ItemType type;
	private HashMap<String, Integer> stats = new HashMap<>();
	
	public Item(ItemDictionary itemDictionary, String name) {
		super(itemDictionary.getImage(name));
		this.id = name;
		this.name = NameGenerator.generateName(itemDictionary.getType(name));
		for (Map.Entry<String, JsonElement> ele : itemDictionary.getStats(name).entrySet()) {
			stats.put(ele.getKey(), ele.getValue().getAsInt());
		}
	}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "(Item | name: " + name + ", stats: " + stats + ", image: " + getImage() + ")";
	}
}
