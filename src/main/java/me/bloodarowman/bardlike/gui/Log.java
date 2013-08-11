package me.bloodarowman.bardlike.gui;

import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.bloodarowman.bardlike.Main;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
/**
 *
 * @author Alex
 */
public class Log implements Menu {
    public static UnicodeFont LOG_FONT;

    static {
        try {
            Font fon = Font.createFont(Font.TRUETYPE_FONT, Log.class.getResourceAsStream("/gfx/fonts/OldLondon.ttf")); // This is so we can load the font when it's in a jar
            LOG_FONT = new UnicodeFont(fon, (int) Main.scale, false, false);
            LOG_FONT.getEffects().add(new ColorEffect(java.awt.Color.white));
            LOG_FONT.addAsciiGlyphs();
            LOG_FONT.loadGlyphs();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //public static TrueTypeFont LOG_FONT = new TrueTypeFont(new java.awt.Font("Arial", Font.PLAIN, (int) Main.scale), true);
	private boolean visible = false;
	// private double curTime = 0.0;
	private final int LOG_LENGTH = 8;
	// private Color white = new Color(0, 0, 0, 255);
	// private Color black = new Color(0, 0, 0, 255);
	private ArrayList<Line> lines = new ArrayList<Line>();
	private final DateFormat dateFormat = new SimpleDateFormat("h:mm:ss");
	
	public Log() {
		Main.luaThread.getLuaState().newTable();
		Main.luaThread.getLuaState().pushValue(-1);
		Main.luaThread.getLuaState().setGlobal("gLog");
		Main.luaThread.getLuaState().pushString("append");
		
		try {
			Main.luaThread.getLuaState().pushJavaFunction(new JavaFunction(Main.luaThread.getLuaState()) {
				public int execute() {
					if (L.getTop() > 1) {
						append(getParam(2).getString());
					}
					return 0;
				}
			});
		} catch (LuaException ex) {
			ex.printStackTrace();
		}

		Main.luaThread.getLuaState().setTable(-3);
	}

	@Override
	public void setVisible(boolean b) {
		this.visible = b;
	}

	@Override
	public boolean isOpen() {
		return visible;
	}
	
	public void append(String str) {
		lines.add(new Line("[" + dateFormat.format(Calendar.getInstance().getTime()) + "] " + str));
	//	curTime = System.currentTimeMillis();
	//	white = new Color(255, 255, 255, 255);
	//	black = new Color(0, 0, 0, 255);
	}
	
	public void clear() {
		lines.clear();
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
			lines.get(i).render(g, x + 2, y - (count * g.getFont().getHeight(lines.get(i).getStr())) - g.getFont().getHeight(lines.get(i).getStr()));
			count++;
		}
	}
	
	public void update() {
		int begin = lines.size() - LOG_LENGTH;
		if (begin < 0) {
			begin = 0;
		}

		for(int i = begin; i < lines.size(); i++) {
			lines.get(i).update();
		}
		//if (System.currentTimeMillis() - curTime >= 10000.0 &&
		//		white.getAlpha() > 0) {
		//	white = new Color(255, 255, 255, white.getAlpha() - 1);
		//	black = new Color(0, 0, 0, black.getAlpha() - 1);
		//}
	}
	
	private class Line {
		//private ArrayList<Char> str = new ArrayList<Char>();
		private String str = "";
		private Color white = new Color(255, 255, 255, 0);
		private Color black = new Color(0, 0, 0, 0);
		
		public Line(String str) {
			//for (int i = 0; i < str.length(); i++) {
			//	this.str.add(new Char(str.charAt(i)));
			//}
			this.str = str;
		}
		
		public String getStr() {
			//String eStr = "";
			//for (int i = 0; i < str.size(); i++) {
			//	eStr = eStr + str.get(i).getChar();
			//}
			//return eStr;
			return str;
		}
		
		public void render(Graphics g, int x, int y) {
			//int strLen = 0;
			//for (int i = 0; i < str.size(); i++) {
			//	str.get(i).render(g, strLen + x, y);
			//	strLen = strLen + g.getFont().getWidth(Character.toString(str.get(i).getChar()));
			//}
			g.setColor(black);
			g.drawString(str, x, y - 2);//TOP
			g.drawString(str, x + 2, y);//LEFT
			g.drawString(str, x - 2, y);//RIGHT
			g.drawString(str, x, y + 2);//DOWN
		
			g.setColor(white);
			g.drawString(str, x, y);
		}
		
		public void update() {
			//for (int i = 0; i < str.size(); i++) {
			//	str.get(i).update();
			//}
			if (white.getAlpha() >= 255 && black.getAlpha() >= 255) { return; }
			white.add(new Color(0, 0, 0, 10));
			black.add(new Color(0, 0, 0, 10));
		}
		/*
		private class Char {
			private char c;
			private Color col = new Color(255, 255, 255, 0);
			
			public Char(char c) {
				this.c = c;
			}
			
			public char getChar() {
				return c;
			}
			
			public void render(Graphics g, int x, int y) {
				g.setColor(col);
				g.drawString(Character.toString(c), x, y);
			}
			
			public void update() {
				//col = new Color((int) (Math.random() * 255) + 1, 0, 0, 255);
				col.add(new Color(0, 0, 0, (int) (Math.random() * 5) + 1));
			}
		}*/
	}
}
