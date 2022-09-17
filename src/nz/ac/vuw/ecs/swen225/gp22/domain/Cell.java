package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.*;

/**
 * @author Linda Zhang
 * represents a single square cell on the game board.
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
	
	//getters/setters
	public CellState state() {return state;}
	public void setState(CellState s) {state = s;}
	public int x() {return x;}
	public int y() {return y;}
	
	//uses CellState to determine result
	public boolean isSolid() {return state.isSolid(this);}
	public char symbol() {return state.symbol(this);}
	//public void onPing() {state.onPing(this);}
	public Img getImage(){return state.getImage(this);}
}

/*
 * CellState interface for different types of cells to implement (State pattern)
 * the state of a cell can be changed this way
 */
interface CellState{
	boolean isSolid(Cell self);
	char symbol(Cell self);
	//void onPing(Cell self);
	Img getImage(Cell self);
}

class Floor implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return '_';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.floor;}
}

class Wall implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return '#';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.wall;}
}

//record Info(String message) implements CellState{
//	public boolean isSolid(Cell self) {return false;}
//	public char symbol(Cell self) {return 'i';}
//	//public void onPing(Cell self) {}
//	public Img getImage(Cell self) {return Img.water;}
//}

class LockedDoor implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return 'L';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.water;} //change later
}

class ExitLock implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return 'X';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.water;} //change later
}

class Spawn implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return 's';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.spawn;}
}

class Water implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return '.';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.water;}
}