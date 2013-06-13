import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
	private GameMap curMap;
	private ArrayList<GameMap> levels = new ArrayList<>();
	private Player player;
	private Camera cam;
	private Inventory inventory;
	private Log log;
	public MobDictionary mobDictionary;

	@Override
	public void init(GameContainer container, StateBasedGame s) throws SlickException {
		ItemDictionary.initItemDictionary(); // Needed as itemdicionary is static, also so we can on-demand load.
		TileDictionary.initTileDictionary();
		Misc.fillMiscImages();
		mobDictionary = new MobDictionary();
		log = new Log();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g) throws SlickException { // Part of BasicGameState
		cam.translate(g, container);

		g.setColor(Color.white); // Outline map with white.
		g.drawRect(-1, -1, curMap.getScaledWidth() + 1, curMap.getScaledHeight() + 1);

		curMap.draw(g, player, cam);
		
		log.draw(g, -cam.getX(), (-cam.getY()) + container.getHeight());
		
		//Overlay
		
		g.setFont(MainMenuState.font);
		if (player.getTile() == null) { return; } // If the player doesn't exist drawing his stats will draw null.
		g.drawString("Ply x: " + player.getTile().getX() + ", y: " + player.getTile().getY() + " HP: " + player.getHP(), -cam.getX(), -cam.getY() + 20);
		inventory.draw(g, -cam.getX(), -cam.getY());
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException { //Part of BasicGameState
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE) && !inventory.isOpen()) {
			container.exit();
		}

		player.update(container);
		inventory.update(container);
		log.update();

		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public int getID() { // Part of BasicGameState, identifies what id this state is, so we can jump to it from other states.
		return 4;
	}
	
	public int genNewLevel() {
		System.out.println("Generating dungeon!");
		levels.add(DungeonGenerator.generateDungeon(Misc.DUNGEON_SIZE, Misc.DUNGEON_SIZE, mobDictionary));
		return levels.size() - 1;
	}
	
	public GameMap setLevel (int l) {
		System.out.println("setting level to: " + l);
		if (l < 0 || l > levels.size()) { return null; }
		curMap = levels.get(l);
		return curMap;
	}

	public void setPlayer(SpriteSheet sprite, JsonObject data) {
		player = new Player(sprite, data, log, this);
		inventory = new Inventory(player);
		cam = new Camera(player, curMap);
		DungeonGenerator.placePlayerInFeasibleLocation(curMap, player);
	}
}
