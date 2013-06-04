import java.util.HashMap;
import java.util.Map.Entry;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.google.gson.JsonElement;

/**
 * A class that handles the players inventory.
 * 
 * @author Bobby Henley
 * @version 1
 */
public class Inventory implements Menu {
	private int width = 550, height = 400, selectX, selectY = 80;
	private int INV_OFFSET_X = width/2 - 30, INV_OFFSET_Y = height/2 - 30;
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
			
			int countX = 0;
			int countY = 0;
			for(int x = 0; x < ply.getPlayerItems().size(); x++) {
				g.drawImage(itemDictionary.getScaledImageByName(32, ply.getPlayerItems().get(x).getName()), countX + ply.getX() - INV_OFFSET_X, (countY + 80) + ply.getY() - INV_OFFSET_Y); // This can't be called every frame with scaled copy, too resource intensive.
				countX += 32;
				if(countX >= 10*32) {
					countX = 0;
					countY+=32;
				}
			}
			g.drawRect(selectX + ply.getX() - INV_OFFSET_X + 4, selectY + ply.getY() - INV_OFFSET_Y + 4, 25, 25);

			// "" + ply.getStat(
			/*for (Entry<String, JsonElement> ele : invMenu.getObject().entrySet()) {
				g.drawString(ele.getValue().getAsString().replace("%n", ele.getKey().split("#(.*)")[0]), 50, 50);
			}*/

			g.drawString(invMenu.getValueAsString("#title"), 320/2 - 40 + ply.getX() - INV_OFFSET_X, 10 + ply.getY() - INV_OFFSET_Y);
			g.drawString(invMenu.getValueAsString("#stat"), (width - 130) + ply.getX() - INV_OFFSET_X, 10 + ply.getY() - INV_OFFSET_Y);
			g.drawRect(ply.getX() - INV_OFFSET_X, ply.getY() - INV_OFFSET_Y, width, height);
		}
	}
	
	//started to make it so that the inventory has controls, using WASD for now.
	public void getControls(GameContainer container) {
		if(!visible) {
			if(container.getInput().isKeyPressed(Input.KEY_I)) {
				this.setVisible(true);
				ply.isFrozen(true);
			}
		}else {
			if(container.getInput().isKeyPressed(Input.KEY_I)) {
				this.setVisible(false);
				ply.isFrozen(false);
			}else if(container.getInput().isKeyPressed(Input.KEY_W)) {
				if(selectY < 80 + 32) { selectY = 80 + 32; }
				selectY-=32;
			}else if(container.getInput().isKeyPressed(Input.KEY_S)) {
				if(selectY > (8*32) + 80) { selectY = (8*32) + 80; }
				selectY+=32;
			}else if(container.getInput().isKeyPressed(Input.KEY_A)) {
				if(selectX < 32) { selectX = 32; }
				selectX-=32;
			}else if(container.getInput().isKeyPressed(Input.KEY_D)) {
				if(selectX > (8*32)) { selectX = (8*32); }
				selectX+=32;
			}
		}
	}
}
