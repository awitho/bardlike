/**
 * A class to handle items.
 * 
 * @author Bobby Henley
 * @version 0.1
 */

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class ItemDictionary {
	private SpriteSheet itemSprites;
	private JsonArray items;
	private JsonObject curItem;
	
	public ItemDictionary() {
		try {
			items = new GameConfig("items.json").getArray();
			itemSprites = new SpriteSheet("./gfx/testitemsheet", 32, 32);
			for(int i = 0; i < items.size(); i++) {
				curItem = items.get(i).getAsJsonObject();
				
			}
		} catch (Exception e) {
			Misc.showDialog(e);
		}
	}
	
	public JsonObject getItem() {
		return curItem;
	}
	
	public SpriteSheet getSprites() {
		return itemSprites;
	}
}
