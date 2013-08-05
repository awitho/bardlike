/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.bloodarowman.bardlike;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Alex
 */
public class ImageLoader {
	public static SpriteSheet loadSpritesheet(String path, int xS, int yS) {
		try {
			return new SpriteSheet("./gfx/ents/items.png", 32, 32);
		} catch(Exception ex) {
			Main.game.exit();
			StackTraceElement[] st = ex.getStackTrace();
			Misc.logError(ex);
			for (StackTraceElement ele : st) {
				Misc.logError("\t" + ele.toString());
			}
			Misc.logError("\nUnable to load items spritesheet.");
			System.exit(1);
		}
		return null;
	}
}
