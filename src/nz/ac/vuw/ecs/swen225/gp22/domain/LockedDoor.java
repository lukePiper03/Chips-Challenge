package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang 300570498
 * The LockedDoor CellState must be public for other modules to access it. 
 * A LockedDoor can be red, blue or green.
 */
public class LockedDoor implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {throw new IllegalStateException("LockedDoor must be extended.");}
}

class RedLockedDoor extends LockedDoor{
	@Override
	public char symbol(Cell self) {return 'R';}
}

class BlueLockedDoor extends LockedDoor{
	@Override
	public char symbol(Cell self) {return 'B';}
}

class GreenLockedDoor extends LockedDoor{
	@Override
	public char symbol(Cell self) {return 'G';}
}