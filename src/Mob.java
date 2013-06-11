import org.newdawn.slick.Image;

/**
 * A basic enemy class, bosses tbi.
 * @author Alex
 */
public class Mob extends Entity {
	private int health;
	private String name;
	
	public Mob(MobDictionary mobDictionary, String name) {
		super(mobDictionary.getMobImage(name));
		this.health = mobDictionary.getHealth();
		this.name = name;
	}
	
	@Override
	public void update() {

	}
}
