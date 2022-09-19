package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;

/**
 * Rrepresents an exit entity. Ends the game.
 * @author lindazhang
 *
 */
record Exit(Point pos) implements Entity{
	public boolean onInteraction(Player p, Cells cells) {return false;}
	public Img getImage() {return Img.water;} //change later
}