/**
 *
 * @author Alex
 */
public class PathfindingTile {
	// There's a reason for these being public!!!
	public PathfindingTile parent; // Tile's parent, used for finding final path.
	public int x;
	public int y;
	public int f; // g + h
	public int g; // Guess, 10 for straight, 14 for diagnol.
	public int h; // Heuristic (Manahattan), g * abs(curX - targetX) + abs(curY - targetY)

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
		return "(PathfindingTile | x: " + x + ", y: " + y + ", f: " + g + ", h: " + h + ", parent: " + ")";
	}
}
