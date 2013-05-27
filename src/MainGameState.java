import com.google.gson.JsonObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The main game 'state' basically in-game render loop and logic.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class MainGameState extends BasicGameState {
	private GameMap map;
	private SpriteSheet playerSprites;
	private Player player;
	private Camera cam;
	public TileDictionary tileDictionary;

	private int transX = 0;
	private int transY = 0;
		
	public MainGameState() {
		
	}

	@Override
	public void init(GameContainer container, StateBasedGame s) throws SlickException {
			tileDictionary = new TileDictionary();
			map = new GameMap(24, 24, tileDictionary);
	}

	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g) throws SlickException {
		//g.translate(transX, transY);
		cam.translate(g, container);
		g.setColor(Color.white);
		g.drawRect(-1,-1, map.getScaledWidth() + 1, map.getScaledHeight() + 1);
		map.draw(g, player, cam);
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			container.exit();
		}

		if (container.getInput().isKeyPressed(Input.KEY_LEFT)) {
			player.move(Direction.LEFT);
		}
		if (container.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			player.move(Direction.RIGHT);
		}
		if (container.getInput().isKeyPressed(Input.KEY_DOWN)) {
			player.move(Direction.DOWN);
		}
		if (container.getInput().isKeyPressed(Input.KEY_UP)) {
			player.move(Direction.UP);
		}

		//transX = (player.getX() * -1) + (5*64);
		//transY = (player.getY() * -1) + (5*64);
		
		/*
		//If statements here for testing purposes, make a method for it later.
		//Makes it so the map doesn't go out of screen bounds (A bit buggy atm).
		if(transY > 0 && transX + map.getScaledWidth() <= container.getWidth()) {
			transX = -container.getWidth();
			transY = 0;
		} else if (transX + map.getScaledWidth() <= container.getWidth() && transY + map.getScaledHeight() <= container.getHeight()) {
			transX = -container.getWidth();
			transY = -container.getHeight();
		} else if (transX > 0 && transY + map.getScaledWidth() <= container.getHeight()) {
			transX = 0;
			transY = -container.getHeight();
		} else if (transY + map.getScaledHeight() <= container.getHeight()) {
			transY = -container.getHeight();
		} else if (transX + map.getScaledWidth() <= container.getWidth()) {
			transX = -container.getWidth();
		} else if(transX > 0) {
			transX = 0;
		} else if (transY > 0) {
			transY = 0;
		} else if (transX > 0 && transY > 0) {
			transX = 0;
			transY = 0;
		}
		*/
		//System.out.println(transY);
	}

	@Override
	public int getID() {
		return 4;
	}

	public void setPlayer(SpriteSheet sprite, JsonObject data) {
		player = new Player(sprite, data, map);
		cam = new Camera(player, map);
	}
}
