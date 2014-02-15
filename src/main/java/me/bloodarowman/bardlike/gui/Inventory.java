package me.bloodarowman.bardlike.gui;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import me.bloodarowman.bardlike.GameConfig;
import me.bloodarowman.bardlike.ItemDictionary;
import me.bloodarowman.bardlike.ItemType;
import me.bloodarowman.bardlike.Misc;
import me.bloodarowman.bardlike.Player;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A class that handles the players inventory.
 * 
 * @author Bobby Henley
 * @version 1
 */
public class Inventory implements Menu {
	private int width = 650, height = 400, reticleX, reticleY = 80;
	private int INV_OFFSET_X = width/2 - 85, INV_OFFSET_Y = height/2 - 30;
	private int INV_WIDTH = 320;
	private int EQUIP_OFFSET = 96;
	private int lastItemX, lastItemY;
	private long curTime = 0;
	
	private boolean visible;
	private Player ply;
	private GameConfig invMenu;
	private InventoryTile[][] inventoryTiles;
	
	private InventoryTile newInvTile;
	private InventoryTile curInvTile;

	public Inventory(Player p) throws MalformedURLException, URISyntaxException {
		ply = p;
		invMenu = new GameConfig("loc/inv_en.json");
		ItemDictionary.scaleImages(32); // We need thumbnails at 32x32 here,
						//so tell ItemDictionary to prepare them for us!
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

	public void draw(GameContainer container, StateBasedGame s, Graphics g) {
		if(visible) {
			g.pushTransform();
			g.translate(container.getWidth()/2 - width/2, container.getHeight()/2 - height/2);
			drawBase(g);
			drawItems();
			for(int x = 0; x < inventoryTiles.length; x++) {
				for(int y = 0; y < inventoryTiles[0].length; y++) {
					InventoryTile tile = inventoryTiles[x][y];
					if(tile != null) {
						if(tile.isEquipSlot()) {
							tile.draw(g, ((x * 32) - 64) + EQUIP_OFFSET, ((y * 32) + 80)); // Offset Equip slots to left.
						} else {
							tile.draw(g, (x * 32) + EQUIP_OFFSET, ((y * 32) + 80));
						}
					}
				}
			}
            curInvTile.drawTooltip(g);
			g.popTransform();
		}
	}
	
	private void drawBase(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(EQUIP_OFFSET, 0, EQUIP_OFFSET, height);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.white);
		g.drawString(invMenu.getValueAsString("#title"), INV_WIDTH/2 + EQUIP_OFFSET, 10);
		g.drawString(invMenu.getValueAsString("#stat"), (width - 130) + 0, 10);
		g.drawString(invMenu.getValueAsString("#str").replace("%n", ply.getStat("str") +""), (width - 170), 80);
		g.drawString(invMenu.getValueAsString("#int").replace("%n", ply.getStat("int") +""), (width - 170), 130);
		g.drawString(invMenu.getValueAsString("#dex").replace("%n", ply.getStat("dex") +""), (width - 170), 180);
		g.drawString(invMenu.getValueAsString("#end").replace("%n", ply.getStat("end") +""), (width - 170), 230);
		g.drawString(invMenu.getValueAsString("#agi").replace("%n", ply.getStat("agi") +""), (width - 170), 280);
		g.drawString("Equip", 30, 10);
		g.drawRect(0, 0, width, height);
	}

	private void drawItems() {
		int count = 0;
		for(int x = 1; x < Misc.MAX_INVENTORY / 10; x++) {	
			for(int y = 0; y < Misc.MAX_INVENTORY / 10; y++) {
				try{
					inventoryTiles[x][y].setContainedItem(ply.getPlayerItems().get(count));
					if(inventoryTiles[x][y].containsItem()) {
						lastItemX = x;
						lastItemY = y;
					}
				} catch (IndexOutOfBoundsException ex) {
					break;
				}
				count++;
			}
		}
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
			}
            if (container.getInput().isKeyPressed(Input.KEY_D)) {
                ply.removeItem(curInvTile.getContainedItem());
                inventoryTiles[lastItemX][lastItemY].setContainedItem(null);
                //curInvTile.setContainedItem(null);
            } else if (container.getInput().isKeyPressed(Input.KEY_E)) {
                if(curInvTile.getContainedItem() != null) {
                    InventoryTile equipTile;
                    for(int i = 0; i < inventoryTiles[0].length; i++) {
                        equipTile = inventoryTiles[0][i];
                        ItemType type = InventoryTile.indexToItemType(i);
                        if(type == null) { continue; }
                        if(curInvTile != null) {
                            if(curInvTile.getContainedItem().getType() == type) {
                                if(equipTile.getContainedItem() != null) {
                                    return;
                                }
                                ply.removeItem(curInvTile.getContainedItem());
                                ply.equipItem(curInvTile.getContainedItem());
                                equipTile.setContainedItem(curInvTile
                                        .getContainedItem());
                                inventoryTiles[lastItemX][lastItemY]
                                        .setContainedItem(null);
                                curInvTile.setContainedItem(null);
                                return;
                            }
                        }
                    }
                }
            } else if(container.getInput().isKeyPressed(Input.KEY_U)) {
                if(curInvTile != null) {
                    if(curInvTile.isEquipSlot()) {
                        ply.unequipItem(curInvTile.getContainedItem());
                        curInvTile.setContainedItem(null);
                    }
                }
            }
			try {
				if (container.getInput().isKeyDown(Input.KEY_UP)) {
					newInvTile = 
					  inventoryTiles[curInvTile.getX()][curInvTile.getY() - 1];
				} else if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
					newInvTile =
					  inventoryTiles[curInvTile.getX()][curInvTile.getY() + 1];
				} else if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
					newInvTile = 
					  inventoryTiles[curInvTile.getX() - 1][curInvTile.getY()];
				} else if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
					newInvTile = 
					  inventoryTiles[curInvTile.getX() + 1][curInvTile.getY()];
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
		}
	}
}