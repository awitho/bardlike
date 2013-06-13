import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map.Entry;

/**
 * A class to handle items.
 * 
 * @author Bobby Henley
 * @version 1
 */
public class ItemDictionary {
	private static HashMap<String, Image> itemImages;
	private static HashMap<String, ItemType> itemTypes;
	private static HashMap<Integer, HashMap<String, Image>> scaledImages;
	private static JsonArray items;
	private static JsonObject curItem;
	
	public static void initItemDictionary() {
		itemImages = new HashMap<>();
		scaledImages = new HashMap<>();
		itemTypes = new HashMap<>();
		try {
			items = new GameConfig("items.json").getArray();
			SpriteSheet itemSprites = new SpriteSheet("./gfx/testitemsheet.png", 32, 32);
			for(int i = 0; i < items.size(); i++) {
				curItem = items.get(i).getAsJsonObject();
				itemTypes.put(curItem.get("name").getAsString(), ItemType.valueOf(curItem.get("type").getAsString()));
				itemImages.put(curItem.get("name").getAsString(), itemSprites.getSubImage(curItem.get("sx").getAsInt(), curItem.get("sy").getAsInt()).getScaledCopy(Misc.TARGET_SIZE, Misc.TARGET_SIZE));
			}
		} catch (Exception e) {
			Misc.showDialog(e);
		}
		NameGenerator.setNames(new GameConfig("names.json").getObject());
	}
	
	public static ItemType getType(String name) {
		return itemTypes.get(name);
	}
	
	public static void scaleImages(int i) {
		HashMap<String, Image> imgs = new HashMap<>();
		for(Entry<String, Image> ele : itemImages.entrySet()) {
			imgs.put(ele.getKey(), ele.getValue().getScaledCopy(i, i));
		}
		scaledImages.put(i, imgs);
	}
	
	public static HashMap<String, Image> getScaledImages(int sized) {
		return scaledImages.get(sized);
	}
	
	public static Image getScaledImageByName(int sized, String name) {
		return getScaledImages(sized).get(name);
	}
	
	public static JsonObject getStats(String name) {
		
		for(int i = 0; i < items.size(); i++) {
			JsonObject item =  items.get(i).getAsJsonObject();
			//System.out.println(item);
			if(item.get("name").getAsString().equalsIgnoreCase(name)) {
				return item.get("stats").getAsJsonObject();
			}
		}
		return null;
	}
	
	public static Image getImage(String name) {
		return itemImages.get(name);
	}
	
	public static int size() {
		return items.size();
	}
	
	public static Item getRandomItem() {
		return new Item(items.get((int) (Math.random() * items.size())).getAsJsonObject().get("name").getAsString());
		//for (int i = 0; i < items.size(); i++) {
		//	if (items.get(i) == null || (int) (Math.random() * items.size()) - 1 == 0) { continue; }
		//	return new Item(items.get(i).getAsJsonObject().get("name").getAsString());
		//}
		//return null;
	}
}
