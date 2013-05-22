/**
 * The Player that you control.
 * 
 * @author Bobby Henley
 * @version 0.1
 */

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

import com.google.gson.JsonObject;


public class Player extends Entity {
	private int playerX, playerY;
	private GameConfig playerInfo;
	private HashMap<String, Integer> godsFavor;
	private HashMap<String, Integer> stats;
	
	public Player(SpriteSheet ss, JsonObject data) {
		
	}
	
	public void addControls(GameContainer container) {
		if(container.getInput().isKeyPressed(Input.KEY_UP)) {
			
		}
	}
}
