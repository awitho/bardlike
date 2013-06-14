
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alex
 */
public class HUD implements Menu {
	private boolean visible = false;
	private Player player;
	
	public HUD(Player ply) {
		this.player = ply;
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
	
	public void draw(Graphics g, int x, int y) {
		g.setFont(Log.LOG_FONT);
		String stuff = "HP: " + player.getHP() + " | LVL: " + player.getLevel() + " | XP: " + player.getXP() + " | DLVL: " + TileDictionary.getDungeonLevel();
		
		g.setColor(Color.black);
		g.drawString(stuff, x - (g.getFont().getWidth(stuff)) - 2, y - g.getFont().getHeight(stuff));
		g.drawString(stuff, x - (g.getFont().getWidth(stuff)) + 2, y - g.getFont().getHeight(stuff));
		g.drawString(stuff, x - (g.getFont().getWidth(stuff)), y - g.getFont().getHeight(stuff) - 2);
		g.drawString(stuff, x - (g.getFont().getWidth(stuff)), y - g.getFont().getHeight(stuff) + 2);
		
		
		g.setColor(Color.white);
		g.drawString(stuff, x - (g.getFont().getWidth(stuff)), y - g.getFont().getHeight(stuff));
	}
}
