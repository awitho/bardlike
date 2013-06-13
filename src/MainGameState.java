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
	private Player player;
	private Camera cam;
	private Inventory inventory;
	private Log log;
	public TileDictionary tileDictionary;
	public MobDictionary mobDictionary;

	private int transX = 0;
	private int transY = 0;

	@Override
	public void init(GameContainer container, StateBasedGame s) throws SlickException {
		ItemDictionary.initItemDictionary();
		tileDictionary = new TileDictionary();
		mobDictionary = new MobDictionary();
		log = new Log();
		map = DungeonGenerator.generateDungeon(24, 24, tileDictionary, mobDictionary);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g) throws SlickException {
		cam.translate(g, container);

		g.setColor(Color.white);
		g.drawRect(-1, -1, map.getScaledWidth() + 1, map.getScaledHeight() + 1);

		map.draw(g, player, cam);
		
		log.draw(g, -cam.getX(), (-cam.getY()) + container.getHeight());
		
		//Overlay
		//g.translate(0, 0);
		g.setFont(MainMenuState.font);
		if (player.getTile() == null) { return; }
		g.drawString("Ply x: " + player.getTile().getX() + ", y: " + player.getTile().getY() + " HP: " + player.getHP(), -cam.getX(), -cam.getY() + 20);
		inventory.draw(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE) && !inventory.isOpen()) {
			container.exit();
		}
		//map.update(container);
		player.update(container);
		inventory.update(container);
		log.update();
		
		if(player.isDead()) {
			
		}

		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public int getID() {
		return 4;
	}

	public void setPlayer(SpriteSheet sprite, JsonObject data) {
		player = new Player(sprite, data, log);
		inventory = new Inventory(player);
		cam = new Camera(player, map);
		DungeonGenerator.placePlayerInFeasibleLocation(map.getTiles(), player, map);
	}
}
