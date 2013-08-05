package me.bloodarowman.bardlike;

import com.google.gson.JsonObject;
import java.util.ArrayList;
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
	private GameMap curMap;
	private ArrayList<GameMap> levels = new ArrayList<GameMap>();
	private Player player;
	private Camera cam;
	private Inventory inventory;
	private Log log;
	private HUD hud;
	public MobDictionary mobDictionary;

	@Override
	public void init(GameContainer container, StateBasedGame s) 
			throws SlickException {
		Misc.init();
		ItemDictionary.initItemDictionary(); // Needed as itemdicionary
					//is static, also so we can on-demand load.
		TileDictionary.initTileDictionary();
		mobDictionary = new MobDictionary();
		log = new Log();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g)
			throws SlickException { // Part of BasicGameState
		cam.translate(g, container);

		g.setColor(Color.white); // Outline map with white.
		g.drawRect(-1, -1, curMap.getScaledWidth() + 1,
				curMap.getScaledHeight() + 1);

		curMap.draw(g, player, cam);
		
		log.draw(g, -cam.getX(), (-cam.getY()) + container.getHeight());
		hud.draw(g, (-cam.getX()) + container.getWidth(),
				(-cam.getY()) + container.getHeight());
		
		//Overlay
		
		g.setFont(MainMenuState.font);
		if (player.getTile() == null) { return; } // If the player doesn't exist drawing his stats will draw null.
		inventory.draw(g, -cam.getX(), -cam.getY());
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta)
			throws SlickException { //Part of BasicGameState
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE) && 
				!inventory.isOpen()) {
			s.enterState(1);
		}

		player.update(container);
		inventory.update(container);
		log.update();
		
		if (player.isDead()) {
			s.enterState(5);
		}

		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public int getID() { // Part of BasicGameState, identifies what
					//id this state is, so we can jump to it from other states.
		return 4;
	}
	
	public void restart() {
		levels.clear();
		setLevel(genNewLevel());
		player.setXP(0);
		player.clearInventory();
		log.clear();
		DungeonGenerator.placePlayerInFeasibleLocation(curMap, player);
	}
	
	public void clearMaps() {
		levels.clear();
	}
	
	public int genNewLevel() {
		levels.add(DungeonGenerator.generateDungeon(Misc.DUNGEON_SIZE,
				Misc.DUNGEON_SIZE, mobDictionary));
		return levels.size() - 1;
	}
	
	public GameMap setLevel (int l) {
		if (l < 0 || l > levels.size()) { return null; }
		curMap = levels.get(l);
		TileDictionary.setDungeonLevel(l + 1);
		return curMap;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(SpriteSheet sprite, JsonObject data) {
		player = new Player(sprite, data, log, this);
		hud = new HUD(player);
		inventory = new Inventory(player);
		cam = new Camera(player, curMap);
		DungeonGenerator.placePlayerInFeasibleLocation(curMap, player);
	}
}
