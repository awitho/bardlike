
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class Misc {
	public static void showDialog(Object obj) {
		JOptionPane.showMessageDialog(null, obj.toString());
	}
	
	public static float lerp(float start, float end, float percent) {
		return (start + percent*(end - start));
	}
}