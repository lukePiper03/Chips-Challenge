package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * CellState for different types of cells (State pattern)
 * the state of a cell can be changed this way
 */
public interface CellState{
	/**
	 * @param self the Cell object continaing the CellState
	 * @return if the cell is solid (player cannot go through)
	 */
	boolean isSolid(Cell self);
	/**
	 * @param self the Cell object continaing the CellState
	 * @return the char representing this type of cell
	 */
	char symbol(Cell self);
}

class Wall implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return '#';}
}

class Spawn implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return 's';}
}