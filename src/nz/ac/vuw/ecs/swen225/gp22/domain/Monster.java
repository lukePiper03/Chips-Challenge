package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;

/**
 * Represents a Monster in the Level. It only interacts with the Player and moves around freely.
 * @author Linda Zhang 300570498
 */
public class Monster {
	private Point pos;
	private Point oldPos; //for smooth rendering
	private Direction direction = Direction.Down;
	private int timestamp = 5; //update position once every 5 ticks
	private int timeSinceLastMove = 5; //a counter from the last move
	private List<Direction> directions = new ArrayList<>();
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * Monster constructor
	 * @param pos the current position
	 */
	public Monster(Point pos) {
		this.pos = pos;
		oldPos = getPos();
		directions.addAll(List.of(Direction.Left, Direction.Right, Direction.Up, Direction.Down));
	}
	
	/**
	 * @return the position of the monster in a new point
	 */
	public Point getPos() {return new Point(pos.x(), pos.y());}
	/**
	 * @return the previous old point of the monster (for smooth animation in render)
	 */
	public Point getOldPos() {return new Point(oldPos.x(),oldPos.y());}
	
	/**
	 * @return the monster's current direction
	 */
	public Direction direction(){ return direction; }
	
	/**
	 * @return the moving time (for smooth animation in render)
	 */
	public float getMoveTime() {return (float) timeSinceLastMove / (float)timestamp;}
	
	/**
	 * @return the name of the class, to be used by renderer
	 */
	public String getName() {return this.getClass().getSimpleName();}
	
	
	/**
	 * the monster at each tick
	 * @param cells the cells on current level
	 */
	public void tick(Cells cells){
		if(timeSinceLastMove < timestamp) timeSinceLastMove++;
		
		//allow movement every 5 ticks
		if(timeSinceLastMove >= timestamp) {
			while(cells.get(pos.add(direction.point())).isSolid()) {
				direction = randomDirection(); //generate 'random' direction until move is legal
			}
			move(direction, cells);
		}
	}
	
	/**
	 * makes player move in a direction
	 * @param d direction
	 * @param cells the cells on current level
	 */
	public void move(Direction d, Cells cells){
		timeSinceLastMove = 0;
		Point newPos = pos.add(d.point());
		if(cells.get(newPos).isSolid()) { 
			throw new IllegalArgumentException("Monster cannot be on a solid tile"); 
		}
		oldPos = getPos();
		pos = newPos; 
	}
	
	//generates a 'random' direction
	private Direction randomDirection() {
		//Collections.shuffle(directions);
		Direction d = directions.get(RANDOM.nextInt(directions.size()));
		System.out.println("Random direction: "+d);
		return d;
	}
	
}
