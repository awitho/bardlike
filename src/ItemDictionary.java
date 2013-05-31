import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A class to handle items.
 * 
 * @author Bobby Henley
 * @version 1
 */
public class ItemDictionary {
	private SpriteSheet itemSprites;
	private HashMap<String, Image> itemImages;
	private JsonArray items;
	private JsonObject curItem;
	
	public ItemDictionary() {
		itemImages = new HashMap<>();
		try {
			items = new GameConfig("items.json").getArray();
			itemSprites = new SpriteSheet("./gfx/testitemsheet.png", 32, 32);
			for(int i = 0; i < items.size(); i++) {
				curItem = items.get(i).getAsJsonObject();
				itemImages.put(curItem.get("name").getAsString(), itemSprites.getSubImage(curItem.get("sx").getAsInt(), curItem.get("sy").getAsInt()).getScaledCopy(Misc.TargetSize, Misc.TargetSize));
			}
		} catch (Exception e) {
			Misc.showDialog(e);
		}
	}
	
	public JsonObject getStats(String name) {
		for(int i = 0; i < items.size(); i++) {
			JsonObject item =  items.get(i).getAsJsonObject();
			System.out.println(item);
			if(item.get("name").getAsString().equalsIgnoreCase(name)) {
				return item.get("stats").getAsJsonObject();
			}
		}
		return null;
	}
	
	public Image getImage(String name) {
		return itemImages.get(name);
	}
}
