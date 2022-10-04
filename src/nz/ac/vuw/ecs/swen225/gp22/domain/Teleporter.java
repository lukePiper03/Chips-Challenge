package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * Represents a Key entity. It has a matching Teleporter to teleport to
 */
public class Teleporter extends Entity{
	private final Point pos;
	private Teleporter other;
	
	/**
	 * @param pos the position of this teleporter on the map
	 * @param other the other teleporter that connects with this one
	 */
	public Teleporter(Point pos, Teleporter other) {this.pos = pos; this.other = other;}
	
	public void onInteraction(Player p, Cells cells) {		
		if(!p.getPos().equals(pos)) throw new IllegalStateException("Player is not on Teleporter!");
		if(!p.foundMatchingTeleporter(this)) throw new IllegalArgumentException("Other teleporter not valid!");
		if(cells.get(other.getPos().add(p.direction().point())).isSolid()) {
			throw new IllegalStateException("the landing pos after teleporting is solid");
		}
		
		p.setPos(other.getPos().add(p.direction().point())); //teleport player to landing position (one more in direction of other pos)
		onChange();
		
		assert p.getOldPos().equals(pos);
		assert p.getPos().equals(other.getPos().add(p.direction().point()));
	}

	/**
	 * @param other set the other teleporter so it can be initialised
	 */
	public void setOther(Teleporter other) {this.other = other;}
	public Point getPos() {return pos;}
	/**
	 * @return the other teleporter that matches this one
	 */
	public Teleporter getOther() {return other;}
}
