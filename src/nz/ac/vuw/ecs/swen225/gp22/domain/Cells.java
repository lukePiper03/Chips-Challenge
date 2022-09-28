package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linda Zhang  300570498
 * The board of the current level in the game. Cells
 *         consist of many single cell objects.
 */
public class Cells {
	private int maxX;
	private int maxY;
	private Point spawn;
	private final List<List<Cell>> inner = new ArrayList<>();

	/**
	 * @param map cells loaded from the demo
	 */
	public Cells(char[][] map) {
		maxX = map[0].length;
		maxY = map.length;
		int countLockedDoors = 0;

		for (int x = 0; x < maxX; x++) {
			var tmp = new ArrayList<Cell>();
			inner.add(tmp);
			for (int y = 0; y < maxY; y++) {
				switch(map[y][x]) {
				case '#':
					tmp.add(new Cell(new Wall(), x, y));
					break;
				case 's': 
					tmp.add(new Cell(new Spawn(), x, y));
					spawn = new Point(x, y);
					break;
				case 'L':
					tmp.add(new Cell(new LockedDoor(++countLockedDoors), x, y)); //each door has a different code
					break;
				case 'X':
					tmp.add(new Cell(new ExitLock(), x, y));
					break;
				default:
					tmp.add(new Cell(new Floor(), x, y));
					break;
				}
			}
		}
	}

	/**
	 * Gets the cell on the x and y positon
	 * 
	 * @param x x position on the board
	 * @param y y position on the board
	 * @return the cell on the x and y position. If out of range, return a water
	 *         cell.
	 */
	public Cell get(int x, int y) {
		if (x < 0 || y < 0 || x >= maxX || y >= maxY) {
			return new Cell(new Water(), x, y); //return water cell if out of the map
		}
		Cell res = inner.get(x).get(y);
		assert res != null;
		return res;
	}

	/**
	 * Gets the cell on the point
	 * 
	 * @param p point of cell
	 * @return the cell on the point. If out of range, return a water cell.
	 */
	public Cell get(Point p) {
		return get(p.x(),p.y());
	}
	
	/**
	 * Gets all Locked doors on the board
	 * @return the list of Locked doors
	 */
	public List<Cell> getAllLockedDoors(){
		return inner.stream().flatMap(cells -> cells.stream())
						.filter(c -> c.state() instanceof LockedDoor)
						.toList();
	}
	
	/**
	 * @return the cell that represents the ExitLock
	 */
	public Cell getExitLock() {
		return inner.stream().flatMap(cells -> cells.stream())
				.filter(c -> c.state() instanceof ExitLock)
		        .findFirst()
		        .orElseThrow(() -> new IllegalStateException("No ExitLock exists on board!"));	
	}

	/**
	 * @return the point at which the player should spawn
	 */
	public Point getSpawn() {
		return spawn;
	}
	
	/**
	 * @return the maxX position of the cell board
	 */
	public int getMaxX() {
		return maxX;
	}
	
	/**
	 * @return the maxY position of the cell board
	 */
	public int getMaxY() {
		return maxY;
	}
	
	/**
	 * @return return Cells as a 2D array of symbols. Used for testing.
	 */
	public char[][] toMap(){
		char[][] newMap = new char[maxY][maxX];
		
		for (int y = 0; y < maxY; y++) {
			for (int x = 0; x < maxX; x++) {
				newMap[y][x] = inner.get(x).get(y).symbol();
			}
		}
		return newMap;
	}

}