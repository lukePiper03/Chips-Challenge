package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

/**
 * Represents a Key entity. It is used to unlock LockedDoor cells
 * @author Linda Zhang 300570498
 */
record Key(Point pos, int matchDoorCode) implements Entity{
	
	public boolean onInteraction(Player p, Cells cells, SoundPlayer soundplayer) {
		if(!p.getPos().equals(pos)) return false; //player not on key, do nothing
		
		boolean found = false;
		int size = p.entitiesOnBoard().size();
		for(Cell c: cells.getAllLockedDoors()) {
			if(((LockedDoor) c.state()).keyCode() == matchDoorCode) {
				//found a matching locked door
				p.inventory().add(this);
				soundplayer.play(Sound.beep);
				
				System.out.println("\n Inventory:");
				p.inventory().forEach(i -> System.out.println(i));
				
				p.entitiesOnBoard().remove(this); //remove key from board
				c.setState(new Floor()); //change state of LockedDoor to floor
				found = true;
			}
		}
		if(!found) throw new IllegalStateException("No LockedDoor exists to match this key");
		assert p.entitiesOnBoard().size() == size - 1: "No LockedDoor matches this key";
		return true;
	}
	public Point getPos() {return pos;}
	public Img getImage() {return Img.door_key;}
}