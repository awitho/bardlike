import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	private HashMap<Integer, HashMap<String, Image>> scaledImages;
	private JsonArray items;
	private JsonObject curItem;
	
	public ItemDictionary() {
		itemImages = new HashMap<>();
		scaledImages = new HashMap<>();
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
	
	/*public void scaleImages(int i) {
		for(Entry<String, Image> ele : itemImages.entrySet()) {
			itemImages.get(ele.getKey()).getScaledCopy(i, i);
			System.out.println("Scaling");	
		}
		scaledImages.put(i, itemImages);
	}
	
	public HashMap<String, Image> getScaledImages(int sized) {
		return scaledImages.get(sized);
	}*/
	
	public Image getScaledImageByName(int sized, String name) {
		//return scaledImages.get(sized).get(name).getScaledCopy(32, 322);
		return itemImages.get(name).getScaledCopy(sized, sized);
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
