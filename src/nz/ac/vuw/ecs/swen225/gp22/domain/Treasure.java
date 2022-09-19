package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;

/**
 * Represents a Treasure entity. Once all treasures are collected the exitLock is unlocked
 * @author Linda Zhang 300570498
 *
 */
record Treasure(Point pos) implements Entity{
	
	public boolean onInteraction(Player p, Cells cells) {
		if(!p.getPos().equals(pos)) return false; //player not on treasure, do nothing
		
		int size = p.entitiesOnBoard().size();
		//remove treasure and change state of exitlock to floor if all treaures are collected
		p.entitiesOnBoard().remove(this); 
		if(p.allTreasuresCollected()) {
			Cell exitlock = cells.getExitLock();
			exitlock.setState(new Floor());
		}
		assert p.entitiesOnBoard().size() == size - 1: "Treasure was not correctly removed";
		return true;
	}
	public Img getImage() {return Img.chip;}
}