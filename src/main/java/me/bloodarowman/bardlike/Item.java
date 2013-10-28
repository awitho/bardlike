package me.bloodarowman.bardlike;

import com.google.gson.JsonElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

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
    private boolean named = false;
    private Color color;
    private String desc;
    private ItemType type;
	private WeaponType weaponType;
	private HashMap<String, Integer> stats = new HashMap<String, Integer>();

	public Item(String id) {
		super(ItemDictionary.getImage(id));
		this.id = id;
        this.name = ItemDictionary.getName(id);
        this.desc = ItemDictionary.getDesc(id);
		this.type = ItemDictionary.getType(id);
		this.weaponType = ItemDictionary.getWeaponType(id);

		if ((int) (Math.random() * 100) <=45) {
			if (weaponType != null) {
                this.named = true;
				this.name = NameGenerator.generateName(weaponType.toString());
			} else {
                this.named = true;
				this.name = NameGenerator.generateName(type.toString());
			}
		}
        int stTotal = 0;
		for (Map.Entry<String, JsonElement> ele : ItemDictionary.getStats(id).entrySet()) {
			stats.put(ele.getKey(), ele.getValue().getAsInt());
            stTotal += ele.getValue().getAsInt();
		}
        color = Misc.statsToQLColor(stTotal);
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

    public String getDesc() {
        if (desc.trim().equalsIgnoreCase("")) {
            return "A description would be here, but we don't like you.";
        }
        return desc;
    }

	public HashMap<String, Integer> getStats() {
		return stats;
	}

	public boolean isNamed() {
		return named;
    }

    public Color getQLColor() {
        return color;
    }

	public ItemType getType() {
		return type;
	}

    @Override
    public void update(GameContainer container, StateBasedGame s, int delta) {return;}

    @Override
	public String toString() {
		return "(Item | name: " + getName() + ", stats: " + stats + ", image: "
				+ getImage() + ", type: " + type + ")";
	}
}
