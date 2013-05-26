import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;


public class Item extends Entity {
	public Item(ItemDictionary itemDictionary, Image img, GameMap map, String name) {
		super(itemDictionary.getSprites().getSubImage
				(itemDictionary.getItem().get("sx").getAsInt(), 
						itemDictionary.getItem().get("sy").getAsInt()), map);
	}

}
