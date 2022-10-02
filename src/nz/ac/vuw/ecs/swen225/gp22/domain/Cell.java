package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * represents a single square cell on the game board.
 * It can have different states e.g. exitLock becomes floor once all treasures are collected
 */
public class Cell{
	private CellState state;
	private int x,y;
	
	/**
	 * @param state current state of the cell
	 * @param x x position
	 * @param y y position
	 */
	public Cell(CellState state, int x, int y) {
		this.state = state;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the state of the cell
	 */
	public CellState state() {return state;}
	/**
	 * @param s set the state of the cell
	 */
	public void setState(CellState s) {state = s;}
	/**
	 * @return x location
	 */
	public int x() {return x;}
	/**
	 * @return y location
	 */
	public int y() {return y;}
	
	/**
	 * @return if cell is solid so player cannot go through it
	 */
	public boolean isSolid() {return state.isSolid(this);}
	/**
	 * @return the character respresenting the state of the cell
	 */
	public char symbol() {return state.symbol(this);}
	/**
	 * @return the name displaying the state of the cell
	 */
	public String getName(){return state.getClass().getSimpleName();}
}