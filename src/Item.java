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
	private String name;
	private HashMap<String, Integer> stats = new HashMap<>();
	
	public Item(ItemDictionary itemDictionary, GameMap map, String name) {
		super(itemDictionary.getImage(name), map);
		this.name = name;
		for (Map.Entry<String, JsonElement> ele : itemDictionary.getStats(name).entrySet()) {
			stats.put(ele.getKey(), ele.getValue().getAsInt());
		}
		System.out.println(stats);
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "(Item | name: " + name + ", stats: " + stats + ", image: " + getImage() + ")";
	}
}
