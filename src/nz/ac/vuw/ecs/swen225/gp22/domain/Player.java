package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;
import nz.ac.vuw.ecs.swen225.gp22.recorder.Recorder;

/**
 * @author Linda Zhang 300570498
 *  the player of the game. Represents Chap
 */
public class Player {
	private Point pos;
	private Point oldPos; //for smooth rendering
	private Direction direction = Direction.None;
	
	private int timestamp = 5; //update position once every 5 ticks
	private int timeSinceLastMove = 5; //a counter from the last move
	
	private Set<Entity> entitiesOnBoard;
	private int treasuresToCollect; //a counter for the number of treasures that need collecting
	private Set<Entity> inventory = new HashSet<>();
	private Set<Entity> entitiesToRemove = new HashSet<>(); //set to add to if the entity is to be removed
	
	private InfoField activeInfoField = null;
	
	Player(Point p, Set<Entity> entities){
		pos = p;
		entitiesOnBoard = entities;
		oldPos = getPos();
		treasuresToCollect = totalTreasureCount(); // start with the total number of treasures on the board
	}
	
	/**
	 * @return the position of the player in a new point
	 */
	public Point getPos() {
		return new Point(pos.x(), pos.y());
	}
	/**
	 * @return the previous old point of the player (for smooth animation in render)
	 */
	public Point getOldPos() {
		return new Point(oldPos.x(),oldPos.y());
	}
	/**
	 * @return the moving time (for smooth animation in render)
	 */
	public float getMoveTime() {
		return (float) timeSinceLastMove / (float)timestamp;
	}
	
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
	public Set<Entity> entitiesToRemove(){
		return entitiesToRemove;
	}
	
	/**
	 * @return the InfoField the player is standing on. null if not
	 */
	public InfoField getActiveInfoField() {
		return activeInfoField;
	}
	
	/**
	 * @param i set i to the active info field. Could be null.
	 */
	public void setActiveInfoField(InfoField i) {
		activeInfoField = i;
	}

	
	/**
	 * the player at each tick
	 * @param cells the cells on current level
	 */
	public void tick(Cells cells){
		if(timeSinceLastMove < timestamp) timeSinceLastMove++;
		
		//allow movement every 5 ticks
		if(timeSinceLastMove >= timestamp) {
			if(!cells.get(pos.add(direction.point())).isSolid()) move(direction, cells); //only call move if move is legal
		}
	}
	
	/**
	 * makes player move in a direction
	 * @param d direction
	 * @param cells the cells on current level
	 */
	public void move(Direction d, Cells cells){
		if(d == Direction.None) return; //no movement
		timeSinceLastMove = 0;
		
		Point newPos = pos.add(d.point());
		
		if(cells.get(newPos).isSolid()) { 
			throw new IllegalArgumentException("Chap cannot be on a solid tile"); 
		}
		oldPos = getPos();
		pos = newPos; 
	}
	
	//total treasure count on the board
	private int totalTreasureCount() {
		return (int) entitiesOnBoard.stream().filter(e -> e instanceof Treasure).count();
	}
}
