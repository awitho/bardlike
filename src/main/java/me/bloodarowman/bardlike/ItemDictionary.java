package me.bloodarowman.bardlike;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
	private static HashMap<String, Image> itemImages = new HashMap<String, Image>();
	private static HashMap<String, ItemType> itemTypes = new HashMap<String, ItemType>();
    private static HashMap<String, String> itemNames = new HashMap<String, String>();
    private static HashMap<String, String> itemDescs = new HashMap<String, String>();
	private static HashMap<String, WeaponType> weaponTypes = new HashMap<String, WeaponType>();
	private static HashMap<Integer, HashMap<String, Image>> scaledImages = new HashMap<Integer, HashMap<String, Image>>();
	private static JsonArray items;
	private static JsonObject curItem;
	
	public static void initItemDictionary() throws MalformedURLException, URISyntaxException {
        try {
			items = new GameConfig("items.json").getArray();

            GameConfig itemNamesJSON = new GameConfig("loc/items_en.json");
			SpriteSheet itemSprites = ImageLoader.loadSpritesheet("ents/items.png", 32, 32);
			for(int i = 0; i < items.size(); i++) {
				curItem = items.get(i).getAsJsonObject();

				itemTypes.put(curItem.get("id").getAsString(), ItemType.valueOf(curItem.get("type").getAsString()));

				if (curItem.get("sx").getAsInt() < 0 || curItem.get("sx").getAsInt() > itemSprites.getHorizontalCount() || curItem.get("sy").getAsInt() < 0 || curItem.get("sy").getAsInt() > itemSprites.getVerticalCount()) {
					Misc.logError(curItem.get("id").getAsString() + " was out of the spritesheet's bounds, using placeholder.");
					itemImages.put(curItem.get("id").getAsString(), Misc.miscImages.get("placeholder"));
				} else {
					itemImages.put(curItem.get("id").getAsString(), itemSprites.getSubImage(curItem.get("sx").getAsInt(), curItem.get("sy").getAsInt()).getScaledCopy(Misc.TARGET_SIZE, Misc.TARGET_SIZE));
				}

                itemNames.put(curItem.get("id").getAsString(), itemNamesJSON.getObject().get(curItem.get("id").getAsString()).getAsJsonObject().get("name").getAsString());
                itemDescs.put(curItem.get("id").getAsString(), itemNamesJSON.getObject().get(curItem.get("id").getAsString()).getAsJsonObject().get("desc").getAsString());

                String wepType;
				try {
					wepType = curItem.get("type2").getAsString();
				} catch (NullPointerException ex) { continue; }
				if(wepType != null) {
					weaponTypes.put(curItem.get("id").getAsString(),
							WeaponType.valueOf(wepType));
				}
			}
		} catch (Exception e) {
			Misc.showDialog(e);
		}
       // System.out.println(itemImages + "\n" + scaledImages + "\n" + itemTypes + "\n" + itemNames + "\n" + weaponTypes);
		NameGenerator.setNames(new GameConfig("names.json").getObject());
    }

    public static String getName(String id) {
        return itemNames.get(id);
    }

    public static String getDesc(String id) {
        return itemDescs.get(id);
    }
	
	public static ItemType getType(String name) {
		return itemTypes.get(name);
	}
	
	public static WeaponType getWeaponType(String name) {
		return weaponTypes.get(name);
	}
	
	public static void scaleImages(int i) {
		HashMap<String, Image> imgs = new HashMap<String, Image>();
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
			if(item.get("id").getAsString().equalsIgnoreCase(name)) {
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
		return new Item(items.get((int) (Math.random() * items.size())).getAsJsonObject().get("id").getAsString());
	}
}
