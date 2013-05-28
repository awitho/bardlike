/**
 * A class that handles the players inventory.
 * 
 * @author Bobby Henley
 * @version 1
 */
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import com.google.gson.JsonArray;

public class Inventory implements Menu {
	private int INV_OFFSET = 200;
	private boolean visible;
	private HashMap<String, Integer> playerStats;
	private Player ply;
	
	public Inventory(Player p) {
		ply = p;
		playerStats = p.getStats();
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
		int width = 550, height = 400;
		if(visible) {
			g.setColor(Color.black);
			g.fillRect(ply.getX() - INV_OFFSET, ply.getY() - INV_OFFSET, width, height);
			for(int i = 0; i < 320; i+=32) {
				for(int j = 0; j < height - 80; j+=32) {
					g.setColor(Color.white);
					g.drawRect(i + ply.getX() - INV_OFFSET, (j + 80) + ply.getY() - INV_OFFSET, 32, 32);
				}
			}
			g.drawString("Inventory", 320/2 - 40 + ply.getX() - INV_OFFSET, 10 + ply.getY() - INV_OFFSET);
			g.drawString("Stats", (width - 130) + ply.getX() - INV_OFFSET, 10 + ply.getY() - INV_OFFSET);
			g.drawRect(ply.getX() - INV_OFFSET, ply.getY() - INV_OFFSET, width, height);
		}
	}
}
