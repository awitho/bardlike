
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alex
 */
public class Log implements Menu {
	public static TrueTypeFont LOG_FONT = 
				new TrueTypeFont(new java.awt.Font("Arial", 1, 12), true);
	private boolean visible = false;
	private double curTime = 0.0;
	private final int LOG_LENGTH = 8;
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
		lines.add("[" + dateFormat.format(Calendar.getInstance().getTime())
					+ "] " + str);
		curTime = System.currentTimeMillis();
		white = new Color(255, 255, 255, 255);
		black = new Color(0, 0, 0, 255);
	}
	
	@Override
	public void draw(Graphics g) {
		draw(g, 0, 0);
	}
	
	public void draw(Graphics g, int x, int y) {
		int begin = lines.size() - LOG_LENGTH;
		if (begin < 0) {
			begin = 0;
		}
		
		g.setFont(LOG_FONT);
	
		int count = 0;
		for(int i = begin; i < lines.size(); i++) {
			g.setColor(black);
			g.drawString(lines.get(i), x, y - 
					(count*g.getFont().getHeight(lines.get(i)))
					-g.getFont().getHeight(lines.get(i)) - 2);//TOP
			g.drawString(lines.get(i), x + 2, y - 
					(count*g.getFont().getHeight(lines.get(i)))
					-g.getFont().getHeight(lines.get(i)));//LEFT
			g.drawString(lines.get(i), x - 2, y - 
					(count*g.getFont().getHeight(lines.get(i)))
					-g.getFont().getHeight(lines.get(i)));//RIGHT
			g.drawString(lines.get(i), x, y - 
					(count*g.getFont().getHeight(lines.get(i)))
					-g.getFont().getHeight(lines.get(i)) + 2);//DOWN
			
			g.setColor(white);
			g.drawString(lines.get(i), x + 2, y -
				(count*g.getFont().getHeight(lines.get(i)))
					-g.getFont().getHeight(lines.get(i)));
			count++;
		}
	}
	
	public void update() {
		if (System.currentTimeMillis() - curTime >= 10000.0 &&
				white.getAlpha() > 0) {
			white = new Color(255, 255, 255, white.getAlpha() - 1);
			black = new Color(0, 0, 0, black.getAlpha() - 1);
		}
	}
}
