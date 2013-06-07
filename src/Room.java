import java.util.ArrayList;

/**
 * A ORAMOWMDEAWOMDO, K?
 * @author Alex
 */
public class Room {
	private Tile[][] room;
	private int connections = 0;
	
	public Room(Tile[][] tiles) {
		this.room = tiles;
	}
	
	public void addHallway() {
		connections++;
	}
	
	public Vector getRandomWall() {
		return new Vector(0, 0);
	}
}
