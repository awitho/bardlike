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
	private int width = 550, height = 400, reticleX, reticleY = 80;
	private int INV_OFFSET_X = width/2 - 30, INV_OFFSET_Y = height/2 - 30;
	private int lastDelta;
	private Item selected;
	private long curTime = 0;
	private Rectangle selectionReticle;
	private boolean visible;
	private HashMap<String, Integer> playerStats;
	private Player ply;
	private GameConfig invMenu;

	public Inventory(Player p) {
		ply = p;
		playerStats = p.getStats();
		invMenu = new GameConfig("./loc/inventorytext.json");
		selectionReticle = new Rectangle(reticleX, reticleY, 25, 25);
		ItemDictionary.scaleImages(32); // We need thumbnails at 32x32 here, so tell ItemDictionary to prepare them for us!
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
			g.setColor(Color.black);
			g.fillRect(ply.getX() - INV_OFFSET_X, ply.getY() - INV_OFFSET_Y, width, height);
			for(int i = 0; i < 320; i+=32) {
				for(int j = 0; j < height - 80; j+=32) {
					g.setColor(Color.white);
					g.drawRect(i + ply.getX() - INV_OFFSET_X, (j + 80) + ply.getY() - INV_OFFSET_Y, 32, 32);
				}
			}
			
			//puts items on top of each other.
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
						selected = ply.getPlayerItems().get(count);
						g.drawString(ply.getPlayerItems().get(count).getName(), 20 + ply.getX() - INV_OFFSET_X + 10, 50 + ply.getY() - INV_OFFSET_Y);
					}
					count++;
				}
			}
			
			g.draw(selectionReticle);
			selectionReticle.setX(reticleX + ply.getX() - INV_OFFSET_X + 4);
			selectionReticle.setY(reticleY + ply.getY() - INV_OFFSET_Y + 4);

			// "" + ply.getStat(
			/*for (Entry<String, JsonElement> ele : invMenu.getObject().entrySet()) {
				g.drawString(ele.getValue().getAsString().replace("%n", ele.getKey().split("#(.*)")[0]), 50, 50);
			}*/

			g.drawString(invMenu.getValueAsString("#title"), 320/2 - 40 + ply.getX() - INV_OFFSET_X, 10 + ply.getY() - INV_OFFSET_Y);
			g.drawString(invMenu.getValueAsString("#stat"), (width - 130) + ply.getX() - INV_OFFSET_X, 10 + ply.getY() - INV_OFFSET_Y);
			g.drawRect(ply.getX() - INV_OFFSET_X, ply.getY() - INV_OFFSET_Y, width, height);
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
			}else if(container.getInput().isKeyPressed(Input.KEY_D)) {
				ply.removeItem(selected);
			
			}else if (container.getInput().isKeyDown(Input.KEY_UP)) {
				if(reticleY < 80 + 32) { reticleY = 80 + 32; }
				reticleY-=32;
				curTime = container.getTime();
			} else if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
				if(reticleY > (8*32) + 80) { reticleY = (8*32) + 80; }
				reticleY+=32;
				curTime = container.getTime();
			} else if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
				if(reticleX < 32) { reticleX = 32; }
				reticleX-=32;
				curTime = container.getTime();
			} else if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
				if(reticleX > (8*32)) { reticleX = (8*32); }
				reticleX+=32;
				curTime = container.getTime();
			}
		}
	}
}