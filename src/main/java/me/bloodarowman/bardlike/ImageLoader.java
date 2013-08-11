/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.bloodarowman.bardlike;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 *
 * @author Alex
 */
public class ImageLoader { // This whole class is so we can globally change image loading methods && add easier compatibility for jar storage
	public static SpriteSheet loadSpritesheet(String path, int xS, int yS) {
		try {
            SpriteSheet ss = new SpriteSheet(ImageLoader.class.getResource("/gfx/" + path), xS, yS);
            ss.setFilter(Image.FILTER_NEAREST);
			return ss;
		} catch(Exception ex) {
			handleException(ex);
		}
		return null;
	}

    public static Image loadImage(String path) {
        try {
            BufferedImage img = ImageIO.read(ImageLoader.class.getResource("/gfx/" + path));
            Texture tex = BufferedImageUtil.getTexture("/", img);
            return new Image(tex);
        } catch (Exception ex) {
            handleException(ex);
        }
        return null;
    }

    private static void handleException(Exception ex) {
        Main.game.exit();
        StackTraceElement[] st = ex.getStackTrace();
        Misc.logError(ex);
        for (StackTraceElement ele : st) {
            Misc.logError("\t" + ele.toString());
        }
        Misc.logError("\nUnable to load items spritesheet.");
        System.exit(1);
    }
}
