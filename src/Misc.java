import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Purely static class that provides miscellaneous functions for the game.
 * @author alex
 * @since 5/23/13
 * @version 1
 */
public class Misc {
	public static final int TARGET_SIZE = 64;
	public static final int MAX_INVENTORY = 110;
	public static final int DUNGEON_SIZE = 50;
	
	public static HashMap<String, Image> miscImages = new HashMap<>();

	/**
	 * Pop-up dialog, replaces console println
	 * @param obj Any object to print to console.
	 */
	public static void showDialog(Object obj) {
		JOptionPane.showMessageDialog(null, obj.toString());
	}
	
	/**
	 * This is a really hacky method to get images for misc ents.
	 */
	public static void fillMiscImages() throws SlickException {
		miscImages.put("downladder", new SpriteSheet("./gfx/ents/hole.png", 32, 32).getSprite(0, 0).getScaledCopy(Misc.TARGET_SIZE, Misc.TARGET_SIZE));
	}

	/**
	 * Linear interpolation.
	 * @param start Starting value.
	 * @param end Ending value.
	 * @param percent Percent between.
	 * @return Lerped value.
	 */
	public static float lerp(float start, float end, float percent) {
		return (start + percent*(end - start));
	}

	/**
	 * Clamps a value between an upper and lower value.
	 * @param value Value to clamp.
	 * @param low Lower value to clamp to.
	 * @param high Higher value to clamp to.
	 * @return Clamped value
	 */
	public static int clamp(int value, int low, int high) {
		if (value < low) 
			return low;
		if (value > high)
			return high;
		return value;
	}
	
	public static Vector getLocFromDir(int x, int y, Direction dir) {
		switch(dir) {
				case LEFT:
					return new Vector(x - 1, y);
				case RIGHT:
					return new Vector(x + 1, y);
				case UP:
					return new Vector(x, y - 1);
				case DOWN:
					return new Vector(x, y + 1);
		}
					/*
		if (dir == Direction.LEFT) {
			return new Vector(x - 1, y);
		} else if (dir == Direction.RIGHT) {
			return new Vector(x + 1, y);
		} else if (dir == Direction.UP) {
			return new Vector(x, y - 1);
		} else if (dir == Direction.DOWN) {
			return new Vector(x, y + 1);
		} else if (dir == Direction.LEFT_UP) {
			
		} else if (dir == Direction)
		*/
		return null;
	}

	/**
	 * Returns a random boolean!
	 * @return A random boolean.
	 */
	public static boolean randomBool() {
		if ((int) (Math.random() * 2) == 0) {
			return false;
		}
		return true;
	}
	
	public static int randomInt(int n, int m) {
		return (int) (Math.random() * (m - n)) + n;
	}
	
	/*
	 * Pathfinds to a certain tile, then returns the path!
	 * @param map The map to pathfind on.
	 * @param x1 X of first point.
	 * @param y1 Y of first point.
	 * @param x2 X of second point.
	 * @param y2 Y of second point.
	 * @return ArrayList of PathfindingTiles of path.
	 */
	public static ArrayList<PathfindingTile> pathfindTo(GameMap map, int x1, int y1, int x2, int y2) {
			ArrayList<PathfindingTile> openList = new ArrayList<>();
			ArrayList<PathfindingTile> closedList = new ArrayList<>();
			
			Tile[][] tiles = map.getTiles();
			
			PathfindingTile curLookingTile = new PathfindingTile(null, x1, y1, 0, 0, 0);
			openList.add(curLookingTile);
			
			int added = 0; // May not be needed?
			
			outerloop: // Loop label.
			while (true) {
				innerloop: // Loop label.
				for (Direction dir : Direction.values()) { // Find adj tiles to starting point.
					Vector vec = Misc.getLocFromDir(curLookingTile.x, curLookingTile.y, dir);
					
					if (vec.getX() == x2 && vec.getY() == y2) { closedList.add(new PathfindingTile(curLookingTile, x2, y2, 0, 0, 0)); break outerloop; }
					
					for (int i = 0; i < closedList.size(); i++) {
						if (vec.getX() == closedList.get(i).x && vec.getY() == closedList.get(i).y) {
							continue innerloop;
						}
					}
					
					for (int i = 0; i < openList.size(); i++) {
						if (vec.getX() == openList.get(i).x && vec.getY() == openList.get(i).y) {
							continue innerloop; // Possibly, might want to remove?
						}
					}
					
					try {
						if (vec.getX() < 0 || vec.getX() > tiles.length || vec.getY() < 0 || vec.getY() > tiles[0].length || (tiles[vec.getX()][vec.getY()].isWall() && tiles[vec.getX()][vec.getY()].isReal())) { continue; }
					} catch (IndexOutOfBoundsException ex) { }

					//calculate f, g & h here.
					double g = 10.0;
					double h = Double.MAX_VALUE;

					double xDistance = Math.abs(vec.getX() - x2); // Diagonol Shortcut Huerisitic
					double yDistance = Math.abs(vec.getY() - y2);
					if (xDistance > yDistance) {
						h = 14.0 * yDistance + 10.0 * (xDistance-yDistance);
					} else {
						h = 14.0 * xDistance + 10.0 * (yDistance-xDistance);
					}
					
					//double h = 10 * (Math.abs(vec.getX() - x2) + Math.abs(vec.getY() - y2)); // Manhattan Huerisitic

					openList.add(new PathfindingTile(curLookingTile, vec.getX(), vec.getY(), g + h, g, h));
					added++;
				}
				if (added == 0) { return null; }
				if (openList.isEmpty()) { return null; }
				
				openList.remove(curLookingTile); // Add starting tile to closed list.
				closedList.add(curLookingTile);
				
				int lowest = 0;
				for (int i = 0; i < openList.size(); i++) { // Find lowest cost tile. (Will pick last tile if some are the same.)
					if (openList.get(i).f < openList.get(lowest).f) {
						lowest = i;
					}
				}
				
				curLookingTile = openList.get(lowest); // Set new tile to starting point, remove from open, add to closed.
				closedList.add(curLookingTile);
				openList.remove(lowest);
            }
			return openList;
	}
}
