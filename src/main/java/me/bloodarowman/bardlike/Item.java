package me.bloodarowman.bardlike;

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
	private String id;
	private String name;
    private boolean named = false;
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
		for (Map.Entry<String, JsonElement> ele : ItemDictionary.getStats(id)
					.entrySet()) {
			stats.put(ele.getKey(), ele.getValue().getAsInt());
		}
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

	public ItemType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "(Item | name: " + getName() + ", stats: " + stats + ", image: "
				+ getImage() + ", type: " + type + ")";
	}
}
