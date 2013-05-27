import javax.swing.JOptionPane;

/**
 * Purely static class that provides miscellaneous functions for the game.
 * @author alex
 * @since 5/23/13
 * @version 1
 */
public class Misc {
	public static final int TargetSize = 64;

	/**
	 * Pop-up dialog, replaces console println
	 * @param obj Any object to print to console.
	 */
	public static void showDialog(Object obj) {
		JOptionPane.showMessageDialog(null, obj.toString());
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
}
