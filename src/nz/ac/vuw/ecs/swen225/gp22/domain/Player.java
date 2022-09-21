package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;

/**
 * @author Linda Zhang 300570498
 *  the player of the game. Represents Chap
 */
public class Player {
	private Point pos;
	private Point oldPos; //for smooth rendering
	private Direction direction = Direction.None;
	int timestamp = 5; //update position once every ten ticks
	int timeSinceLastMove = 5;
	private List<Entity> entitiesOnBoard;
	private List<Entity> inventory = new ArrayList<>();
	private int treasuresToCollect; 
	
	Player(Point p, List<Entity> entities){
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
		return ()->direction=f.apply(direction);
	}
	
	/**
	 * @return all the entities on the current board
	 */
	public List<Entity> entitiesOnBoard() {return entitiesOnBoard;}
	
	/**
	 * @return all the entities in the players inventory (picked up)
	 */
	public List<Entity> inventory(){return inventory;}
	
	/**
	 * @return the number of treasures on the board currently
	 */
	public int treasuresToCollect() {return treasuresToCollect;}
	
	/**
	 * decrease the count for the number of treasures on the board
	 */
	public void decreaseTreasureCount() {treasuresToCollect --;}
	
	/**
	 * @return true if all treasures have been collected by the player
	 */
	public boolean allTreasuresCollected() {
		for(Entity e: entitiesOnBoard) {
			if(e instanceof Treasure) {
				return false;
			}
		}
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
			move(direction, cells);
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

		if(cells.get(pos).isSolid()) { 
			throw new IllegalArgumentException("Chap cannot be on a solid tile"); 
		}
		
		Point newPos = pos.add(d.point());
		oldPos = getPos();
		if(!cells.get(newPos).isSolid()) { pos = newPos; };
	}
	
	//total treasure count on the board
	private int totalTreasureCount() {
		int result = 0;
		for(Entity e: entitiesOnBoard) {
			if(e instanceof Treasure) {
				result++;
			}
		}
		return result;
	}
}
