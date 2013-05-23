import java.util.ArrayList;

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


public class ClassSelectState extends BasicGameState {
	private SpriteSheet classSprites;
	private JsonArray classes;
	private JsonObject character;
	private ArrayList<String> charNames;
	private ArrayList<Image> charSprite;
	private MouseOverArea mouseOver;
	
	@Override
	public void init(GameContainer container, StateBasedGame s)
			throws SlickException {
		charSprite = new ArrayList<Image>();
		charNames = new ArrayList<String>();
		classes = new GameConfig("classes.json").getArray();
		classSprites = new SpriteSheet("./gfx/charcreation.png", 64, 64);
		
		
		for(int i = 0; i < classes.size(); i++) {
			character = classes.get(i).getAsJsonObject();
			charSprite.add(classSprites.getSubImage(character.get("sx").getAsInt(), character.get("sy").getAsInt()));
			charNames.add(character.get("name").getAsString());
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g)
			throws SlickException {
		g.drawString("Choose Your Character!", container.getWidth()/2 - 100, 50);
		int locY = container.getHeight() / 2 - 130;
		for(int i = 0; i < charSprite.size(); i++) {
			g.drawImage(charSprite.get(i), container.getWidth() / 2 + 50, locY);
			g.drawString(charNames.get(i), container.getWidth() / 2 - 20, locY);
			locY += 64;
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta)
			throws SlickException {
		//add buttons eventually.
		int chosen = 0;
		Player player;
		MainGameState main = (MainGameState) s.getState(4);
		if(container.getInput().isKeyPressed(Input.KEY_B)) {
			chosen = 0;
			s.enterState(4);
		}else if(container.getInput().isKeyPressed(Input.KEY_M)) {
			chosen = 1;
			s.enterState(4);
		}else if(container.getInput().isKeyPressed(Input.KEY_P)) {
			chosen = 2;
			s.enterState(4);
		}else if(container.getInput().isKeyPressed(Input.KEY_W)) {
			chosen = 3;
			s.enterState(4);
		}else if(container.getInput().isKeyPressed(Input.KEY_R)) {
			chosen = 4;
			s.enterState(4);
		}
		character = classes.get(chosen).getAsJsonObject();
		player = new Player(classSprites, character);
		main.setPlayer(player);
	}

	@Override
	public int getID() {
		return 2;
	}

}
