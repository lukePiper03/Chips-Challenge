package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

/**
 * Rrepresents an exit entity. Ends the game.
 * @author lindazhang
 *
 */
record Exit(Point pos, Level level) implements Entity{
	public boolean onInteraction(Player p, Cells cells, SoundPlayer soundplayer) {
		if(!p.getPos().equals(pos)) return false; //player not on exit, do nothing
		p.entitiesOnBoard().remove(this);
		soundplayer.play(Sound.beep);
		level.gameOver();
		return true;
	}
	public Point getPos() {return pos;}
	public Img getImage() {return Img.water;} //change later
}