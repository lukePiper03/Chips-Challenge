package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * Represents an exit entity.
 * @author lindazhang 300570498
 *
 */
public class Exit extends Entity{
	private final Point pos;
	/**
	 * @param pos the location of the Exit
	 */
	public Exit(Point pos){this.pos = pos;}
	public void onInteraction(Player p, Cells cells) {
		if(!p.getPos().equals(pos)) throw new IllegalStateException("Player is not on Exit!");
		p.entitiesToRemove().add(this);
		onChange();
	}
	public Point getPos() {return pos;}
}