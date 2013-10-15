package me.bloodarowman.bardlike;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * A single tile for multiple entities to occupy at a single time.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class Tile {
	private int ix, iy, w, h; // Width and height are stored as they were taking up a lot of time in the callstack.
	private String name;
	private boolean inLos, playerSaw, isWall, isEmpty;
	private ArrayList<Entity> containedEnts = new ArrayList<Entity>();
	private Image spr, overlay;

	private static final Color overlayColor = new Color(0, 0, 0, 200);

	public Tile(String tile, int x, int y) {
		if (tile == null) { return; }
		name = tile;
		ix = x;
		iy = y;
		overlay = TileDictionary.getTileImageByName("Overlay");
		isWall = TileDictionary.getTileIsWall(tile); // Ditto.
		spr = TileDictionary.getTileImageByName(tile); // Grab a reference for the tile image from the tile dictionary, no need to make our own copy!
        w = spr.getWidth();
        h = spr.getHeight();
		isEmpty = !tile.equalsIgnoreCase("Empty");
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public Tile setSeen(boolean bool) {
		playerSaw = bool;
		return this;
	}

	public boolean hasSeen() {
		return playerSaw;
	}

	public Tile setInLos(boolean vis) {
		inLos = vis;
		return this;
	}

	public boolean getInLos() {
		return inLos;
	}
	
	public boolean isWall() {
		return isWall;
	}

	public void addEnt(Entity ent) {
		if (getName().equalsIgnoreCase("Empty"))  { return; }
		containedEnts.add(ent);
	}

	public void removeEnt(Entity ent) {
		containedEnts.remove(ent);
	}

	public int getTileX() {
		return ix;
	}

	public int getTileY() {
		return iy;
	}

	public String getName() {
		return name;
	}
	
	public boolean isReal() {
		return isEmpty;
	}

	public boolean containsEnt(Entity ent) {
		for (int i = 0; i < containedEnts.size(); i++) {
			if (containedEnts.get(i).equals(ent)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Entity> findType(Class<?> cls) {
		ArrayList<Entity> foundEnts = new ArrayList<Entity>();
		for (int i = 0; i < containedEnts.size(); i++) {
			if (cls.isAssignableFrom(containedEnts.get(i).getClass())) {
				foundEnts.add(containedEnts.get(i));
			}
		}
		if(foundEnts.size() <= 0) {return null;}
		return foundEnts;
	}

	public void draw(Graphics g, int x, int y) {
		g.drawImage(spr, x, y);
		if (playerSaw && !inLos) {
            Color old = g.getColor();
            g.setColor(overlayColor);
			g.drawImage(overlay, x, y);
            g.setColor(old);
		}
	}

	public void drawEnts(Graphics g, int x, int y) {
		if (containedEnts != null) {
			for (int i = 0; i < containedEnts.size(); i++) {
				containedEnts.get(i).draw(g, x, y);
			}
		}
	}

	public void update(MainGameState mgs) {
		for (int i = 0; i < containedEnts.size(); i++) {
			containedEnts.get(i).update(mgs);
		}
	}
	
	public void updateAttacks() {
		for (int i = 0; i < containedEnts.size(); i++) {
			containedEnts.get(i).updateAttacks();
		}
	}
	
	public String toString() {
		return "(Tile | name: " + name + ", x: " + ix + ", y: " + iy + ", containedEnts: " + containedEnts +")";
	}
}
