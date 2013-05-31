import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Image;

/**
 * A class that makes the Items for the game.
 * 
 * @author Bobby Henley
 * @version 1
 */
public class Item extends Entity {
	private Image itemImage;
	private HashMap<String, Integer> stats = new HashMap<>();
	
	public Item(ItemDictionary itemDictionary, GameMap map, String name) {
		super(itemDictionary.getImage(name), map);
		for (Map.Entry<String, JsonElement> ele : itemDictionary.getStats(name).entrySet()) {
			stats.put(ele.getKey(), ele.getValue().getAsInt());
		}
		System.out.println(stats);
	}
}
