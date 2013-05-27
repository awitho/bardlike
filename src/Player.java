import com.google.gson.JsonElement;
import java.util.HashMap;
import org.newdawn.slick.SpriteSheet;
import com.google.gson.JsonObject;
import java.util.Map;

/**
 * The Player that you control.
 * @author Bobby Henley
 * @version 1
 */
public class Player extends Entity {
	private HashMap<String, Integer> godsFavor = new HashMap<>();
	private HashMap<String, Integer> stats = new HashMap<>();
	private String plyClass = "";

	public Player(SpriteSheet ss, JsonObject data, GameMap map) {
		super(ss.getSubImage(data.get("sx").getAsInt(), data.get("sy").getAsInt()), map);
		plyClass = data.get("name").getAsString();
		for(Map.Entry<String, JsonElement> entry: data.get("stats").getAsJsonObject().entrySet()){
				stats.put(entry.getKey(), entry.getValue().getAsInt());
		}
	}

	public void move(Direction dir) {
		System.out.println("Attempting to move in dir: " + dir);
		getMap().moveEnt(getTile(), this, dir);
	}
	
	public String toString() {
		return plyClass + ": " + stats;
	}
}
