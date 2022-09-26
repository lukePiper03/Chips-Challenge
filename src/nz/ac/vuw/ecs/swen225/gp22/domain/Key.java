package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

/**
 * Represents a Key entity. It is used to unlock LockedDoor cells
 * @author Linda Zhang 300570498
 */
public record Key(Point pos, int matchDoorCode) implements Entity{
	
	public void onInteraction(Player p, Cells cells, SoundPlayer soundplayer) {
		if(!p.getPos().equals(pos)) throw new IllegalStateException("Player is not on Key!");
		
		//intial values before change is made
		boolean found = false;
		int size = p.entitiesOnBoard().size();
		
		for(Cell c: cells.getAllLockedDoors()) {
			if(((LockedDoor) c.state()).keyCode() == matchDoorCode) {
				//found a matching locked door, remove key
				p.inventory().add(this);
				p.entitiesToRemove().add(this);
				soundplayer.play(Sound.beep);
				
				System.out.println("\n Inventory:");
				p.inventory().forEach(i -> System.out.println(i));
				
				c.setState(new Floor()); //change state of LockedDoor to floor
				found = true;
				break; //only unlock one door for each key
			}
		}
		assert found : "No LockedDoor exists to match this key";
		//if(!found) throw new IllegalStateException("No LockedDoor exists to match this key");
		assert p.entitiesOnBoard().size() == size - 1: "Key was not correctly removed"; //might be wrong
	}
	public Point getPos() {return pos;}
	public Img getImage() {return Img.door_key;}
}