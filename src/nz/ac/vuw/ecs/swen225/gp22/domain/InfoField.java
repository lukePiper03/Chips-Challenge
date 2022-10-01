package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

/**
 * Represents an InfoField entity. Displays message.
 * @author Linda Zhang 300570498
 */
public record InfoField(Point pos, String message) implements Entity{
	
	public void onInteraction(Player p, Cells cells, SoundPlayer soundplayer) {
		if(!p.getPos().equals(pos)) {
			p.removeActiveInfoField();
			throw new IllegalStateException("Player is not on InfoField!");
		}
		
		if(p.getActiveInfoField().isEmpty()) { //only display the first time player is on the info field
			p.setActiveInfoField(this);
			System.out.println("InfoField: "+message); //change later to display message differently
			soundplayer.play(Sound.beep);
		}
	}
	/**
	 * @return the message that the InfoField displays
	 */
	public String getMessage() {return message;}
	public Point getPos() {return pos;}
}