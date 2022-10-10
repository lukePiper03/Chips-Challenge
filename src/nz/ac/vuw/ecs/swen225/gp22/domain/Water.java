package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * The Water CellState must be public for other modules to access it.
 */
public class Water implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return 'w';}
}