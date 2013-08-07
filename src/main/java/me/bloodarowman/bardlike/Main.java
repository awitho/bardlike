package me.bloodarowman.bardlike;

import java.awt.Dimension;
import java.awt.Toolkit;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Main class for our game, creates a container and runs everything.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class Main extends StateBasedGame {
	public Main(String name) {
		super(name);
	}
	
	public static AppGameContainer game;
	public static LuaState L = LuaStateFactory.newLuaState();
	public static double scale;

	public static void main(String[] args) throws SlickException {
		L.openLibs();

		game = new AppGameContainer(new Main("bardLIKE"));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		//int width =  800;
		//int height = 600;
		scale = (width + height) / 110;
		System.out.println(scale);
		game.setDisplayMode(width, height, true);

		game.setVSync(true);
		game.setTargetFrameRate(60);
		game.setMaximumLogicUpdateInterval(10);
		game.setSmoothDeltas(true);
	
		game.start();
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		this.addState(new MainMenuState());
		this.addState(new ClassSelectState());
		this.addState(new MainGameState());
		this.addState(new HelpMenuState());
		this.addState(new GameOverState());
	}
}
