package me.bloodarowman.bardlike;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.JsonElement;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Class selection screen for the game.
 * @since 5/26/13
 * @version 1
 * @author Bobby Henley
 */
public class ClassSelectState extends BasicGameState {
	public static final int STATE_ID = 2;

	private SpriteSheet classSprites;
	private JsonObject classes;
	private ArrayList<String> charNames;
	private ArrayList<Image> charSprite;
	private MouseOverArea mouseOver;
	private boolean loading = false;
	private PlayerClass chosen;

	@Override
	public void init(GameContainer container, StateBasedGame s) throws SlickException {
		charSprite = new ArrayList<Image>();
		charNames = new ArrayList<String>();
        try {
            classes = new GameConfig("classes.json").getObject();
        } catch (Exception ex) {
            Misc.logError(ex);
        }
        classSprites = ImageLoader.loadSpritesheet("ents/chars_lofi.png", 32, 32); // new SpriteSheet(new URL("file:///gfx/ents/chars.png"), 64, 64);

		for (Map.Entry<String, JsonElement> ele : classes.entrySet()) {
			JsonObject character = ele.getValue().getAsJsonObject();
			charSprite.add(classSprites.getSubImage(character.get("sx").getAsInt(), character.get("sy").getAsInt()));
			charNames.add("(" + character.get("hotkey").getAsString() + ")" + character.get("name").getAsString().substring(1));
		}
		//	for(int i = 0; i < classes.size(); i++) {
		//	JsonObject character = classes.get(i).getAsJsonObject();
		//	charSprite.add(classSprites.getSubImage(character.get("sx").getAsInt(), character.get("sy").getAsInt()));
		//	charNames.add("(" + character.get("hotkey").getAsString() + ")" + character.get("name").getAsString().substring(1));
		//}
	}

	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g) throws SlickException {
		if (loading) {
			g.drawString("Loading...", container.getWidth()/2 - g.getFont().getWidth("Loading...")/2, container.getHeight()/2 - g.getFont().getHeight("Loading...")/2);
			return;
		}
		g.drawString("Choose Your Character!", container.getWidth()/2 - 100, 50);
		int locY = container.getHeight() / 2 - 130;
		for(int i = 0; i < charSprite.size(); i++) {
			g.drawImage(charSprite.get(i), container.getWidth() / 2 + 50, locY);
			g.drawString(charNames.get(i), container.getWidth() / 2 - 20, locY);
			locY += 64;
		}
	}

	public enum PlayerClass {
		BARD, MAGE, PALADIN, WARRIOR, ROGUE
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException {
		if (loading) { return; }
		//add buttons eventually.
		MainGameState main = (MainGameState) s.getState(4);
		if (container.getInput().isKeyPressed(Input.KEY_B)) {
			chosen = PlayerClass.BARD;
		} else if(container.getInput().isKeyPressed(Input.KEY_M)) {
			chosen = PlayerClass.MAGE;
		} else if(container.getInput().isKeyPressed(Input.KEY_P)) {
			chosen = PlayerClass.PALADIN;
		} else if(container.getInput().isKeyPressed(Input.KEY_W)) {
			chosen = PlayerClass.WARRIOR;
		} else if(container.getInput().isKeyPressed(Input.KEY_R)) {
			chosen = PlayerClass.ROGUE;
		}
		if (chosen != null) {
			(new LevelLoader(s, this, main)).start();
			loading = true;
		}
		container.getInput().clearKeyPressedRecord();
	}
	
	public void finishLoading(MainGameState main, StateBasedGame s) throws MalformedURLException, URISyntaxException {
		main.setPlayer(classSprites, classes.get(chosen.toString()).getAsJsonObject());
			chosen = null;
			loading = false;
		s.enterState(MainGameState.STATE_ID);
	}

	@Override
	public int getID() {
		return STATE_ID;
	}
	
	private class LevelLoader extends Thread {
		private MainGameState state;
		private ClassSelectState css;
		private StateBasedGame s;
		
		public LevelLoader(StateBasedGame s, ClassSelectState css, MainGameState state) {
			this.state = state;
			this.css = css;
			this.s = s;
		}
		
		public void run() {
			state.clearMaps();
			state.setLevel(state.genNewLevel());
            state.getLog().clear();
            try {
                css.finishLoading(state, s);
            } catch (Exception ex) {
                Misc.logError(ex);
                System.exit(1);
            }
        }
	}
}
