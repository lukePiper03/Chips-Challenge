package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * The LockedDoor CellState must be public for other modules to access it. 
 * Otherwise there is no difference from the other CellStates
 */
public class LockedDoor implements CellState{
	int matchKeyCode;
	/**
	 * @param mkc matchKeyCode that matches the code of a Key
	 */
	public LockedDoor(int mkc) {matchKeyCode = mkc;}
	/**
	 * @return the matchKeyCode to matches the Key
	 */
	public int keyCode() {return matchKeyCode;}
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return 'L';}
}