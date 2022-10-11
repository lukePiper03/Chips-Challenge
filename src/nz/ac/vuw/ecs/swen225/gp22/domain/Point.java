package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * Represents a point in space. A helper class.
 * @param x x position width
 * @param y y position height
 * 
 * @author Linda Zhang 300570498
 */
public record Point(int x, int y){
	
	/**
	 * @param x xPos to add by
	 * @param y yPos to add by
	 * @return the resulting Point
	 */
	public Point add(int x,int y){
	    return new Point(x()+x, y()+y);
	  }
	
	/**
	 * @param p the point to add by
	 * @return the resulting Point
	 */
	public Point add(Point p){
	    return add(p.x, p.y);
	  }
	
	/**
	 * @param v the scale to multiply the x and y by
	 * @return the resulting Point
	 */
	
	public Point times(int v) {
		return new Point(x()*v, y()*v);
	}	
	
	/**
	 * @param other the other Point
	 * @return the distance between the points
	 */
	public Point distance(Point other){
	    return this.add(other.times(-1));
	}
	
	/**
	 * @param other
	 * @return return the euclidean distance from this point to the other
	 */	  
	public double euclidean(Point other) {
		return Math.hypot(this.x-other.x, this.y-other.y);
	}

}