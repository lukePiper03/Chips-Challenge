package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * Prepresents Boots enitity. If these are picked up, 
 */
public class Boots extends Entity{
	private final Point pos;
	
	/**
	 * @param pos the location of the Boots
	 */
	public Boots(Point pos) {this.pos = pos;}
	
	public void onInteraction(Player p, Cells cells) {
		if(!p.getPos().equals(pos)) throw new IllegalStateException("Player is not on Boots!");
		
		//intial values before change is made
		int inventorySize = p.inventory().size();

		//remove boots
		p.inventory().add(this);
		p.entitiesToRemove().add(this);
		onChange();
		
		assert p.inventory().size() == inventorySize + 1: "Boots were not correctly removed";
	}

	public Point getPos() {
		return pos;
	}

}
