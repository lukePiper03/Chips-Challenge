package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

/**
 * Represents a Treasure entity. Once all treasures are collected the exitLock is unlocked
 * @author Linda Zhang 300570498
 *
 */
record Treasure(Point pos) implements Entity{
	
	public boolean onInteraction(Player p, Cells cells, SoundPlayer soundplayer) {
		if(!p.getPos().equals(pos)) return false; //player not on treasure, do nothing
		
		int size = p.entitiesOnBoard().size();
		int treasureCount = p.treasuresToCollect();
		//remove treasure (add to inventory) and change state of exitlock to floor if all treaures are collected
		p.decreaseTreasureCount();
		p.entitiesOnBoard().remove(this); 
		soundplayer.play(Sound.beep);
		if(p.allTreasuresCollected()) {
			Cell exitlock = cells.getExitLock();
			exitlock.setState(new Floor());
		}
		assert p.entitiesOnBoard().size() == size - 1: "Treasure was not correctly removed";
		assert treasureCount == p.treasuresToCollect() - 1: "Treasure count was not correctly decreased";
		
		System.out.println("Treasure Count: "+p.treasuresToCollect());
		return true;
	}
	public Point getPos() {return pos;}
	public Img getImage() {return Img.chip;}
}