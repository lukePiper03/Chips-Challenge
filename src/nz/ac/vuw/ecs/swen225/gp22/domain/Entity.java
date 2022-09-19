package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;

/**
 * @author Linda Zhang 300570498
 * This represents the objects that interact with the player
 * These are different from cells. They are on top of cells and could move (eg monster)
 * Entities can be collected by the player if it's a Treasure or a Key
 */
public interface Entity {
	/**
	 * Do the interaction with the player
	 * @param p the player
	 * @param cells the current cells board
	 * @return true if interaction successful
	 */
	boolean onInteraction(Player p, Cells cells);
	
	/**
	 * @return image representing the entity
	 */
	Img getImage();
}
