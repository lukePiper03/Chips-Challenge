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
	public void onInteraction(Player p, Cells cells, SoundPlayer soundplayer) {
		if(!p.getPos().equals(pos)) throw new IllegalStateException("Player is not on Exit!");
		p.entitiesToRemove().add(this);
		soundplayer.play(Sound.beep);
		level.gameOver(); //end the game
	}
	public Point getPos() {return pos;}
	public Img getImage() {return Img.water;} //change later
}