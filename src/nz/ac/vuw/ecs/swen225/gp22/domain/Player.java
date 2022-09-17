package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;

/**
 * @author Linda Zhang
 *  the player of the game. Represents Chap
 */
public class Player {
	private Point pos;
	private Direction direction = Direction.None;
	double scale = 0.5;
	int timestamp = 5; //update position once every ten ticks
	int timeSinceLastMove = 5;
	
	Player(Point p){
		pos = p;
	}
	
	public Point getPos() {
		return new Point(pos.x(), pos.y());
	}
	public Direction direction(){ return direction; }
	public void direction(Direction d){ direction=d; }
	public Runnable set(Function<Direction,Direction> f){
		return ()->direction=f.apply(direction);
	}
	  
	/**
	 * the player at each tick
	 * @param cells the cells on current level
	 */
	public void tick(Cells cells){
		timeSinceLastMove++;
		
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
	public void move(Direction d, Cells cells) {
		if(d == Direction.None) return;
		timeSinceLastMove = 0;
		
		if(cells.get(pos).isSolid()) { 
			throw new IllegalArgumentException("Chap cannot be on a solid tile"); 
		}
		
		Point newPos = pos.add(d.point());
		if(!cells.get(newPos).isSolid()) { pos = newPos; };
	}
	
	/**
	 * draw the player on screen. Handled by Renderer.
	 * @param img
	 * @param g
	 * @param center
	 * @param size
	 */
	public void draw(BufferedImage img, Graphics g, Point center, Dimension size){
		double w1=pos.x()*64-(center.x()*64) + 64*(scale/2);
		double h1=pos.y()*64-(center.y()*64) + 64*(scale/2);
		double w2=w1+64*scale;
		double h2=h1+64*scale;

	    var isOut = h2<=0 || w2<=0 || h1>=size.height || w1>=size.width;
	    if(isOut){ return; }
	    g.drawImage(img,(int)w1,(int)h1,(int)w2,(int)h2,0,0,64,64,null);
	}
}
