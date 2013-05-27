/**
 * A class that makes the Items for the game.
 * 
 * @author Bobby Henley
 * @version 1
 */

import org.newdawn.slick.Image;

public class Item extends Entity {
	public Item(ItemDictionary itemDictionary, GameMap map, String name) {
		super(itemDictionary.getImage(name), map);
	}
}
