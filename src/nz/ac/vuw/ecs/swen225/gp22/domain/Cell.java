package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.*;

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
	
	//getters/setters
	public CellState state() {return state;}
	public void setState(CellState s) {state = s;}
	public int x() {return x;}
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
	 * @return the image displaying the state of the cell
	 */
	public Img getImage(){return state.getImage(this);}
}

/*
 * CellState for different types of cells (State pattern)
 * the state of a cell can be changed this way
 */
interface CellState{
	boolean isSolid(Cell self);
	char symbol(Cell self);
	Img getImage(Cell self);
}

class Floor implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return '.';}
	public Img getImage(Cell self) {return Img.floor;}
}

class Wall implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return '#';}
	public Img getImage(Cell self) {return Img.wall;}
}

class LockedDoor implements CellState{
	int matchKeyCode;
	public LockedDoor(int mkc) {matchKeyCode = mkc;} //must match the code of a key
	public int keyCode() {return matchKeyCode;}
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return 'L';}
	public Img getImage(Cell self) {return Img.locked_door;}
}

class ExitLock implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return 'X';}
	public Img getImage(Cell self) {return Img.exit_door;}
}

class Spawn implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return 's';}
	public Img getImage(Cell self) {return Img.spawn;}
}

class Water implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return 'w';}
	public Img getImage(Cell self) {return Img.water;}
}