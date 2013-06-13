
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alex
 */
public class Log implements Menu {
	private boolean visible = false;
	private double curTime = 0.0;
	private Color white = new Color(0, 0, 0, 255);
	private Color black = new Color(0, 0, 0, 255);
	private ArrayList<String> lines = new ArrayList<>();
	private final DateFormat dateFormat = new SimpleDateFormat("h:mm:ss");

	@Override
	public void setVisible(boolean b) {
		this.visible = b;
	}

	@Override
	public boolean isOpen() {
		return visible;
	}
	
	public void append(String str) {
		lines.add("[" + dateFormat.format(Calendar.getInstance().getTime()) + "] " + str);
		curTime = System.currentTimeMillis();
		white = new Color(255, 255, 255, 255);
		black = new Color(0, 0, 0, 255);
	}
	
	@Override
	public void draw(Graphics g) {
		draw(g, 0, 0);
	}
	
	public void draw(Graphics g, int x, int y) {
		int begin = lines.size() - 5;
		if (begin < 0) {
			begin = 0;
		}
		
		g.setFont(MainMenuState.font);
	
		int count = 0;
		for(int i = begin; i < lines.size(); i++) {
			g.setColor(black);
			g.drawString(lines.get(i), x + 4, y - (count*g.getFont().getHeight(lines.get(i)))-g.getFont().getHeight(lines.get(i)) + 2);
			g.setColor(white);
			g.drawString(lines.get(i), x + 2, y - (count*g.getFont().getHeight(lines.get(i)))-g.getFont().getHeight(lines.get(i)));
			count++;
		}
	}
	
	public void update() {
		if (System.currentTimeMillis() - curTime >= 2000.0 && white.getAlpha() > 0) {
			white = new Color(255, 255, 255, white.getAlpha() - 1);
			black = new Color(0, 0, 0, black.getAlpha() - 1);
		}
	}
}
