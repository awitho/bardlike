/**
 * The Player that you control.
 * 
 * @author Bobby Henley
 * @version 0.1
 */

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SpriteSheet;

import com.google.gson.JsonObject;


public class Player extends Entity {
	private int MOVE_SPEED = 64;
	private Image playerImage;
	private int playerX = 128, playerY = 128;
	private HashMap<String, Integer> godsFavor;
	private HashMap<String, Integer> stats;
	
	public Player(SpriteSheet ss, JsonObject data) {
		playerImage = ss.getSubImage(data.get("sx").getAsInt(), data.get("sy").getAsInt());
	}
	
	//temporarily here as i could not think of a way to make Entity do it
	public void draw(Graphics g) {
		g.drawImage(playerImage, playerX, playerY);
	}
	
	public void addControls(GameContainer container) {
		if (container.getInput().isKeyPressed(Input.KEY_W)) {
			playerY -= MOVE_SPEED;
		} else if (container.getInput().isKeyPressed(Input.KEY_S)) { 
			playerY += MOVE_SPEED;
		} else if (container.getInput().isKeyPressed(Input.KEY_A)) {
			playerX -= MOVE_SPEED;
		} else if (container.getInput().isKeyPressed(Input.KEY_D)) {
			playerX += MOVE_SPEED;
		}
	}
	
	//See above draw.
	public int getX() {
		return playerX;
	}
	
	public int getY(){
		return playerY;
	}
}
