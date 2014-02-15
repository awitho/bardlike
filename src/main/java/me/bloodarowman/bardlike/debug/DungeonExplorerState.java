package me.bloodarowman.bardlike.debug;

import me.bloodarowman.bardlike.*;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;

public class DungeonExplorerState extends BasicGameState {
	public static final int STATE_ID = 6;
	private GameMap map;
	private int x = -1125, y = -1250;
	private float scale = 0.23f;
	private MobDictionary mobD;
	private boolean filter = false, save = false;

	@Override
	public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
	}

	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g) throws SlickException {
		if (map == null) { return; }
		g.pushTransform();
		g.translate(container.getWidth()/2, container.getHeight()/2);
		g.scale(scale, scale);
		g.translate(-(container.getWidth()/2), -(container.getHeight()/2));
		g.translate(x, y);
		map.filter(filter);
		map.draw(container, s, g, new Vector(-(x-container.getWidth()/2)/64, -(y-container.getHeight()/2)/64), 1/scale);
		g.popTransform();
		if (save) {
			Image img = new Image(container.getWidth(), container.getHeight());
			g.copyArea(img, 0, 0);
			ImageOut.write(img, "./maps/" + (new Date()).getTime() + ".png");
			img.destroy();
			save = false;
		}
		g.drawString("s: " + scale + " x:" + x + " y:" + y, 0, 0);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		try {
			mobD = new MobDictionary();
		} catch (MalformedURLException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			return;
		} catch (URISyntaxException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			return;
		}
		genNewMap();
	}

	@Override
	public void update(GameContainer container, StateBasedGame stateBasedGame, int i) throws SlickException {
		if (container.getInput().isKeyDown(Input.KEY_LEFT) || container.getInput().isKeyDown(Input.KEY_A)) {
			x += 5 * (1/scale);
		}
		if (container.getInput().isKeyDown(Input.KEY_RIGHT) || container.getInput().isKeyDown(Input.KEY_D)) {
			x -= 5 * (1/scale);
		}
		if (container.getInput().isKeyDown(Input.KEY_DOWN) || container.getInput().isKeyDown(Input.KEY_S)) {
			y -= 5 * (1/scale);
		}
		if (container.getInput().isKeyDown(Input.KEY_UP) || container.getInput().isKeyDown(Input.KEY_W)) {
			y += 5 * (1/scale);
		}
		if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			genNewMap();
		}
		int wheel = Mouse.getDWheel();
		if (wheel != 0) {
			scale += ((double)wheel)/1000;
			if (scale < 0.1f) {
				scale = 0.1f;
			} else if (scale > 5.0f) {
				scale = 5.0f;
			}
			if (scale >= 1.0f) {
				filter = false;
			} else if (scale < 1.0f) {
				filter = true;
			}
		}
	}

	@Override
	public int getID() {
		return STATE_ID;
	}

	public GameMap getMap() {
		return map;
	}

	public void genNewMap() {
		map = DungeonGenerator.generateDungeon(Misc.DUNGEON_SIZE, Misc.DUNGEON_SIZE, mobD);
		save = true;
	}
}
