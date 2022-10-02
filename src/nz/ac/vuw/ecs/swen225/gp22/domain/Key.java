package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * Represents a Key entity. It is used to unlock LockedDoor cells
 * @author Linda Zhang 300570498
 */
public class Key extends Entity{
	private final Point pos;
	private final int matchDoorCode;
	/**
	 * @param pos the location ofthe Key
	 * @param matchDoorCode the code of the Key that matches a LockedDoor
	 */
	public Key(Point pos, int matchDoorCode){this.pos = pos; this.matchDoorCode = matchDoorCode;}
	
	public void onInteraction(Player p, Cells cells) {
		if(!p.getPos().equals(pos)) throw new IllegalStateException("Player is not on Key!");
		
		//intial values before change is made
		int inventorySize = p.inventory().size();
		
		for(Cell c: cells.getAllLockedDoors()) {
			if(((LockedDoor) c.state()).keyCode() == matchDoorCode) {
				//found a matching locked door, remove key
				p.inventory().add(this);
				p.entitiesToRemove().add(this);
				onChange();
				
				System.out.println("\n Inventory:");
				p.inventory().forEach(i -> System.out.println(i));
				
				c.setState(new Floor()); //change state of LockedDoor to floor
				break; //only unlock one door for each key
			}
		}
		assert p.inventory().size() == inventorySize + 1: "No LockedDoor exists to match this key or key was not correctly removed";
	}
	public Point getPos() {return pos;}
}