package me.bloodarowman.bardlike;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A generic entity class, represents everything that's not a tile.
 * @since 5/26/13
 * @version 1
 * @author Alex
 */
public abstract class Entity {
	private static int gId = 1; // global id
	private int id;
	private int x, y;
	private boolean visible = false;
	private Image img;
	private GameMap map;
	private Tile curTile;

	public Entity(Image img) {
		id = gId;
		gId++;
		this.img = img;
	}

	public void draw(Graphics g, int x, int y) {
		if(!visible) { return; }
		setPos(x, y);
		//setPos((int) Misc.lerp(this.x, curTile.getTileX() * Misc.TARGET_SIZE, 0.1), (int) Misc.lerp(this.y, curTile.getTileY() * Misc.TARGET_SIZE, 0.1));
		g.drawImage(img, this.x, this.y);
	}

	public abstract void update(GameContainer container, StateBasedGame s, int delta);
	//public abstract void updateAttacks();

	public void setTile(Tile tile) {
		if (curTile != null) { getTile().removeEnt(this); } 
						// Remove from old tile, if we had a last tile.
		if (tile == null) {
			curTile = tile;
			visible = false;
			return;
		} else {
			visible = true;
		} // Make sure entity is visible, now that we are on a tile!
		
		curTile = tile; // Set our current tile.
		tile.addEnt(this); // Add to new tile.
		//setPos(tile.getTileX() * Misc.TARGET_SIZE, tile.getTileY() * Misc.TARGET_SIZE);
	}

	public Tile getTile() {
		return curTile;
	}
	
	public Image getImage() {
		return img;
	}
	
	public void setMap(GameMap map) {
		this.map = map;
	}

	public GameMap getMap() {
		return map;
	}

	public void setImage(Image img) {
		this.img = img;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setPos(int x, int y){
		setX(x);
		setY(y);
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	@Override
	public String toString() {
		return "(Entity | id: " + id + ", x: " + x + ", y: " + y + ")";
	}
}
