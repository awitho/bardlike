/**
 * The Player that you control.
 * 
 * @author Bobby Henley
 * @version 0.1
 */

import java.util.HashMap;

import org.newdawn.slick.SpriteSheet;

import com.google.gson.JsonObject;

public class Player extends Entity {
	private HashMap<String, Integer> godsFavor;
	private HashMap<String, Integer> stats;
	
	public Player(SpriteSheet ss, JsonObject data, GameMap map) {
		super(ss.getSubImage(data.get("sx").getAsInt(), data.get("sy").getAsInt()), map);
	}

	public void move(Direction dir) {
		System.out.println("Attempting to move in dir: " + dir);
			getMap().moveEnt(getTile(), this, dir);
	}
}