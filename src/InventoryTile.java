import org.newdawn.slick.Graphics;


public class InventoryTile {
	private int ix, iy;
	private boolean equipSlot = false;
	private ItemType equipType;
	private Item containedItem;
	private boolean selected = false;
	
	public InventoryTile(int ix, int iy) {
		this.ix = ix;
		this.iy = iy;
		if (ix == 1) { 
			equipSlot = true;
			
		}
	}
	
	public void draw(Graphics g, int x, int y) {
		if (selected) {
			g.drawRect(x, y, 25, 25); // This might need to be offset to the left a bit
		}
		g.drawRect(x, y, 32, 32); // farts.
		if (containedItem != null) {
			g.drawImage(containedItem.getImage(), 0, 0);
		}
	}
	
	public void toggleSelect() {
		selected = !selected;
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
		}
		return null;
	}
}
