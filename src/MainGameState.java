import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class MainGameState extends BasicGameState {
        private GameMap map;
        private SpriteSheet playerSprites;
        private Player player;
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
                g.translate(transX, transY);
                // Translate g to player!
		map.draw(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
                    container.exit();
                }
                if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
                    transX += 10;
                }
                if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
                    transX -= 10;
                }
                if (container.getInput().isKeyDown(Input.KEY_UP)) {
                    transY += 10;
                }
                if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
                    transY -= 10;
                }
	}

	@Override
	public int getID() {
		return 4;
	}
	
	public void setPlayer(Player p) {
		player = p;
	}
}