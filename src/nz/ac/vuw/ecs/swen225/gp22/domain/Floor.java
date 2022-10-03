package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * The Floor CellState must be public for other modules to access it. 
 * Otherwise there is no difference from the other CellStates
 */
public class Floor implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return '.';}
}
