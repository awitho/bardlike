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
	private int lastDelta;
	private int curSelectedX, curSelectedY;
	private long curTime = 0;
	private boolean visible;
	private HashMap<String, Integer> playerStats;
	private Player ply;
	private GameConfig invMenu;
	private InventoryTile[][] inventoryTiles;
	private InventoryTile newInvTile;
	private InventoryTile curInvTile;

	public Inventory(Player p) {
		ply = p;
		playerStats = p.getStats();
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
		if(visible) {
			drawBase(g);
			drawItems(g);
			for(int x = 0; x < inventoryTiles.length; x++) {
				for(int y = 0; y < inventoryTiles[0].length; y++) {
					InventoryTile tile = inventoryTiles[x][y];
					if(tile != null) {
						if(x == 0) {
							tile.draw(g, ((x * 32) - 64) + ply.getX() - INV_OFFSET_X, ((y * 32) + 80) + ply.getY() - INV_OFFSET_Y);
						}else {
							tile.draw(g, (x * 32) + (ply.getX() - INV_OFFSET_X), ((y * 32) + 80) + ply.getY() - INV_OFFSET_Y);
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
	
	private void drawBase(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(ply.getX() - INV_OFFSET_X - EQUIP_OFFSET, ply.getY() - INV_OFFSET_Y, EQUIP_OFFSET, height);
		g.fillRect(ply.getX() - INV_OFFSET_X, ply.getY() - INV_OFFSET_Y, width, height);
		
		g.setColor(Color.white);
		
		g.drawString(invMenu.getValueAsString("#title"), INV_WIDTH/2 - 40 + ply.getX() - INV_OFFSET_X, 10 + ply.getY() - INV_OFFSET_Y);
		g.drawString(invMenu.getValueAsString("#stat"), (width - 130) + ply.getX() - INV_OFFSET_X, 10 + ply.getY() - INV_OFFSET_Y);
		g.drawString("Equip", ply.getX() - INV_OFFSET_X - EQUIP_OFFSET + 30, 10 + ply.getY() - INV_OFFSET_Y);
		g.drawRect(ply.getX() - INV_OFFSET_X, ply.getY() - INV_OFFSET_Y, width, height);
		g.drawRect(ply.getX() - INV_OFFSET_X - EQUIP_OFFSET, ply.getY() - INV_OFFSET_Y, EQUIP_OFFSET, height);
	}
	
	private void drawItems(Graphics g) {
		int count = 0;
		for(int x = 0; x < Misc.MAX_INVENTORY / 10; x++) {	
			for(int y = 0; y < Misc.MAX_INVENTORY / 10; y++) {
				try{
					ply.getPlayerItems().get(count);
				} catch (IndexOutOfBoundsException ex) {
					break;
				}
				g.drawImage(ItemDictionary.getScaledImageByName(32, ply.getPlayerItems().get(count).getID()), (y*32) + ply.getX() - INV_OFFSET_X, ((x*32) + 80) + ply.getY() - INV_OFFSET_Y);
				if(reticleX == (y*32) && (reticleY-80) == (x*32)) {		
					//selected = ply.getPlayerItems().get(count);
					g.drawString(ply.getPlayerItems().get(count).getName(), 20 + ply.getX() - INV_OFFSET_X + 10, 50 + ply.getY() - INV_OFFSET_Y);
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
			try {
				if (container.getTime() - curTime <= 150) { return; }
				if (container.getInput().isKeyPressed(Input.KEY_I)) {
					this.setVisible(false);
					ply.isFrozen(false);
				}else if(container.getInput().isKeyPressed(Input.KEY_D)) {
					//ply.removeItem(selected);
				}else if(container.getInput().isKeyPressed(Input.KEY_E)){
					System.out.println("Equipping");
					//ply.removeItem(selected);
					//ply.equipItem(selected);
				}else if (container.getInput().isKeyDown(Input.KEY_UP)) {
					newInvTile = inventoryTiles[curSelectedX][curSelectedY -= 1];
					curTime = container.getTime();
				} else if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
					newInvTile = inventoryTiles[curSelectedX][curSelectedY += 1];
					curTime = container.getTime();
				} else if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
					newInvTile = inventoryTiles[curSelectedX -= 1][curSelectedY];
					curTime = container.getTime();
				} else if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
					newInvTile = inventoryTiles[curSelectedX += 1][curSelectedY];
					curTime = container.getTime();
				}
				if(newInvTile != null) {
					curInvTile.toggleSelect();
					newInvTile.toggleSelect();
					curInvTile = newInvTile;
				}
			}catch(IndexOutOfBoundsException e) {
				if(curSelectedX < 0) {
					curSelectedX = 0;
				}else if(curSelectedX > 10) {
					curSelectedX = 10;
				}else if(curSelectedY < 0) {
					curSelectedY = 0;
				}else if(curSelectedY > 10) {
					curSelectedY = 10;
				}
			}
		}
	}
}