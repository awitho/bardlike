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
	private int INV_ITEMBOX_WIDTH = 320;
	private int EQUIP_OFFSET = 96;
	private String equipNames;
	private int lastDelta;
	private Item selected;
	private long curTime = 0;
	private Rectangle selectionReticle;
	private boolean visible;
	private HashMap<String, Integer> playerStats;
	private Player ply;
	private GameConfig invMenu;
	private ItemDictionary itemDictionary;

	public Inventory(Player p, ItemDictionary id) {
		ply = p;
		playerStats = p.getStats();
		invMenu = new GameConfig("./loc/inventorytext.json");
		itemDictionary = id;
		equipNames = "Head\nTorso\nWeapon\nCloak\nHands\nLegs\nFeet\nWaist\nPauld";
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
		for(int i = 0; i < INV_ITEMBOX_WIDTH; i+=32) {
			for(int j = 0; j < height - 80; j+=32) {
				g.setColor(Color.white);
				g.drawRect(i + ply.getX() - INV_OFFSET_X, (j + 80) + ply.getY() - INV_OFFSET_Y, 32, 32);
			}
		}	
		
		for(int y = 0; y < (9*32); y+=32) {
			g.drawRect(ply.getX() - INV_OFFSET_X - EQUIP_OFFSET + 32, (y + 80) + ply.getY() - INV_OFFSET_Y, 32, 32);
		}
		
		g.drawString(invMenu.getValueAsString("#title"), INV_ITEMBOX_WIDTH/2 - 40 + ply.getX() - INV_OFFSET_X, 10 + ply.getY() - INV_OFFSET_Y);
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
				g.drawImage(itemDictionary.getScaledImageByName(32, ply.getPlayerItems().get(count).getID()), (y*32) + ply.getX() - INV_OFFSET_X, ((x*32) + 80) + ply.getY() - INV_OFFSET_Y);
				if(reticleX == (y*32) && (reticleY-80) == (x*32)) {		
					selected = ply.getPlayerItems().get(count);
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
		
		g.draw(selectionReticle);
		selectionReticle.setX(reticleX + ply.getX() - INV_OFFSET_X + 4);
		selectionReticle.setY(reticleY + ply.getY() - INV_OFFSET_Y + 4);
	}	
	
	public void update(GameContainer container) {
		if (!visible) {
			if (container.getInput().isKeyPressed(Input.KEY_I)) {
				this.setVisible(true);
				ply.isFrozen(true);
			}
		} else {
			System.out.println("reticle: (" +reticleX+", "+reticleY+")");
			if (container.getTime() - curTime <= 150) { return; }
			if (container.getInput().isKeyPressed(Input.KEY_I)) {
				this.setVisible(false);
				ply.isFrozen(false);
			}else if(container.getInput().isKeyPressed(Input.KEY_D)) {
				ply.removeItem(selected);
			}else if(container.getInput().isKeyPressed(Input.KEY_E)){
				System.out.println("Equipping");
				ply.removeItem(selected);
				ply.equipItem(selected);
				selected = null;
			}else if (container.getInput().isKeyDown(Input.KEY_UP)) {
				if(reticleY < 80 + 32) { reticleY = 80 + 32; }
				reticleY-=32;
				curTime = container.getTime();
			} else if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
				if(reticleY > (8*32) + 80) { reticleY = (8*32) + 80; }
				if((reticleX < 0) && (reticleY > (7*32) + 80)) { reticleY = (7*32) + 80; }
				reticleY+=32;
				curTime = container.getTime();
			} else if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
				if(reticleX < 32) { reticleX = -32; }
				if(reticleX == -32 && reticleY == (9*32) + 80) {
					reticleX = -32;
					reticleY = (8*32) + 80;
				}
				reticleX-=32;
				curTime = container.getTime();
			} else if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
				if(reticleX > (8*32)) { reticleX = (8*32); }
				if(reticleX < 0) { reticleX = -32; }
				reticleX+=32;
				curTime = container.getTime();
			}
		}
	}
}