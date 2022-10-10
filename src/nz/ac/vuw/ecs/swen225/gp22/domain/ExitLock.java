package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * Represents ExitLock CellState. Public for accessibility.
 */
public class ExitLock implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return 'X';}
}