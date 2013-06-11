import org.newdawn.slick.Image;

/**
 * A basic enemy class, bosses tbi.
 * @author Alex
 */
public class Mob extends Entity {
	private int health;
	
	public Mob(MobDictionary mobDictionary) {
		super(mobDictionary.getMobImage());
		this.health = mobDictionary.getHealth();
	}
	
	@Override
	public void update() {

	}
}
