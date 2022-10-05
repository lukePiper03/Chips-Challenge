package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;
import nz.ac.vuw.ecs.swen225.gp22.recorder.Recorder;

/**
 * @author Linda Zhang 300570498
 *  the player of the game. Represents Chap
 */
public class Player extends Subject{
	private Point pos;
	private Point oldPos; //for smooth rendering
	private Direction direction = Direction.None;
	
	private int timestamp = 5; //update position once every 5 ticks
	private int timeSinceLastMove = 5; //a counter from the last move
	
	private Set<Entity> entitiesOnBoard;
	private int treasuresToCollect; //a counter for the number of treasures that need collecting
	private Set<Entity> inventory = new HashSet<>();
	private Set<Entity> entitiesToRemove = new HashSet<>(); //set to add to if the entity is to be removed
	
	private Optional<InfoField> activeInfoField = Optional.empty(); //for when the player is on an InfoField
	
	Player(Point p, Set<Entity> entities){
		pos = p;
		entitiesOnBoard = entities;
		oldPos = getPos();
		treasuresToCollect = totalTreasureCount(); // start with the total number of treasures on the board
	}
	
	/**
	 * @return the position of the player in a new point
	 */
	public Point getPos() {return new Point(pos.x(), pos.y());}
	
	/**
	 * @return the previous old point of the player (for smooth animation in render)
	 */
	public Point getOldPos() {return new Point(oldPos.x(),oldPos.y());}
	
	/**
	 * @param newPos set the position of the player to the newPos, assigning oldPos to pos
	 */
	public void setPos(Point newPos) {
		oldPos = pos;
		pos = newPos;
	}
	
	/**
	 * @return the moving time (for smooth animation in render)
	 */
	public float getMoveTime() {return (float) timeSinceLastMove / (float)timestamp;}
	
	/**
	 * @return the player's current direction
	 */
	public Direction direction(){ return direction; }
	
	/**
	 * set the player direction
	 * @param d direction to set the player to
	 */
	public void direction(Direction d){ direction=d; }
	
	/**
	 * @param f the function to apply
	 * @return the runnable applied to the direction
	 */
	public Runnable set(Function<Direction,Direction> f){
		return ()->{
			Recorder.recorder.savePlayerMoveEvent((int)System.currentTimeMillis(), direction);
			direction=f.apply(direction);};
	}
	
	/**
	 * @return all the entities on the current board
	 */
	public Set<Entity> entitiesOnBoard() {return entitiesOnBoard;}
	
	/**
	 * @return all the entities in the players inventory (picked up)
	 */
	public Set<Entity> inventory(){return inventory;}
	
	/**
	 * @return the number of treasures on the board currently
	 */
	public int treasuresToCollect() {return treasuresToCollect;}
	
	/**
	 * decrease the count for the number of treasures on the board
	 */
	public void decreaseTreasureCount() {treasuresToCollect --;}
	
	/**
	 * @return the set to queue entities to be removed
	 */
	public Set<Entity> entitiesToRemove(){return entitiesToRemove;}
	
	/**
	 * @return the InfoField the player is standing on. empty optional if not
	 */
	public Optional<InfoField> getActiveInfoField() {return activeInfoField;}
	
	/**
	 * @param i set i to the active info field. Could be empty optional.
	 */
	public void setActiveInfoField(InfoField i) {activeInfoField = Optional.of(i);}
	
	/**
	 * sets the infoField Optional back to empty 
	 */
	public void removeActiveInfoField() {activeInfoField = Optional.empty();}
	
	/**
	 * @param t the teleporter to find a match for
	 * @return if there exists a teleporter that matches the given one
	 */
	public boolean foundMatchingTeleporter(Teleporter t) {
		if(t.getOther() == null) return false; //other is null
		long count = entitiesOnBoard.stream().filter(e -> e.equals(t.getOther())).count();
		if(count != 1) return false; //must have one other also in entities
		if(!t.getOther().getOther().equals(t)) return false; //other's other must be t
		return true;
	}

	
	/**
	 * the player at each tick
	 * @param cells the cells on current level
	 */
	public void tick(Cells cells){
		if(timeSinceLastMove < timestamp) timeSinceLastMove++;
		
		//allow movement every 5 ticks
		if(timeSinceLastMove >= timestamp) {
			Cell nextCell = cells.get(pos.add(direction.point()));
			if(!nextCell.isSolid() || nextCell.state() instanceof LockedDoor) move(direction, cells); //only call move if move is legal
		}
	}
	
	/**
	 * makes player move in a direction
	 * @param d direction
	 * @param cells the cells on current level
	 */
	public void move(Direction d, Cells cells){
		if(d == Direction.None) return; //no movement
		
		Point newPos = pos.add(d.point());
		Cell nextCell = cells.get(newPos);
		
		//remove LockedDoor if key exits to unlock it (has matching color)
		if(nextCell.state() instanceof LockedDoor){
			Key key = findMatchingKey(nextCell.symbol());
			if(key != null) {
				if(key.getColor() != 'G' || cells.getAllLockedDoorsOfType(key.getColor()).size() == 1) {
					inventory.remove(key); //remove from inventory if red or blue, and green if last door
				}
				nextCell.setState(new Floor());
			}
			else return; //otherwise don't move
		}
		
		timeSinceLastMove = 0;
		if(nextCell.isSolid()) { 
			throw new IllegalArgumentException("Chap cannot be on a solid tile"); 
		}
		oldPos = getPos();
		pos = newPos; 
		onChange(); //Observer pattern for Renderer
	}
	
	//total treasure count on the board
	private int totalTreasureCount() {
		return (int) entitiesOnBoard.stream().filter(e -> e instanceof Treasure).count();
	}
	
	//finds a key that matches this color or else return null
	private Key findMatchingKey(char col) {
		return inventory.stream().filter(e -> e instanceof Key && ((Key)e).getColor() == col)
								.map(e -> (Key)e)
								.findFirst().orElse(null);
	}
	
	
}
