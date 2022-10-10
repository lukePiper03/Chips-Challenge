package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * This represents the objects that interact with the player
 * These are different from cells. They are on top of cells and could move (eg monster)
 * Entities can be collected by the player if it's a Treasure or a Key
 */
public abstract class Entity extends Subject{
	/**
	 * Do the interaction with the player
	 * @param p the player
	 * @param cells the current cells board
	 */
	public abstract void onInteraction(Player p, Cells cells);
	
	/**
	 * @return the Point for the location of the entity
	 */
	public abstract Point getPos();
	/**
	 * @return name representing the entity
	 */
	public String getName() {return this.getClass().getSimpleName();}
}
