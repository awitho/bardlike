package me.bloodarowman.bardlike;

import me.bloodarowman.bardlike.gui.Inventory;
import me.bloodarowman.bardlike.gui.Log;
import me.bloodarowman.bardlike.gui.HUD;
import com.google.gson.JsonObject;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
	public static final int STATE_ID = 4;

    public static MainGameState current;

	private GameMap curMap;
    private int loops = 0;
	private ArrayList<GameMap> levels = new ArrayList<GameMap>();
	ArrayList<File> autorunScripts = Misc.findFilesRecurse("/scripts/autorun/", "(.*).lua");
	private Player player;
	private Camera cam;
	private Inventory inventory;
	private Log log;
	private HUD hud;
	public MobDictionary mobDictionary;

	@Override
	public void init(GameContainer container, StateBasedGame s) throws SlickException {
        current = this;

        Misc.fillMiscImages();
        try {
            ItemDictionary.initItemDictionary(); // Needed as item dictionary
        } catch (Exception ex) {
            Misc.logError(ex);
            System.exit(1);
        }
        //is static, also so we can on-demand load.
		TileDictionary.initTileDictionary();
        try {
            mobDictionary = new MobDictionary();
        } catch (Exception ex) {
            Misc.logError(ex);
        }
        log = new Log();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g) throws SlickException { // Part of BasicGameState
		g.pushTransform();
		g.scale(Misc.SCALE, Misc.SCALE);

		cam.translate(container, s, g, 1/Misc.SCALE);

		g.setColor(Color.white); // Outline map with white.
		g.drawRect(-1, -1, curMap.getScaledWidth() + 1, curMap.getScaledHeight() + 1);

		curMap.draw(container, s, g, new Vector(player.getTile().getTileX(), player.getTile().getTileY()), 1/Misc.SCALE);
		g.popTransform();
		log.draw(container, s, g);
		hud.draw(container, s, g);
		
		//Overlay
		g.setFont(MainMenuState.font);
		if (player.getTile() == null) { return; } // If the player doesn't exist drawing his stats will draw null.
		inventory.draw(container, s, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException { //Part of BasicGameState
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE) && 
				!inventory.isOpen()) {
			s.enterState(1);
		}

		player.update(container, s, delta);
		inventory.update(container);
		log.update();
		
		if (player.isDead() && loops%100 == 0) {
			s.enterState(5);
		}

		container.getInput().clearKeyPressedRecord();
        loops++;
	}

	@Override
	public int getID() { // Part of BasicGameState, identifies what
					//id this state is, so we can jump to it from other states.
		return STATE_ID;
	}
	
	public void restart() {
		levels.clear();
		setLevel(genNewLevel());
		player.setXp(0);
		player.clearInventory();
		log.clear();
		DungeonGenerator.placePlayerInFeasibleLocation(curMap, player);
	}
	
	public void clearMaps() {
		levels.clear();
	}
	
	public int genNewLevel() {
		levels.add(DungeonGenerator.generateDungeon(Misc.DUNGEON_SIZE, Misc.DUNGEON_SIZE, mobDictionary));
		return levels.size() - 1;
	}

    public Camera getCam() {
        return cam;
    }

    public Log getLog() {
        return log;
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

	public void setPlayer(SpriteSheet sprite, JsonObject data) throws MalformedURLException, URISyntaxException {
		player = new Player(sprite, data, log, this);
		hud = new HUD(player);
		inventory = new Inventory(player);
		cam = new Camera(player, curMap);
		DungeonGenerator.placePlayerInFeasibleLocation(curMap, player);
        cam.setXY(- curMap.getScaledWidth() / 2, - curMap.getScaledHeight() / 2);
		
		Misc.LuaExecFileList(autorunScripts);
	}
}
