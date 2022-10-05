package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * Represents a Key entity. It is used to unlock LockedDoor cells
 * @author Linda Zhang 300570498
 */
public class Key extends Entity{
	private final Point pos;
	private final char color;
	/**
	 * @param pos the location ofthe Key
	 * @param color the char that represents the color of the key
	 */
	public Key(Point pos, char color){this.pos = pos; this.color = color;}
	
	public void onInteraction(Player p, Cells cells) {
		if(!p.getPos().equals(pos)) throw new IllegalStateException("Player is not on Key!");
		
		//intial values before change is made
		int inventorySize = p.inventory().size();

		//found a matching locked door, remove key
		p.inventory().add(this);
		p.entitiesToRemove().add(this);
		onChange();
		
		assert p.inventory().size() == inventorySize + 1: "key was not correctly removed";
	}
	public Point getPos() {return pos;}
	/**
	 * @return the code that matches a LockedDoor
	 */
	public char getColor() {return color;}
}