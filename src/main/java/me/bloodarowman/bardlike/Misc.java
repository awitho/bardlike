package me.bloodarowman.bardlike;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

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
	private static File errorLog;
	private static FileWriter write;
	
	public static HashMap<String, Image> miscImages = new HashMap<String, Image>();
	
	
	public static void init() {
		initLogs();
		fillMiscImages();
	}
	
	private static void initLogs() {
		try {
			errorLog = new File("./error.txt");
			System.out.println(errorLog.toString());
			write = new FileWriter(errorLog);
		} catch (IOException ex) {
			System.out.println("Unable to create error log!");
			System.exit(1);
		}
	}


    //TODO: Grab errors from Lua Interpreter.
	public static void LuaExecFileList(ArrayList<File> files) {
        for (File file : files) {
            try {
                //Main.L.load(scripts.get(i)., null);
                //Main.L.LdoFile(files.get(i).toString());
                Main.luaThread.addFile(file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
	}
	
	public static ArrayList<File> findFilesRecurse(String dir, String filter) {
		ArrayList<File> finalFiles = new ArrayList<File>();
		File folder = new File(dir);
		File[] files = folder.listFiles();
		if (files == null || files.length == 0) { return finalFiles; }
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().matches(filter)) {
                    finalFiles.add(file);
                }
            } else if (file.isDirectory()) {
                ArrayList<File> newFiles = findFilesRecurse(file.getPath(), filter);
                finalFiles.addAll(newFiles);
            }
        }
		return finalFiles;
	}
	
	public static void printArray(Object[] obj) {
		if (obj == null || obj.length == 0) { return; }
		System.out.print("[");
		System.out.print(" l: " + obj.length + ", ");
        for (Object anObj : obj) {
            System.out.print(obj.toString() + ", ");
        }
		System.out.println("]");
	}

    public static float easeInOut(float t,float b , float c, float d) {
        return -c/2 * ((float)Math.cos(Math.PI*t/d) - 1) + b;
    }
	
	public static void logError(Object obj) {
		try {
			write.write(obj.toString() + "\n");
			write.flush();
		} catch (IOException ex) {
			System.out.println("Unable to write to error log!");
			System.exit(1);
		}
	}

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
	private static void fillMiscImages() {
		miscImages.put("downladder", ImageLoader.loadSpritesheet("./gfx/ents/hole.png", 32, 32).getScaledCopy(Misc.TARGET_SIZE, Misc.TARGET_SIZE));
		
		ImageBuffer placeholder = new ImageBuffer(Misc.TARGET_SIZE, Misc.TARGET_SIZE);
		for (int x = 0; x < Misc.TARGET_SIZE; x++) {
			for (int y = 0; y < Misc.TARGET_SIZE; y++) {
				placeholder.setRGBA(x, y, 255, 0, 0, 255);
			}
		}
		miscImages.put("placeholder", placeholder.getImage());
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
        return (int) (Math.random() * 2) != 0;
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
			ArrayList<PathfindingTile> openList = new ArrayList<PathfindingTile>();
			ArrayList<PathfindingTile> closedList = new ArrayList<PathfindingTile>();
			
			Tile[][] tiles = map.getTiles();
			
			PathfindingTile curLookingTile = new PathfindingTile(null, x1, y1, 0, 0, 0);
			openList.add(curLookingTile);
			
			int added = 0; // May not be needed?
			
			outerloop: // Loop label.
			while (true) {
				innerloop: // Loop label.
				for (Direction dir : Direction.values()) { // Find adj tiles to starting point.
					Vector vec = Misc.getLocFromDir(curLookingTile.getX(), curLookingTile.getY(), dir);
					
					if (vec.getX() == x2 && vec.getY() == y2) { closedList.add(new PathfindingTile(curLookingTile, x2, y2, 0, 0, 0)); break outerloop; }
					
					for (int i = 0; i < closedList.size(); i++) {
						if (vec.getX() == closedList.get(i).getX() && vec.getY() == closedList.get(i).getY()) {
							continue innerloop;
						}
					}
					
					for (int i = 0; i < openList.size(); i++) {
						if (vec.getX() == openList.get(i).getX() && vec.getY() == openList.get(i).getY()) {
							continue innerloop; // Possibly, might want to remove?
						}
					}
					
					try {
						if (vec.getX() < 0 || vec.getX() > tiles.length || vec.getY() < 0 || vec.getY() > tiles[0].length || (tiles[vec.getX()][vec.getY()].isWall() && tiles[vec.getX()][vec.getY()].isReal())) { continue; }
					} catch (IndexOutOfBoundsException ignored) { }

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
				//System.out.println("Added: " + curLookingTile + " to closedList");
				
				int lowest = 0;
				for (int i = 0; i < openList.size(); i++) { // Find lowest cost tile. (Will pick last tile if some are the same.)
					if (openList.get(i).getF() < openList.get(lowest).getF()) {
						lowest = i;
					}
				}
				
				curLookingTile = openList.get(lowest); // Set new tile to starting point, remove from open, add to closed.
				//closedList.add(curLookingTile);
				//System.out.println("Added: " + curLookingTile + " to closedList");
				openList.remove(lowest);
            }
			return closedList;
	}
}
