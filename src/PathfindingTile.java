/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alex
 */
public class PathfindingTile {
	// There's a reason for these being public!@
	public PathfindingTile parent;
	public int x;
	public int y;
	public int f;
	public int g;
	public int h;

	public PathfindingTile(PathfindingTile parent, int x, int y, int f, int g, int h) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.f = f;
		this.g = g;
		this.h = h;
	}
	
	@Override
	public String toString() {
		return "(PathfindingTile | x: " + x + ", y: " + y + ", f: " + g + ", h: " + h + ", parent: " + parent + ")";
	}
}
