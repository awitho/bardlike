/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.bloodarowman.bardlike;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex
 */
public class Buff {
	private static int gId = 0;
	private int id = 0;
	private double ttl = 0;
	private double life = 0;
	private Player ply;
	private Method func;
	
	public Buff(Player ply, Method func) {
		this.ply = ply;
		this.func = func;
		this.id = gId++;
	}
	
	public int getID() {
		return id;
	}
	
	public void update() {
		life--;
		if (life < ttl) { ply.removeBuff(id); }
		try {
			func.invoke(this, (Object[]) null);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(Buff.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(Buff.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(Buff.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
