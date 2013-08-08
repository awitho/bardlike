package me.bloodarowman.bardlike;

/**
 *
 * @author Alex
 */
public class PathfindingTile {
	// There's a reason for these being public!!! (Fixed, reason was I was too lazy.)
    private PathfindingTile parent; // Tile's parent, used for finding final path.
	private int x;
	private int y;
	private double f; // g + h
	private double g; // Guess, 10 for straight, 14 for diagnol.
	private double h; // Heuristic (Manahattan), g * abs(curX - targetX) + abs(curY - targetY)

	public PathfindingTile(PathfindingTile parent, int x, int y, double f, double g, double h) {
		this.setParent(parent);
		this.setX(x);
		this.setY(y);
		this.setF(f);
		this.setG(g);
		this.setH(h);
	}
	
	@Override
	public String toString() {
		return "(PathfindingTile | x: " + getX() + ", y: " + getY() + ", f: " + getG() + ", h: " + getH() + ", parent: " + ")";
	}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public PathfindingTile getParent() {
        return parent;
    }

    public void setParent(PathfindingTile parent) {
        this.parent = parent;
    }
}
