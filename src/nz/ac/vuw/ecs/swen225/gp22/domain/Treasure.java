package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

/**
 * Represents a Treasure entity. Once all treasures are collected the exitLock is unlocked
 * @author Linda Zhang 300570498
 *
 */
public record Treasure(Point pos) implements Entity{
	
	public void onInteraction(Player p, Cells cells, SoundPlayer soundplayer) {
		if(!p.getPos().equals(pos)) throw new IllegalStateException("Player is not on Treasure!");
		
		//intial values before change is made
		int size = p.entitiesOnBoard().size();
		int treasureCount = p.treasuresToCollect();
		if(treasureCount <= 0) throw new IllegalStateException("Cannot have negative treasure to collect!");
		
		//queue to remove treasure, decrease the number of treasures to collect
		p.decreaseTreasureCount();
		p.entitiesToRemove().add(this);
		soundplayer.play(Sound.beep);
		
		if(p.treasuresToCollect() <= 0) {
			Cell exitlock = cells.getExitLock();
			exitlock.setState(new Floor()); //unlocked
		}
		
		assert p.entitiesOnBoard().size() == size - 1: "Treasure was not correctly removed"; //might be wrong
		assert treasureCount == p.treasuresToCollect() - 1: "Treasure count was not correctly decreased";
		
		System.out.println("Treasure Count: "+p.treasuresToCollect());
	}
	public Point getPos() {return pos;}
	public Img getImage() {return Img.chip;}
}