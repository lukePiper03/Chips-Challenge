package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang
 *
 * represents a point in space. A helper class
 * @param x x position
 * @param y y position
 */
public record Point(int x, int y){
	  //Note: x==width, y==height
	  public Point add(int x,int y){
	    return new Point(x()+x, y()+y);
	  }
	  public Point add(Point p){
	    return add(p.x, p.y);
	  }
	  public Point times(int x, int y) {
	    return new Point(x()*x, y()*y);
	  }
	  public Point times(int v) {
	    return new Point(x()*v, y()*v);
	  }
	  
	  public Point distance(Point other){
	    return this.add(other.times(-1));
	  }
	  
	  public double size(){//Pythagoras here!
	    return Math.sqrt(x*x+y*y);
	  }

	}