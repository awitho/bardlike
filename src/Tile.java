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
	private int ix, iy;
	private String name;
	private boolean inLos, playerSaw, isWall;
	private ArrayList<Entity> containedEnts = new ArrayList<>();;
	private Image spr, overlay;

	private static final Color overlayColor = new Color(0, 0, 0, 200);

	public Tile(TileDictionary tileDictionary, String tile, int x, int y) {
		if (tile == null) { return; }
		name = tile;
		ix = x;
		iy = y;
		overlay = tileDictionary.getTileImageByName("Overlay");
		isWall = tileDictionary.getTileIsWall(tile); // Ditto.
		spr = tileDictionary.getTileImageByName(tile); // Grab a reference for the tile image from the tile dictionary, no need to make our own copy!
	}

	public int getWidth() {
		return spr.getWidth();
	}

	public int getHeight() {
		return spr.getHeight();
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

	public void addEnt(Entity ent) {
		System.out.println("Attempting to add ent: " + ent + " to " + this);
		containedEnts.add(ent);
		System.out.println(containedEnts);
	}

	public void removeEnt(Entity ent) {
		System.out.println("Attempting to remove ent: " + ent + " from " + this);
		containedEnts.remove(ent);
		System.out.println(containedEnts);
	}

	public int getX() {
		return ix;
	}

	public int getY() {
		return iy;
	}

	public String getName() {
		return name;
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
			if (cls == containedEnts.get(i).getClass()) {
				foundEnts.add(containedEnts.get(i));
			}
		}
		if(foundEnts.size() <= 0) {return null;}
		return foundEnts;
	}

	public void draw(Graphics g, int x, int y) {
		g.drawImage(spr, x, y);
		if (containedEnts != null) {
			for (int i = 0; i < containedEnts.size(); i++) {
				containedEnts.get(i).draw(g, x, y);
			}
		}
		if (playerSaw && !inLos) {
			g.drawImage(overlay, x, y);
		}
	}

	public void update() {	
	}
	
	public String toString() {
		return name + ": (" + ix + ", " + iy +")";
	}
}
