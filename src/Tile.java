import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Tile {
	private int ix, iy;
	private String name;
	private boolean inLos, playerSaw, isWall;
	private ArrayList<Entity> containedEnts;
	private Image spr, overlay;

	private static final Color overlayColor = new Color(0, 0, 0, 200);

	public Tile(TileDictionary tileDictionary, String tile, int x, int y) {
		if (tile == null) { return; }
		name = tile;
		ix = x;
		iy = y;
		overlay = tileDictionary.getTileImageByName("Overlay");
		isWall = tileDictionary.getTileIsWall(tile); // Ditto. \/
		spr = tileDictionary.getTileImageByName(tile); // Grab a reference for the tile image from the tile dictionary, no need to make our own copy!
		containedEnts = new ArrayList<>();
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
		System.out.println(containedEnts.add(ent) + "");
	}

	public void removeEnt(Entity ent) {
		System.out.println("Attempting to remove ent: " + ent + " from " + this);
		System.out.println(containedEnts.remove(ent) + "");
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

	public void draw(Graphics g, int x, int y) {
		g.drawImage(spr, x, y);
		if (containedEnts != null) {
			for (int i = 0; i < containedEnts.size(); i++) {
				containedEnts.get(i).draw(g);
			}
		}
		if (playerSaw && !inLos) {
			g.drawImage(overlay, x, y);
		}
	}

	public void update() {	
	}
}
