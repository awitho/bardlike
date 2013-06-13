import org.newdawn.slick.Graphics;


public class InventoryTile {
	private int ix, iy;
	private int curX, curY;
	private boolean equipSlot = false;
	private ItemType equipType;
	private Item containedItem;
	private boolean selected = false;
	
	public InventoryTile(int ix, int iy) {
		this.ix = ix;
		this.iy = iy;
		if (ix == 0) { 
			equipSlot = true;
		} 
	}
	
	public void draw(Graphics g, int x, int y) {
		if (selected) {
			g.drawRect(x + 4, y + 4, 25, 25); // This might need to be offset to the left a bit
			curX = x;
			curY = y;
		}
		g.drawRect(x, y, 32, 32); // farts.
		if (containedItem != null) {
			g.drawImage(containedItem.getImage().getScaledCopy(32, 32), x, y);
		}
	}
	
	public boolean isEquipSlot() {
		return equipSlot;
	}
	
	public void setContainedItem(Item i) {
		containedItem = i;
	}
	
	public Item getContainedItem() {
		return containedItem;
	}
	
	public void removeContainedItem() {
		containedItem = null;
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
