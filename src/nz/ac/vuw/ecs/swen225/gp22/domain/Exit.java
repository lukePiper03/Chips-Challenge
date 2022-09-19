package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;

/**
 * Rrepresents an exit entity. Ends the game.
 * @author lindazhang
 *
 */
record Exit(Point pos, Level level) implements Entity{
	public boolean onInteraction(Player p, Cells cells) {
		if(!p.getPos().equals(pos)) return false; //player not on exit, do nothing
		p.entitiesOnBoard().remove(this);
		level.gameOver();
		return true;
	}
	public Img getImage() {return Img.water;} //change later
}