import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

/**
 * A class that handles the players inventory.
 * 
 * @author Bobby Henley
 * @version 1
 */
public class Inventory implements Menu {
	private int width = 550, height = 400, reticleX, reticleY = 80, equipLoc = 0;;
	private int INV_OFFSET_X = width/2 - 85, INV_OFFSET_Y = height/2 - 30;
	private int INV_WIDTH = 320;
	private int EQUIP_OFFSET = 96;
	private long curTime = 0;
	
	private boolean visible;
	private Player ply;
	private GameConfig invMenu;
	private InventoryTile[][] inventoryTiles;
	
	private InventoryTile newInvTile;
	private InventoryTile curInvTile;

	public Inventory(Player p) {
		ply = p;
		invMenu = new GameConfig("./loc/inventorytext.json");
		ItemDictionary.scaleImages(32); // We need thumbnails at 32x32 here, so tell ItemDictionary to prepare them for us!
		inventoryTiles = new InventoryTile[11][10];
		for(int i = 0; i < 11; i++) {
			for(int j = 0; j < 10; j++) {
				inventoryTiles[i][j] = new InventoryTile(i, j);
			}
		}
		inventoryTiles[0][0].toggleSelect();
		curInvTile = inventoryTiles[0][0];
		newInvTile = null;
	}

	@Override
	public void setVisible(boolean b) {
		visible = b;
	}
	
	@Override
	public boolean isOpen() {
		return visible;
	}
	
	@Override
	public void draw(Graphics g) {
		draw(g, 0, 0);
	}
	
	public void draw(Graphics g, int screenX, int screenY) {
		if(visible) {
			drawBase(g, screenX, screenY);
			drawItems(g);
			for(int x = 0; x < inventoryTiles.length; x++) {
				for(int y = 0; y < inventoryTiles[0].length; y++) {
					InventoryTile tile = inventoryTiles[x][y];
					if(tile != null) {
						if(tile.isEquipSlot()) {
							tile.draw(g, ((x * 32) - 64) + screenX + EQUIP_OFFSET, ((y * 32) + 80) + screenY);
						}else {
							tile.draw(g, (x * 32) + screenX + EQUIP_OFFSET, ((y * 32) + 80) + screenY);
						} 
					}
				}
			}
			// "" + ply.getStat(
			/*for (Entry<String, JsonElement> ele : invMenu.getObject().entrySet()) {
				g.drawString(ele.getValue().getAsString().replace("%n", ele.getKey().split("#(.*)")[0]), 50, 50);
			}*/
		}
	}
	
	private void drawBase(Graphics g, int screenX, int screenY) {
		g.setColor(Color.black);
		g.fillRect(screenX + EQUIP_OFFSET, screenY, EQUIP_OFFSET, height);
		g.fillRect(screenX, screenY, width, height);
		
		g.setColor(Color.white);
		
		g.drawString(invMenu.getValueAsString("#title"), INV_WIDTH/2 - 40 + screenX, 10 + screenY);
		g.drawString(invMenu.getValueAsString("#stat"), (width - 130) + screenX, 10 + screenY);
		if(curInvTile.getContainedItem() != null) {
			g.drawString(curInvTile.getContainedItem().getName(), screenX + 30, screenY + 50);
		}
		g.drawString("Equip", screenX + 30, 10 + screenY);
		g.drawRect(screenX - EQUIP_OFFSET, screenY, width  + EQUIP_OFFSET, height);
	}
	
	private void drawItems(Graphics g) {
		int count = 0;
		for(int x = 1; x < Misc.MAX_INVENTORY / 10; x++) {	
			for(int y = 0; y < Misc.MAX_INVENTORY / 10; y++) {
				try{
					inventoryTiles[x][y].setContainedItem(ply.getPlayerItems().get(count));
					if(inventoryTiles[x][y].containsItem()) {
						System.out.println(inventoryTiles[x][y].containsItem() + ", " +inventoryTiles[x][y].getX()+", " +inventoryTiles[x][y].getY());
					}
				} catch (IndexOutOfBoundsException ex) {
					break;
				}
				count++;
			}
		
			for(int i = 0; i < ply.getEquippedItems().size(); i++) {
				if(ply.getEquippedItems().get(i) == null) { continue; }
				System.out.println(ply.getEquipLoc());
				g.drawImage(ply.getEquippedItems().get(i).getImage().getScaledCopy(32, 32), ply.getX() - INV_OFFSET_X - EQUIP_OFFSET + 32, (ply.getEquipLoc()+80) + ply.getY() - INV_OFFSET_Y);
			}
		}
		
		/*g.draw(selectionReticle);
		selectionReticle.setX(reticleX + ply.getX() - INV_OFFSET_X + 4);
		selectionReticle.setY(reticleY + ply.getY() - INV_OFFSET_Y + 4);
		*/
	}	
	
	public void update(GameContainer container) {
		if (!visible) {
			if (container.getInput().isKeyPressed(Input.KEY_I)) {
				this.setVisible(true);
				ply.isFrozen(true);
			}
		} else {
			if (container.getTime() - curTime <= 150) { return; }
			if (container.getInput().isKeyPressed(Input.KEY_I)) {
				this.setVisible(false);
				ply.isFrozen(false);
			} else if(container.getInput().isKeyPressed(Input.KEY_D)) {
				ply.removeItem(curInvTile.getContainedItem());
				curInvTile.setContainedItem(null);
				
			} else if(container.getInput().isKeyPressed(Input.KEY_E)){
				System.out.println("Equipping");
				//ply.removeItem(selected);
				//ply.equipItem(selected);
			}
			try {
				if (container.getInput().isKeyDown(Input.KEY_UP)) {
					newInvTile = inventoryTiles[curInvTile.getX()][curInvTile.getY() - 1];
				} else if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
					newInvTile = inventoryTiles[curInvTile.getX()][curInvTile.getY() + 1];
				} else if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
					newInvTile = inventoryTiles[curInvTile.getX() - 1][curInvTile.getY()];
				} else if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
					newInvTile = inventoryTiles[curInvTile.getX() + 1][curInvTile.getY()];
				}
			} catch(IndexOutOfBoundsException e) {
				newInvTile = curInvTile;
				return;
			}
			if(newInvTile != null && newInvTile != curInvTile) {
				curTime = container.getTime();
				curInvTile.toggleSelect();
				newInvTile.toggleSelect();
				curInvTile = newInvTile;
			}
			if(newInvTile != null) {
				System.out.println(newInvTile.getX()+", "+newInvTile.getY());
			}
		}
	}
}