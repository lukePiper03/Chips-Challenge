package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;

/**
 * Represents a Monster in the Level. It only interacts with the Player and moves around freely.
 * @author Linda Zhang 300570498
 */
public class Monster {
	private Point pos;
	private Point oldPos; //for smooth rendering
	private Direction direction = Direction.Down;
	private int timestamp = 20; //update position once every 20 ticks
	private int timeSinceLastMove = 20; //a counter from the last move
	private List<Direction> route;
	//private static final SecureRandom RANDOM = new SecureRandom();
	private int move = 0;
	
	/**
	 * Monster constructor
	 * @param pos the current position
	 * @param route the list of directions the monster should follow to move
	 */
	public Monster(Point pos, List<Direction> route) {
		this.pos = pos;
		oldPos = getPos();
		this.route = route;
		if(!checkRouteValid()) throw new IllegalArgumentException("Route does not loop back to start. Invalid");
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
	 * @return the route that the monster moves
	 */
	public List<Direction> getRoute(){return route;}
	
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
			direction = route.get(move%route.size()); 
			System.out.println(move);
			move++;
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
	
	//checks if the route makes a loop to where it began
	private boolean checkRouteValid() {
		AtomicInteger horizontal = new AtomicInteger(0);
		AtomicInteger vertical = new AtomicInteger(0);
		
		//go through route
		route.forEach(dir -> {
			if(dir == Direction.Up)vertical.incrementAndGet();
			if(dir == Direction.Down)vertical.decrementAndGet();
			if(dir == Direction.Right)vertical.incrementAndGet();
			if(dir == Direction.Left)vertical.decrementAndGet();
		});
		return horizontal.get() == 0 && vertical.get() == 0;
	}
	
}
