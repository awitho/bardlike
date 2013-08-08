package me.bloodarowman.bardlike.gui;

import me.bloodarowman.bardlike.Item;
import me.bloodarowman.bardlike.ItemType;
import me.bloodarowman.bardlike.Vector;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

import java.util.Map;


public class InventoryTile {
	private int ix, iy;
	private int curX, curY;
	private boolean equipSlot = false;
	private ItemType equipType;
    private String itemString = "";
	private Item containedItem;
	private boolean selected = false;
	private boolean containsItem = false;

    private final int TILE_SIZE = 32;
    private final int PADDING = 10;
    private final int HALF_TILE = TILE_SIZE / 2;
	
	public InventoryTile(int ix, int iy) {
		this.ix = ix;
		this.iy = iy;
		if (ix == 0) { 
			equipSlot = true;
		} 
	}
	
	public void draw(Graphics g, int x, int y) {
		if (selected) {
			g.drawRect(x + 4, y + 4, 25, 25); // This might need
								///to be offset to the left a bit
			curX = x;
			curY = y;
		}
		g.drawRect(x, y, TILE_SIZE, TILE_SIZE); // farts.
		if (containedItem != null) {
			g.drawImage(containedItem.getImage().getScaledCopy(32, 32), x, y);
		}
	}

    public void drawTooltip(Graphics g) {
        if (selected && containedItem != null) {
            Font f = g.getFont();
            Color c = g.getColor();
            g.setFont(Log.LOG_FONT);
            g.setColor(Color.black);
            g.fillRect(curX + TILE_SIZE / 2, curY + TILE_SIZE / 2, g.getFont().getWidth(itemString) + PADDING, g.getFont().getHeight(itemString) + PADDING);
            g.setColor(Color.white);
            g.drawRect(curX + TILE_SIZE / 2, curY + TILE_SIZE / 2, g.getFont().getWidth(itemString) + PADDING, g.getFont().getHeight(itemString) + PADDING);
            g.setColor(Color.white);
            g.drawString(itemString, curX + HALF_TILE + PADDING/2, curY + HALF_TILE);
            g.setFont(f);
            g.setColor(c);
        }
    }
	
	public boolean isEquipSlot() {
		return equipSlot;
	}
	
	public void setContainedItem(Item i) {
		containedItem = i;
        if (i.isNamed()) {
            itemString = "\"" + i.getName() + "\"\n";
        } else {
            itemString = i.getName() + "\n";
        }
        for (Map.Entry<String, Integer> ent : i.getStats().entrySet()) {
            itemString += "· " + ent.getKey() + ": " + ent.getValue() + "\n";
        }
		containsItem = true;
	}
	
	public Item getContainedItem() {
		return containedItem;
	}
	
	public boolean containsItem() {
		return containsItem;
	}
	
	public void toggleSelect() {
		selected = !selected;
	}
	
	public int getX() {
		return ix;
	}
	
	public int getY() {
		return iy;
	}
	
	public Vector getPos() {
		return new Vector(ix, iy);
	}
	
	public static ItemType indexToItemType(int iy) {
		switch(iy) {
			case 0:
				return ItemType.HEADGEAR;
			case 1:
				return ItemType.PAULDRONS;
			case 2:
				return ItemType.TORSO;
			case 3:
				return ItemType.WAIST;
			case 4:
				return ItemType.LEGS;
			case 5:
				return ItemType.CLOAK;
			case 6:
				return ItemType.GAUNTLETS;
			case 7:
				return ItemType.WEAPON;
			case 8:
				return ItemType.FEET;
			case 9:
				return ItemType.RING;
		}
		return null;
	}
}
