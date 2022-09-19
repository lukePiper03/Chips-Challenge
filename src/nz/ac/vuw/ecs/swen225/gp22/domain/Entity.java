package nz.ac.vuw.ecs.swen225.gp22.domain;

import javax.swing.JOptionPane;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;

/**
 * @author Linda Zhang 300570498
 * This represents the objects that interact with the player
 * These are different from cells. They are on top of cells and could move (eg monster)
 * Entities can be collected by the player if it's a Treasure or a Key
 */
public interface Entity {
	/**
	 * Do the interaction with the player
	 * @param p the player
	 * @param cells the current cells board
	 * @return true if interaction successful
	 */
	boolean onInteraction(Player p, Cells cells);
	
	/**
	 * @return image representing the entity
	 */
	Img getImage();
}

/**
 * Represents a Key entity. It is used to unlock LockedDoor cells
 * @author Linda Zhang 300570498
 *
 */
record Key(Point pos, int matchDoorCode) implements Entity{
	
	public boolean onInteraction(Player p, Cells cells) {
		if(!p.getPos().equals(pos)) return false; //player not on key, do nothing
		
		boolean found = false;
		int size = p.entitiesOnBoard().size();
		for(Cell c: cells.getAllLockedDoors()) {
			if(((LockedDoor) c.state()).keyCode() == matchDoorCode) {
				//found a matching locked door
				p.entitiesOnBoard().remove(this); //remove key from board
				c.setState(new Floor()); //change state of LockedDoor to floor
				found = true;
			}
		}
		if(!found) throw new IllegalStateException("No LockedDoor exists to match this key");
		assert p.entitiesOnBoard().size() == size - 1: "No LockedDoor matches this key";
		return true;
	}
	
	public Img getImage() {return Img.water;} //change later
}

record Treasure(Point pos) implements Entity{
	public boolean onInteraction(Player p, Cells cells) {return false;}
	public Img getImage() {return Img.water;} //change later
}

/**
 * Represents an InfoField entity. Displays message
 * @author Linda Zhang 300570498
 *
 */
class InfoField implements Entity{
	private boolean switchOn = false; // message should only display the first tick
	private Point pos; private String message;
	InfoField(Point pos, String message){this.pos = pos; this.message = message;}
	
	public boolean onInteraction(Player p, Cells cells) {
		if(!p.getPos().equals(pos)) { //player not on Info, do nothing
			switchOn = true;
			return false;
		} 
		//display message
		if(switchOn) {
			JOptionPane.showMessageDialog(null, message);
			switchOn = false;
			return true;
		}
		return false;
	}
	
	public Img getImage() {return Img.water;} //change later
}

record Exit(Point pos) implements Entity{
	public boolean onInteraction(Player p, Cells cells) {return false;}
	public Img getImage() {return Img.water;} //change later
}
